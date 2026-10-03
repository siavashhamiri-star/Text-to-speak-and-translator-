export type Language = 'fa' | 'en';

export type SpeakerId = 'person_a' | 'person_b';

export type TranslationStyleId = 'natural' | 'friendly' | 'standard' | 'very_casual' | 'casual_slang';

export interface TranslationStyle {
  id: TranslationStyleId;
  titleEn: string;
  titleFa: string;
  descriptionEn: string;
  descriptionFa: string;
}

export interface TranslationResult {
  originalText: string;
  translatedText: string;
  sourceLanguage: Language;
  targetLanguage: Language;
  style: TranslationStyleId;
  isOffline: boolean;
  confidence: number;
  timestamp: number;
}

export interface ChatMessage {
  id: string;
  speaker: SpeakerId;
  originalText: string;
  translatedText: string;
  sourceLanguage: Language;
  targetLanguage: Language;
  style: TranslationStyleId;
  timestamp: number;
  isSpoken: boolean;
}

export type ConversationMode = 'side_by_side' | 'chat' | 'live_voice' | 'practice' | 'video' | 'android_artifacts';

export interface VocabularyItem {
  termEn: string;
  meaningFa: string;
  pronunciationEn: string;
  exampleEn: string;
}

export interface PracticeTurn {
  id: string;
  promptEn: string;
  promptFa: string;
  expectedAnswersEn: string[];
  explanationFa: string;
  vocabularyNotes: VocabularyItem[];
}

export interface PracticeScenario {
  id: string;
  categoryId: string;
  categoryTitleEn: string;
  categoryTitleFa: string;
  iconSymbol: string;
  titleEn: string;
  titleFa: string;
  descriptionEn: string;
  descriptionFa: string;
  turns: PracticeTurn[];
}

export interface EvaluationResult {
  scorePercent: number;
  feedbackEn: string;
  feedbackFa: string;
  isAccurate: boolean;
  bestMatchingExpected: string;
  matchedKeywords: string[];
}
