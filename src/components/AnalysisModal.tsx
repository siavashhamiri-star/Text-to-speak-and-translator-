import React, { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';
import { ChevronUp, ChevronDown, Info } from 'lucide-react';
import { AnalysisResult } from '../types';

interface AnalysisModalProps {
  isAnalyzing: boolean;
  analysisResult: AnalysisResult | null;
  onClose: () => void;
  paceMode: 'gross' | 'net';
  setPaceMode: (mode: 'gross' | 'net') => void;
  totalWords: number;
  fillerCount: number;
  netWords: number;
  speechDurationSeconds: number;
  grossPaceWpm: number;
  netPaceWpm: number;
  currentPaceWpm: number;
  pacePercentage: number;
  fillerPercentage: number;
}

export const AnalysisModal: React.FC<AnalysisModalProps> = ({
  isAnalyzing,
  analysisResult,
  onClose,
  paceMode,
  setPaceMode,
  totalWords,
  fillerCount,
  netWords,
  speechDurationSeconds,
  grossPaceWpm,
  netPaceWpm,
  currentPaceWpm,
  pacePercentage,
  fillerPercentage,
}) => {
  const [isPaceBreakdownOpen, setIsPaceBreakdownOpen] = useState(false);

  if (!isAnalyzing && !analysisResult) return null;

  return (
    <AnimatePresence>
      <motion.div
        initial={{ opacity: 0, y: 80 }}
        animate={{ opacity: 1, y: 0 }}
        exit={{ opacity: 0, y: 80 }}
        transition={{ type: 'spring', stiffness: 240, damping: 26 }}
        className="fixed inset-x-0 bottom-0 top-16 z-50 overflow-y-auto bg-black/95 backdrop-blur-2xl border-t border-white/20 p-6 md:p-10"
      >
        <div className="max-w-4xl mx-auto space-y-6">
          {/* Header */}
          <div className="flex items-center justify-between border-b border-white/10 pb-4">
            <div className="flex items-center gap-3">
              <img
                src="https://www.gstatic.com/lamda/images/gemini_sparkle_aurora_33f86dc0c0257da337c63.svg"
                alt="Gemini Sparkle"
                className="w-8 h-8"
              />
              <div>
                <h2 className="font-sans text-2xl font-bold text-white">Speech Analysis & Coaching</h2>
                <p className="font-mono text-xs text-white/60">Benchmarked against speech and communication standards</p>
              </div>
            </div>
            <button
              onClick={onClose}
              className="px-4 py-1.5 border border-white bg-white text-black font-mono text-xs font-bold uppercase tracking-wider hover:bg-gray-200 cursor-pointer"
            >
              Close & Resume
            </button>
          </div>

          {isAnalyzing ? (
            <div className="py-20 flex flex-col items-center justify-center space-y-4">
              <motion.div
                animate={{ scale: [1, 1.1, 1], opacity: [0.8, 1, 0.8] }}
                transition={{ repeat: Infinity, duration: 2, ease: 'easeInOut' }}
              >
                <img
                  src="https://www.gstatic.com/lamda/images/gemini_sparkle_aurora_33f86dc0c0257da337c63.svg"
                  alt="Analyzing"
                  className="w-16 h-16"
                />
              </motion.div>
              <p className="text-xl font-mono text-white/70">Analyzing your dual-stream speech...</p>
            </div>
          ) : analysisResult ? (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Benchmark Score Card */}
              <div className="p-6 rounded-3xl bg-zinc-900 flex flex-col justify-between hover:bg-zinc-800 transition-colors">
                <span className="font-semibold text-lg text-white">Benchmark Score</span>
                <div className="flex items-end justify-between mt-4">
                  <div>
                    <div className="flex items-baseline gap-1">
                      <span className="text-6xl font-bold tracking-tight text-white">{analysisResult.overallScore || 0}</span>
                      <span className="text-xl font-bold text-white">/100</span>
                    </div>
                    <p className="text-white/50 text-base mt-2">
                      {analysisResult.overallScore >= 80 ? 'Proficient Delivery' : 'Developing Delivery'}
                    </p>
                  </div>
                  <div className="w-32 h-20">
                    <ResponsiveContainer width="100%" height="100%">
                      <PieChart>
                        <Pie
                          data={[{ value: analysisResult.overallScore || 0 }, { value: 100 - (analysisResult.overallScore || 0) }]}
                          cx="50%"
                          cy="100%"
                          startAngle={180}
                          endAngle={0}
                          innerRadius={25}
                          outerRadius={35}
                          paddingAngle={5}
                          dataKey="value"
                          stroke="none"
                        >
                          <Cell fill="#6366f1" />
                          <Cell fill="#3f3f46" />
                        </Pie>
                      </PieChart>
                    </ResponsiveContainer>
                  </div>
                </div>
              </div>

              {/* Speaking Pace Card */}
              <div className="p-6 rounded-3xl bg-zinc-900 flex flex-col justify-between hover:bg-zinc-800 transition-colors">
                <span className="font-semibold text-lg text-white">Speaking Pace</span>
                <div className="mt-4">
                  <div className="flex items-baseline gap-2">
                    <span className="text-5xl font-bold tracking-tight text-white">{currentPaceWpm}</span>
                    <span className="text-xl font-bold text-white/70">WPM ({paceMode.toUpperCase()})</span>
                  </div>
                  <p className="text-white/60 text-sm mt-2">{analysisResult.speakingPaceFeedback}</p>
                </div>
                <div className="flex gap-2 mt-4">
                  <button
                    onClick={() => setPaceMode('gross')}
                    className={`px-3 py-1 text-xs font-mono rounded cursor-pointer ${paceMode === 'gross' ? 'bg-white text-black font-bold' : 'bg-white/10 text-white'}`}
                  >
                    Gross: {grossPaceWpm} WPM
                  </button>
                  <button
                    onClick={() => setPaceMode('net')}
                    className={`px-3 py-1 text-xs font-mono rounded cursor-pointer ${paceMode === 'net' ? 'bg-white text-black font-bold' : 'bg-white/10 text-white'}`}
                  >
                    Net: {netPaceWpm} WPM
                  </button>
                </div>
              </div>

              {/* Filler Words Card */}
              <div className="p-6 rounded-3xl bg-zinc-900 flex flex-col hover:bg-zinc-800 transition-colors">
                <span className="font-semibold text-lg text-white">Filler Words Detected</span>
                <div className="flex items-baseline gap-2 mt-4">
                  <span className="text-5xl font-bold text-white">{fillerCount}</span>
                  <span className="text-lg text-white/60">words</span>
                </div>
                {analysisResult.fillerWordsBreakdown && analysisResult.fillerWordsBreakdown.length > 0 && (
                  <div className="flex flex-wrap gap-2 mt-4">
                    {analysisResult.fillerWordsBreakdown.map((item, idx) => (
                      <span key={idx} className="px-2.5 py-1 bg-white/10 rounded-full text-xs font-mono text-white/80">
                        &quot;{item.word}&quot; &times; {item.count}
                      </span>
                    ))}
                  </div>
                )}
              </div>

              {/* Feedback Summary */}
              <div className="p-6 rounded-3xl bg-zinc-900 flex flex-col justify-between hover:bg-zinc-800 transition-colors">
                <span className="font-semibold text-lg text-white">Overall Feedback</span>
                <p className="text-white/80 text-sm leading-relaxed mt-3">{analysisResult.overallFeedback}</p>
                <div className="mt-4 pt-3 border-t border-white/10 flex flex-col gap-1 text-xs text-white/60">
                  <div><strong className="text-white">Strengths:</strong> {analysisResult.strengths?.slice(0, 2).join(', ')}</div>
                  <div><strong className="text-white">To Improve:</strong> {analysisResult.areasForImprovement?.slice(0, 2).join(', ')}</div>
                </div>
              </div>
            </div>
          ) : null}
        </div>
      </motion.div>
    </AnimatePresence>
  );
};
