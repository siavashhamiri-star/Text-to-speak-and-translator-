import express from 'express';
import http from 'http';
import path from 'path';
import WebSocket, { WebSocketServer } from 'ws';
import { GoogleGenAI, Type, Schema } from '@google/genai';

async function startServer() {
  const app = express();
  const PORT = 3000;

  app.use(express.json({ limit: '50mb' }));

  // Health check endpoint
  app.get('/api/health', (req, res) => {
    res.json({
      status: 'ok',
      hasApiKey: Boolean(process.env.GEMINI_API_KEY),
    });
  });

  // Analysis endpoint
  app.post('/api/analyze', async (req, res) => {
    const apiKey = process.env.GEMINI_API_KEY;
    if (!apiKey) {
      return res.status(500).json({ error: 'GEMINI_API_KEY is not set' });
    }

    const { transcript, durationSeconds, audioBase64, audioMimeType } = req.body;
    if (!transcript) {
      return res.status(400).json({ error: 'Transcript is required' });
    }

    const words = transcript.trim().split(/\s+/).filter((w: string) => w.length > 0);
    const totalWords = words.length;
    const validDuration = Math.max(1, Math.round(Number(durationSeconds) || 60));

    try {
      const ai = new GoogleGenAI({ apiKey });
      
      const contents: any[] = [
        `You are an expert public speaking coach. Analyze the following transcript and audio (if provided) of a user practicing for an interview or speech.
The user spoke for approximately ${validDuration} seconds with a total of ${totalWords} words.
Transcript: "${transcript}"

Analyze their vocal delivery, sentence formation, speaking style, pauses, cadence, and identify all filler words (such as "um", "uh", "like", "er", "ah", "you know", "basically", "sort of", repeated words).
Provide precise evaluation on pacing, strengths, and actionable areas for improvement.`
      ];
      
      if (audioBase64 && audioMimeType) {
        contents.push({
          inlineData: {
            data: audioBase64,
            mimeType: audioMimeType
          }
        });
      }

      const response = await ai.models.generateContent({
        model: 'gemini-3.6-flash',
        contents: contents,
        config: {
          responseMimeType: 'application/json',
          responseSchema: {
            type: Type.OBJECT,
            properties: {
              overallScore: { type: Type.INTEGER, description: 'Overall score from 0 to 100.' },
              speakingPaceFeedback: { type: Type.STRING, description: 'Feedback on the speaking pace (e.g., Natural and stable pace, Slightly rushed, Too slow).' },
              sentenceFormation: { type: Type.STRING, description: 'Feedback on sentence structure, coherence, and grammatical clarity.' },
              speakingStyle: { type: Type.STRING, description: 'Feedback on tone, confidence, pauses, and cadence.' },
              fillerWordsCount: { type: Type.INTEGER, description: 'Count of filler words and verbal disfluencies identified.' },
              fillerWordsList: { type: Type.ARRAY, items: { type: Type.STRING }, description: 'Specific filler words or phrases used (e.g., um, uh, like).' },
              overallFeedback: { type: Type.STRING, description: 'A constructive overall summary of the performance.' },
              strengths: { type: Type.ARRAY, items: { type: Type.STRING }, description: 'Key strengths of the speech.' },
              areasForImprovement: { type: Type.ARRAY, items: { type: Type.STRING }, description: 'Specific actionable areas to improve.' }
            },
            required: ['overallScore', 'speakingPaceFeedback', 'sentenceFormation', 'speakingStyle', 'fillerWordsCount', 'fillerWordsList', 'overallFeedback', 'strengths', 'areasForImprovement']
          }
        }
      });
      
      const jsonText = response.text || "{}";
      const parsed = JSON.parse(jsonText);

      const fillerWordsList: string[] = Array.isArray(parsed.fillerWordsList) ? parsed.fillerWordsList : [];
      const reportedFillerCount = typeof parsed.fillerWordsCount === 'number' ? parsed.fillerWordsCount : fillerWordsList.length;
      const fillerWordsCount = Math.min(totalWords, Math.max(0, reportedFillerCount));

      const netWords = Math.max(0, totalWords - fillerWordsCount);
      const grossWpm = Math.round((totalWords / validDuration) * 60);
      const netWpm = Math.round((netWords / validDuration) * 60);

      // Build filler words frequency map
      const fillerMap: Record<string, number> = {};
      fillerWordsList.forEach((w: string) => {
        const clean = w.toLowerCase().replace(/[^a-z0-9 ']/g, '').trim();
        if (clean) {
          fillerMap[clean] = (fillerMap[clean] || 0) + 1;
        }
      });
      const fillerWordsBreakdown = Object.entries(fillerMap).map(([word, count]) => ({ word, count }));

      const finalResult = {
        ...parsed,
        totalWords,
        netWords,
        durationSeconds: validDuration,
        grossWpm,
        netWpm,
        speakingPaceWpm: grossWpm,
        fillerWordsCount,
        fillerWordsBreakdown,
        fillerWordsRetentionExplanation: 'Filler words (e.g., "um", "uh", "like") are retained in the live transcript for visibility. Net WPM removes filler pauses to measure your true substantive speaking pace.'
      };

      res.json(finalResult);
    } catch (err: any) {
      console.error('Analysis error:', err);
      res.status(500).json({ error: 'Failed to analyze transcript' });
    }
  });

  const server = http.createServer(app);

  // WebSocket server for live transcription relay
  const wss = new WebSocketServer({ server, path: '/ws/transcribe' });

  wss.on('connection', (clientWs, req) => {
    const apiKey = process.env.GEMINI_API_KEY;

    if (!apiKey) {
      console.error('GEMINI_API_KEY is not set');
      clientWs.send(
        JSON.stringify({
          error: 'GEMINI_API_KEY environment variable is missing on the server.',
        })
      );
      clientWs.close();
      return;
    }

    console.log('Client connected to WebSocket transcription proxy');

    const geminiWsUrl = `wss://generativelanguage.googleapis.com/ws/google.ai.generativelanguage.v1alpha.GenerativeService.BidiGenerateContent?key=${apiKey}`;
    let isClientIntentionallyClosed = false;

    // Configuration from client or defaults
    let baseConfig = {
      model: 'models/gemini-3.5-transcribe-live',
      languageCodes: [] as string[],
      customVocabulary: ['like', 'um', 'uh', 'you know'],
    };

    class LiveSession {
      mode: 'smart' | 'verbatim';
      ws: WebSocket | null = null;
      isConnected: boolean = false;
      pendingMessages: string[] = [];

      constructor(mode: 'smart' | 'verbatim') {
        this.mode = mode;
      }

      connect() {
        try {
          this.ws = new WebSocket(geminiWsUrl);
        } catch (err) {
          console.error(`[${this.mode}] Failed to create Gemini WebSocket:`, err);
          return;
        }

        this.ws.on('open', () => {
          console.log(`[${this.mode}] Connected to Gemini Live API WebSocket`);
          this.isConnected = true;

          const vocab = baseConfig.customVocabulary || [];

          const sessionSetupMessage = {
            setup: {
              model: baseConfig.model,
              generationConfig: {
                responseModalities: ["TEXT"]
              },
              inputAudioTranscription: {
                mode: this.mode.toUpperCase(),
                ...(baseConfig.languageCodes.length > 0
                  ? { 
                      languageCodes: baseConfig.languageCodes
                    }
                  : {}),
                ...(vocab.length > 0
                  ? { 
                      customVocabulary: vocab
                    }
                  : {}),
              },
            },
          };

          console.log(`[${this.mode}] Sending setup message:`, JSON.stringify(sessionSetupMessage));
          this.ws?.send(JSON.stringify(sessionSetupMessage));

          while (this.pendingMessages.length > 0) {
            const msg = this.pendingMessages.shift();
            if (msg && this.ws?.readyState === WebSocket.OPEN) {
              this.ws.send(msg);
            }
          }

          if (clientWs.readyState === WebSocket.OPEN) {
            clientWs.send(JSON.stringify({ 
              type: 'connected', 
              status: `Gemini ${this.mode.toUpperCase()} Live Session Ready`,
              mode: this.mode 
            }));
          }
        });

        this.ws.on('message', (data: WebSocket.RawData) => {
          try {
            const strData = data.toString();
            const parsed = JSON.parse(strData);
            const serverContent = parsed.serverContent || parsed.server_content;
            
            if (clientWs.readyState === WebSocket.OPEN) {
              clientWs.send(JSON.stringify({
                type: 'gemini_response',
                mode: this.mode,
                raw: parsed,
                serverContent: serverContent || null,
              }));
            }
          } catch (e) {
            console.error(`[${this.mode}] Error parsing message:`, e);
          }
        });

        this.ws.on('error', (err) => {
          console.error(`[${this.mode}] WebSocket error:`, err);
        });

        this.ws.on('close', (code, reason) => {
          console.log(`[${this.mode}] WebSocket closed: ${code} - ${reason.toString()}`);
          this.isConnected = false;
          
          if (!isClientIntentionallyClosed && clientWs.readyState === WebSocket.OPEN) {
            console.log(`[${this.mode}] Auto-reconnecting to Gemini Live API...`);
            setTimeout(() => this.connect(), 600);
          }
        });
      }

      sendAudio(realtimePayload: string) {
        if (this.isConnected && this.ws?.readyState === WebSocket.OPEN) {
          this.ws.send(realtimePayload);
        } else {
          this.pendingMessages.push(realtimePayload);
        }
      }

      close() {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
          this.ws.close();
        }
      }
    }

    const smartSession = new LiveSession('smart');
    const verbatimSession = new LiveSession('verbatim');

    // Handle incoming messages from the client
    clientWs.on('message', (data: WebSocket.RawData) => {
      try {
        const msg = JSON.parse(data.toString());

        if (msg.type === 'configure') {
          if (msg.customVocabulary || msg.custom_vocabulary) {
            baseConfig.customVocabulary = msg.customVocabulary || msg.custom_vocabulary;
          }
          if (msg.languageCodes || msg.language_codes) {
            baseConfig.languageCodes = msg.languageCodes || msg.language_codes;
          }
          if (msg.model) {
            baseConfig.model = msg.model;
          }
          // Now that we have the configuration, connect to Gemini
          smartSession.connect();
          verbatimSession.connect();
          return;
        }

        if (msg.type === 'audio' || msg.audio) {
          const audioBase64 = msg.audio || msg.data;
          const mimeType = msg.mimeType || 'audio/pcm;rate=16000';

          const realtimePayload = JSON.stringify({
            realtimeInput: {
              mediaChunks: [
                {
                  mimeType: mimeType,
                  data: audioBase64,
                },
              ],
            },
          });

          smartSession.sendAudio(realtimePayload);
          verbatimSession.sendAudio(realtimePayload);
        }
      } catch (err) {
        console.error('Error handling client message:', err);
      }
    });

    clientWs.on('close', () => {
      console.log('Client WebSocket connection closed');
      isClientIntentionallyClosed = true;
      smartSession.close();
      verbatimSession.close();
    });
  });

  // Vite development middleware or production static build serving
  if (process.env.NODE_ENV !== 'production') {
    const { createServer: createViteServer } = await import('vite');
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: 'spa',
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.join(process.cwd(), 'dist');
    app.use(express.static(distPath));
    app.get('*', (req, res) => {
      res.sendFile(path.join(distPath, 'index.html'));
    });
  }

  server.listen(PORT, '0.0.0.0', () => {
    console.log(`Server listening on http://0.0.0.0:${PORT}`);
  });
}

startServer().catch((err) => {
  console.error('Failed to start server:', err);
});
