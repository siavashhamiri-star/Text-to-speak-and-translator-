import { Language } from '../types';

export function speakText(text: string, language: Language) {
  if (!('speechSynthesis' in window)) {
    console.warn('Text-to-speech is not supported in this browser.');
    return;
  }
  window.speechSynthesis.cancel();
  const utterance = new SpeechSynthesisUtterance(text);
  utterance.lang = language === 'fa' ? 'fa-IR' : 'en-US';
  utterance.rate = 0.95;
  utterance.pitch = 1.0;

  // Attempt to select matching voice
  const voices = window.speechSynthesis.getVoices();
  const matchedVoice = voices.find((v) => v.lang.startsWith(language));
  if (matchedVoice) {
    utterance.voice = matchedVoice;
  }

  window.speechSynthesis.speak(utterance);
}

export class BrowserSpeechRecognition {
  private recognition: any = null;
  private isListening = false;

  constructor(
    private onResult: (text: string, isFinal: boolean) => void,
    private onError: (error: string) => void,
    private onEnd: () => void
  ) {
    const SpeechRecognition =
      (window as any).SpeechRecognition || (window as any).webkitSpeechRecognition;
    if (SpeechRecognition) {
      this.recognition = new SpeechRecognition();
      this.recognition.continuous = false;
      this.recognition.interimResults = true;

      this.recognition.onresult = (event: any) => {
        let interim = '';
        let final = '';
        for (let i = event.resultIndex; i < event.results.length; ++i) {
          if (event.results[i].isFinal) {
            final += event.results[i][0].transcript;
          } else {
            interim += event.results[i][0].transcript;
          }
        }
        if (final) {
          this.onResult(final, true);
        } else if (interim) {
          this.onResult(interim, false);
        }
      };

      this.recognition.onerror = (event: any) => {
        this.isListening = false;
        this.onError(event.error || 'Speech recognition error');
      };

      this.recognition.onend = () => {
        this.isListening = false;
        this.onEnd();
      };
    }
  }

  public isSupported(): boolean {
    return Boolean(this.recognition);
  }

  public start(language: Language) {
    if (!this.recognition) {
      this.onError('Speech recognition is not supported in this browser.');
      return;
    }
    if (this.isListening) {
      this.stop();
    }
    this.recognition.lang = language === 'fa' ? 'fa-IR' : 'en-US';
    try {
      this.recognition.start();
      this.isListening = true;
    } catch (e: any) {
      this.onError(e.message || 'Failed to start recognition');
    }
  }

  public stop() {
    if (this.recognition && this.isListening) {
      this.recognition.stop();
      this.isListening = false;
    }
  }
}
