import React, { useState } from 'react';
import { X, Plus, Trash2, Code2, Sparkles, Languages, Check, Info, Wand2, Mic } from 'lucide-react';
import { SetupConfig, TranscriptionMode } from '../types';

interface SetupPanelProps {
  isOpen: boolean;
  onClose: () => void;
  config: SetupConfig;
  onSaveConfig: (newConfig: SetupConfig) => void;
}

export const SetupPanel: React.FC<SetupPanelProps> = ({
  isOpen,
  onClose,
  config,
  onSaveConfig,
}) => {
  const [phrases, setPhrases] = useState<string[]>(config.customVocabulary);
  const [newPhrase, setNewPhrase] = useState('');
  const [languages, setLanguages] = useState<string[]>(config.languageCodes);
  const [newLanguage, setNewLanguage] = useState('');
  const [model, setModel] = useState(config.model);
  const [saved, setSaved] = useState(false);

  if (!isOpen) return null;

  const handleAddPhrase = () => {
    if (newPhrase.trim() && !phrases.includes(newPhrase.trim())) {
      setPhrases([...phrases, newPhrase.trim()]);
      setNewPhrase('');
    }
  };

  const handleRemovePhrase = (phraseToRemove: string) => {
    setPhrases(phrases.filter((p) => p !== phraseToRemove));
  };

  const handleAddLanguage = () => {
    if (newLanguage.trim() && !languages.includes(newLanguage.trim())) {
      setLanguages([...languages, newLanguage.trim()]);
      setNewLanguage('');
    }
  };

  const handleRemoveLanguage = (langToRemove: string) => {
    setLanguages(languages.filter((l) => l !== langToRemove));
  };

  const handleSave = () => {
    onSaveConfig({
      model,
      customVocabulary: phrases,
      languageCodes: languages,
    });
    setSaved(true);
    setTimeout(() => {
      setSaved(false);
      onClose();
    }, 1000);
  };

  const sessionSetupPayload = {
    setup: {
      model: model,
      generationConfig: {
        responseModalities: ["TEXT"]
      },
      inputAudioTranscription: {
        mode: "SMART (and VERBATIM simultaneously)",
        ...(languages.length > 0 ? { languageCodes: languages } : {}),
        ...(phrases.length > 0 ? { customVocabulary: phrases } : {}),
      },
    },
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      {/* Backdrop */}
      <div className="absolute inset-0 bg-black/80 backdrop-blur-sm" onClick={onClose} />
      
      <div className="relative z-10 flex max-h-[90vh] w-full max-w-2xl flex-col overflow-hidden border border-white bg-black">
        {/* Modal Header */}
        <div className="flex items-center justify-between border-b border-white bg-black px-6 py-4">
          <div className="flex items-center gap-3">
            <div>
              <h2 className="font-mono text-sm font-bold uppercase tracking-widest text-white">
                CONFIG.EXE
              </h2>
              <p className="font-mono text-[10px] uppercase text-white/60">
                sessionSetupMessage.setup payload constructor (RFC-153 VVT)
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="flex h-8 w-8 items-center justify-center border border-white bg-black text-white transition-colors hover:bg-white/10"
          >
            <X className="h-4 w-4" />
          </button>
        </div>

        {/* Modal Body */}
        <div className="flex-1 overflow-y-auto bg-black p-6 space-y-6 text-white">
          {/* Model Selection */}
          <div>
            <label className="mb-1.5 block font-mono text-[10px] font-bold uppercase tracking-widest text-white/60">
              Live Model ID
            </label>
            <input
              type="text"
              value={model}
              onChange={(e) => setModel(e.target.value)}
              className="w-full border border-white bg-black px-3 py-2 font-mono text-xs text-white focus:outline-none focus:bg-white/10"
            />
          </div>

          {/* Custom Vocabulary / Adaptation Phrases */}
          <div>
            <div className="mb-1.5 flex items-center justify-between">
              <label className="font-mono text-[10px] font-bold uppercase tracking-widest text-white/60">
                Custom Vocabulary (custom_vocabulary)
              </label>
              <span className="font-mono text-[10px] text-white/40">custom_vocabulary</span>
            </div>
            <p className="mb-3 font-mono text-xs text-white/50 leading-relaxed">
              Define target technical terminology, proper nouns, or domain keywords to bias the speech recognition model toward recognizing specific terms.
            </p>

            <div className="mb-3 flex gap-2">
              <input
                type="text"
                placeholder="e.g. Kubernetes, Anthos, Spanner, Thorsten..."
                value={newPhrase}
                onChange={(e) => setNewPhrase(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleAddPhrase()}
                className="flex-1 border border-white bg-black px-3 py-1.5 font-mono text-xs text-white focus:outline-none focus:bg-white/10"
              />
              <button
                onClick={handleAddPhrase}
                className="flex items-center gap-1 border border-white bg-black px-3 py-1.5 font-mono text-xs font-bold uppercase tracking-widest text-white transition-colors hover:bg-white/10"
              >
                <Plus className="h-3.5 w-3.5" />
                <span>Add</span>
              </button>
            </div>

            <div className="flex flex-wrap gap-2">
              {phrases.map((phrase) => (
                <span
                  key={phrase}
                  className="inline-flex items-center gap-1.5 border border-white bg-white/10 px-2.5 py-1 font-mono text-xs font-bold text-white"
                >
                  <span>{phrase}</span>
                  <button
                    onClick={() => handleRemovePhrase(phrase)}
                    className="text-white/40 hover:text-red-500 cursor-pointer"
                  >
                    <Trash2 className="h-3 w-3" />
                  </button>
                </span>
              ))}
              {phrases.length === 0 && (
                <p className="font-mono text-xs italic text-white/40">No custom vocabulary added.</p>
              )}
            </div>
          </div>

          {/* Language Hints */}
          <div>
            <div className="mb-1.5 flex items-center justify-between">
              <label className="flex items-center gap-1.5 font-mono text-[10px] font-bold uppercase tracking-widest text-white/60">
                <Languages className="h-3.5 w-3.5" />
                <span>Language Codes (Optional)</span>
              </label>
              <span className="font-mono text-[10px] text-white/40">language_codes</span>
            </div>

            <div className="mb-3 flex gap-2">
              <input
                type="text"
                placeholder="e.g. en-US, de-DE, es-ES, ja-JP..."
                value={newLanguage}
                onChange={(e) => setNewLanguage(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleAddLanguage()}
                className="flex-1 border border-white bg-black px-3 py-1.5 font-mono text-xs text-white focus:outline-none focus:bg-white/10"
              />
              <button
                onClick={handleAddLanguage}
                className="flex items-center gap-1 border border-white bg-black px-3 py-1.5 font-mono text-xs font-bold uppercase tracking-widest text-white transition-colors hover:bg-white/10"
              >
                <Plus className="h-3.5 w-3.5" />
                <span>Add</span>
              </button>
            </div>

            <div className="flex flex-wrap gap-2">
              {languages.map((code) => (
                <span
                  key={code}
                  className="inline-flex items-center gap-1.5 border border-white bg-white/10 px-2.5 py-1 font-mono text-xs font-bold text-white"
                >
                  <span>{code}</span>
                  <button
                    onClick={() => handleRemoveLanguage(code)}
                    className="text-white/40 hover:text-red-500 cursor-pointer"
                  >
                    <Trash2 className="h-3 w-3" />
                  </button>
                </span>
              ))}
              {languages.length === 0 && (
                <p className="font-mono text-xs italic text-white/40">Automatic language detection enabled.</p>
              )}
            </div>
          </div>

          {/* Payload Inspector */}
          <div>
            <div className="mb-2 flex items-center gap-1.5 font-mono text-[10px] font-bold uppercase tracking-widest text-white/60">
              <Code2 className="h-3.5 w-3.5" />
              <span>Live Setup JSON Payload (RFC-153 Schema)</span>
            </div>
            <pre className="overflow-x-auto border border-white bg-white/5 p-4 font-mono text-[10px] text-white leading-relaxed">
              {JSON.stringify(sessionSetupPayload, null, 2)}
            </pre>
          </div>
        </div>

        {/* Modal Footer */}
        <div className="flex items-center justify-between border-t border-white bg-black px-6 py-4">
          <div className="flex items-center gap-1.5 font-mono text-[10px] text-white/40 uppercase font-bold">
            <Info className="h-3.5 w-3.5" />
            <span>Updates setup on next session</span>
          </div>
          <div className="flex items-center gap-4">
            <button
              onClick={onClose}
              className="font-mono text-xs font-bold uppercase tracking-widest text-white/60 hover:text-white cursor-pointer"
            >
              Cancel
            </button>
            <button
              onClick={handleSave}
              className="flex items-center gap-1.5 border border-white bg-white px-5 py-2 font-mono text-xs font-bold uppercase tracking-widest text-black transition-colors hover:bg-gray-200 cursor-pointer"
            >
              {saved ? (
                <>
                  <Check className="h-3.5 w-3.5" />
                  <span>Applied</span>
                </>
              ) : (
                <span>Apply Setup</span>
              )}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

