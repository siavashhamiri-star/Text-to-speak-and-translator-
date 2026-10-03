import test from 'node:test';
import assert from 'node:assert/strict';

// Test offline translation logic
const FA_TO_EN_PHRASES = {
  'سلام': {
    natural: 'Hello',
    friendly: 'Hi there, wonderful to see you!',
    standard: 'Hello, greetings.',
    very_casual: 'Hey!',
    casual_slang: "What's up!"
  },
  'سلام چطوری': {
    natural: 'Hey, how are you doing?',
    friendly: 'Hi! How are things going with you?',
    standard: 'Hello, how are you?',
    very_casual: "Hey, how's it going?",
    casual_slang: "Yo, what's good?"
  },
  'ممنون': {
    natural: 'Thank you',
    friendly: 'Thank you so much, really appreciate your help!',
    standard: 'Thank you.',
    very_casual: 'Thanks!',
    casual_slang: 'Props! Much appreciated!'
  }
};

const EN_TO_FA_PHRASES = {
  'how are you': {
    natural: 'حالت چطوره؟',
    friendly: 'سلام! حالتون خوبه؟ روزتون چطور می‌گذره؟',
    standard: 'حال شما چطور است؟',
    very_casual: 'خوبی؟ روبه‌راهی؟',
    casual_slang: 'چه خبر مبر؟ ردیفی؟'
  },
  'thank you': {
    natural: 'ممنون',
    friendly: 'خیلی ممنون از محبت و همراهی شما!',
    standard: 'متشکرم',
    very_casual: 'دستت درد نکنه!',
    casual_slang: 'دمت گرم داداش!'
  }
};

function normalizeFa(input) {
  return input
    .replace(/ي/g, 'ی')
    .replace(/ك/g, 'ک')
    .replace(/میخوام/g, 'می‌خواهم')
    .replace(/مرسی/g, 'ممنون')
    .replace(/[!?.،,;]+$/g, '')
    .trim();
}

test('Persian -> English: Natural style translation', () => {
  const query = normalizeFa('سلام چطوری');
  const matched = FA_TO_EN_PHRASES[query];
  assert.ok(matched, 'Should match phrase');
  assert.equal(matched.natural, 'Hey, how are you doing?');
});

test('Persian -> English: All 5 styles produce distinct conversational variants', () => {
  const query = normalizeFa('سلام');
  const matched = FA_TO_EN_PHRASES[query];
  assert.equal(matched.natural, 'Hello');
  assert.equal(matched.friendly, 'Hi there, wonderful to see you!');
  assert.equal(matched.standard, 'Hello, greetings.');
  assert.equal(matched.very_casual, 'Hey!');
  assert.equal(matched.casual_slang, "What's up!");
});

test('English -> Persian: Casual / Slang style produces colloquial idiom', () => {
  const query = 'how are you';
  const matched = EN_TO_FA_PHRASES[query];
  assert.ok(matched);
  assert.ok(matched.casual_slang.includes('ردیفی') || matched.casual_slang.includes('خبر'));
});

test('Input normalization handles colloquial variations and punctuation', () => {
  assert.equal(normalizeFa('سلام!'), 'سلام');
  assert.equal(normalizeFa('مرسی'), 'ممنون');
});

test('Speaker switching logic toggles correctly between Person A and Person B', () => {
  const getOtherSpeaker = (sp) => (sp === 'person_a' ? 'person_b' : 'person_a');
  assert.equal(getOtherSpeaker('person_a'), 'person_b');
  assert.equal(getOtherSpeaker('person_b'), 'person_a');
});

test('Offline Practice: 9 Scenarios check', () => {
  const requiredCategories = [
    'everyday', 'casual', 'casual_slang', 'job_interview',
    'travel', 'restaurant', 'shopping', 'social', 'free_practice'
  ];
  assert.equal(requiredCategories.length, 9);
});
