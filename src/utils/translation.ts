import { Language, TranslationResult, TranslationStyleId } from '../types';

interface StyleVariants {
  natural: string;
  friendly: string;
  standard: string;
  very_casual: string;
  casual_slang: string;
}

const FA_TO_EN_PHRASES: Record<string, StyleVariants> = {
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
  'خوبی': {
    natural: 'You doing good?',
    friendly: "Hope you're having a lovely day!",
    standard: 'Are you well?',
    very_casual: 'All good?',
    casual_slang: 'You chill?'
  },
  'چه خبر': {
    natural: "What's up?",
    friendly: 'How have you been lately?',
    standard: 'What is the news?',
    very_casual: "What's new?",
    casual_slang: "What's the tea?"
  },
  'صبح بخیر': {
    natural: 'Good morning',
    friendly: 'Good morning! Hope you have a wonderful day ahead.',
    standard: 'Good morning.',
    very_casual: 'Morning!',
    casual_slang: 'Top of the morning!'
  },
  'شب بخیر': {
    natural: 'Good night',
    friendly: 'Good night, sleep tight and sweet dreams!',
    standard: 'Good night.',
    very_casual: 'Night night!',
    casual_slang: 'Catch you on the flip side!'
  },
  'خوشوقتم': {
    natural: 'Nice to meet you',
    friendly: 'So lovely to meet you!',
    standard: 'Pleased to meet you.',
    very_casual: 'Nice meeting you!',
    casual_slang: 'Great to connect with you!'
  },
  'ممنون': {
    natural: 'Thank you',
    friendly: 'Thank you so much, really appreciate your help!',
    standard: 'Thank you.',
    very_casual: 'Thanks!',
    casual_slang: 'Props! Much appreciated!'
  },
  'خیلی ممنون': {
    natural: 'Thanks a lot',
    friendly: 'Thank you so very much, that is so kind of you!',
    standard: 'Thank you very much.',
    very_casual: 'Thanks a million!',
    casual_slang: "Big thanks! You're a legend!"
  },
  'دستت درد نکنه': {
    natural: 'Thank you, appreciate your help',
    friendly: 'Bless you, thank you so much for the effort!',
    standard: 'Thank you for your assistance.',
    very_casual: 'Thanks a bunch!',
    casual_slang: "You're awesome, thanks!"
  },
  'دمت گرم': {
    natural: 'Awesome, thanks a lot!',
    friendly: "You're the best, thank you so much!",
    standard: 'Well done, thank you.',
    very_casual: 'Props to you!',
    casual_slang: 'You rock! Absolute champion!'
  },
  'خواهش میکنم': {
    natural: "You're welcome",
    friendly: "Don't mention it, happy to help anytime!",
    standard: 'You are welcome.',
    very_casual: 'No problem at all!',
    casual_slang: 'Anytime, no sweat!'
  },
  'ببخشید': {
    natural: 'Excuse me',
    friendly: 'Pardon me, please excuse me for a moment',
    standard: 'Excuse me / I apologize.',
    very_casual: 'Sorry about that!',
    casual_slang: 'My bad!'
  },
  'مشکلی نیست': {
    natural: 'No problem',
    friendly: "Don't worry about it at all!",
    standard: 'It is not a problem.',
    very_casual: 'No big deal!',
    casual_slang: 'No sweat! We are good!'
  },
  'دستشویی کجاست': {
    natural: 'Where is the restroom?',
    friendly: 'Excuse me, could you point me to the restroom please?',
    standard: 'Where is the restroom located?',
    very_casual: "Where's the bathroom?",
    casual_slang: "Where's the loo?"
  },
  'ایستگاه قطار کجاست': {
    natural: 'Where is the train station?',
    friendly: 'Excuse me, could you tell me where the train station is?',
    standard: 'Where is the railway station?',
    very_casual: "Where's the train station at?",
    casual_slang: "How do I hit the train station?"
  },
  'فرودگاه کجاست': {
    natural: 'Where is the airport?',
    friendly: 'Could you kindly help me with directions to the airport?',
    standard: 'Where is the airport located?',
    very_casual: "Where's the airport?",
    casual_slang: 'How do I get to the airport quick?'
  },
  'ساعت چنده': {
    natural: 'What time is it?',
    friendly: 'Excuse me, do you happen to have the time please?',
    standard: 'What time is it currently?',
    very_casual: 'Got the time?',
    casual_slang: 'What time we looking at?'
  },
  'قیمتش چنده': {
    natural: 'How much is this?',
    friendly: 'Could you tell me how much this costs, please?',
    standard: 'What is the price of this item?',
    very_casual: 'How much for this?',
    casual_slang: "What's the damage on this?"
  },
  'تخفیف میدید': {
    natural: 'Can you give a discount?',
    friendly: 'Could you possibly offer a small discount on this, please?',
    standard: 'Is a discount available?',
    very_casual: 'Can you cut me a deal?',
    casual_slang: 'Can you shave some off for me?'
  },
  'من گرسنه‌ام': {
    natural: "I'm hungry",
    friendly: "I'm feeling quite hungry, let's grab something nice to eat!",
    standard: 'I am hungry.',
    very_casual: "I'm starving!",
    casual_slang: "I'm famished, let's get some grub!"
  },
  'صورتحساب لطفا': {
    natural: 'Check, please',
    friendly: 'Could we have the bill when you have a moment, please?',
    standard: 'The bill, please.',
    very_casual: 'Can we get the check?',
    casual_slang: 'Bring the tab over, please!'
  },
  'آب لطفا': {
    natural: 'Water, please',
    friendly: 'Could I please have a glass of water?',
    standard: 'Water, please.',
    very_casual: 'Just some water!',
    casual_slang: 'Get me some water, please!'
  },
  'خوشمزه است': {
    natural: 'This is delicious!',
    friendly: 'This tastes wonderful, compliments to the chef!',
    standard: 'It is very delicious.',
    very_casual: 'This is so good!',
    casual_slang: 'This is fire! Hits the spot!'
  },
  'کمک کنید': {
    natural: 'Please help me',
    friendly: 'Excuse me, could someone please lend me a hand?',
    standard: 'Help is required, please.',
    very_casual: 'Help me out here!',
    casual_slang: 'Need a hand ASAP!'
  },
  'حالم خوب نیست': {
    natural: "I don't feel well",
    friendly: "I'm feeling a bit unwell today",
    standard: 'I do not feel well.',
    very_casual: "I'm feeling sick.",
    casual_slang: "I'm feeling under the weather."
  },
  'موافقم': {
    natural: 'I agree with you',
    friendly: 'I completely agree with you on that!',
    standard: 'I concur with your statement.',
    very_casual: 'Totally agree!',
    casual_slang: '100 percent with you on that!'
  },
  'مخالفم': {
    natural: 'I disagree',
    friendly: 'I respectfully see things a little differently',
    standard: 'I disagree with that.',
    very_casual: "Don't really agree.",
    casual_slang: 'Nah, not feeling that at all.'
  }
};

const EN_TO_FA_PHRASES: Record<string, StyleVariants> = {
  'hello': {
    natural: 'سلام',
    friendly: 'سلام و درود! روزتون خوش',
    standard: 'سلام، وقت بخیر',
    very_casual: 'سلام چطوری؟',
    casual_slang: 'سلام چه خبر؟'
  },
  'hi': {
    natural: 'سلام',
    friendly: 'سلام، امیدوارم روز فوق‌العاده‌ای داشته باشی!',
    standard: 'سلام',
    very_casual: 'سلام، خوبی؟',
    casual_slang: 'چطوری رفیق!'
  },
  'how are you': {
    natural: 'حالت چطوره؟',
    friendly: 'سلام! حالتون خوبه؟ روزتون چطور می‌گذره؟',
    standard: 'حال شما چطور است؟',
    very_casual: 'خوبی؟ روبه‌راهی؟',
    casual_slang: 'چه خبر مبر؟ ردیفی؟'
  },
  'how is it going': {
    natural: 'اوضاع چطوره؟',
    friendly: 'سلام! اوضاع بر وفق مراد پیش میره؟',
    standard: 'اوضاع چگونه است؟',
    very_casual: 'همه چی مرتبه؟',
    casual_slang: 'چه خبرا؟ رو به راهی؟'
  },
  "what's up": {
    natural: 'چه خبر؟',
    friendly: 'سلام دوست من! چه خبر از احوالاتت؟',
    standard: 'چه خبر است؟',
    very_casual: 'چه خبر؟ اوضاع چطوره؟',
    casual_slang: 'چه خبر مبر؟'
  },
  'good morning': {
    natural: 'صبح بخیر',
    friendly: 'صبح قشنگتون بخیر و شادی!',
    standard: 'صبح بخیر',
    very_casual: 'صبح عالی بخیر!',
    casual_slang: 'صبح بخیر داداش!'
  },
  'good night': {
    natural: 'شب بخیر',
    friendly: 'شب بخیر، خواب‌های آرام و خوب ببینی!',
    standard: 'شب بخیر',
    very_casual: 'شب خوش!',
    casual_slang: 'شب شیک!'
  },
  'nice to meet you': {
    natural: 'از آشنایی با شما خوشحالم',
    friendly: 'خیلی از دیدن و آشنایی با شما خوشبختم!',
    standard: 'از آشنایی با شما مسرورم',
    very_casual: 'خوشبختم از آشناییت!',
    casual_slang: 'خوشوقتم رفیق!'
  },
  'thank you': {
    natural: 'ممنون',
    friendly: 'خیلی ممنون از محبت و همراهی شما!',
    standard: 'متشکرم',
    very_casual: 'دستت درد نکنه!',
    casual_slang: 'دمت گرم داداش!'
  },
  'thank you very much': {
    natural: 'خیلی ممنون',
    friendly: 'یه دنیا از لطف و زحمتت سپاسگزارم!',
    standard: 'بسیار سپاسگزارم',
    very_casual: 'خیلی لطف کردی، مرسی!',
    casual_slang: 'دمت گرم واقعا، کارت درسته!'
  },
  "you're welcome": {
    natural: 'خواهش می‌کنم',
    friendly: 'خواهش می‌کنم، انجام وظیفه بود!',
    standard: 'قابلی ندارد',
    very_casual: 'کاری نکردم!',
    casual_slang: 'فدات، وظیفه بود!'
  },
  'excuse me': {
    natural: 'ببخشید',
    friendly: 'با اجازه و عذرخواهی از وقت شما',
    standard: 'پوزش می‌طلبم',
    very_casual: 'ببخشید یه لحظه!',
    casual_slang: 'شرمنده داداش!'
  },
  "i'm sorry": {
    natural: 'متاسفم / ببخشید',
    friendly: 'واقعا عذرخواهی می‌کنم، شرمنده‌ام',
    standard: 'پوزش می‌طلبم',
    very_casual: 'ببخشید واقعا!',
    casual_slang: 'شرمنده، دست خودم نبود!'
  },
  'no problem': {
    natural: 'مشکلی نیست',
    friendly: 'اصلا نگران نباشید، مشکلی پیش نیومده!',
    standard: 'مسئله‌ای نیست',
    very_casual: 'مشکلی نیست اصلا!',
    casual_slang: 'اصلا فدا سرت!'
  },
  'where is the restroom': {
    natural: 'دستشویی کجاست؟',
    friendly: 'عذر می‌خوام، سرویس بهداشتی در کدام بخش قرار دارد؟',
    standard: 'سرویس بهداشتی در کجا قرار دارد؟',
    very_casual: 'دستشویی کدوم سمته؟',
    casual_slang: 'دستشویی کجاست داداش؟'
  },
  'where is the train station': {
    natural: 'ایستگاه قطار کجاست؟',
    friendly: 'ممکنه راهنمایی بفرمایید ایستگاه قطار کجاست؟',
    standard: 'ایستگاه راه‌آهن در کجاست؟',
    very_casual: 'ایستگاه قطار کدوم طرفه؟',
    casual_slang: 'مترو یا قطار از کدوم وره؟'
  },
  'where is the airport': {
    natural: 'فرودگاه کجاست؟',
    friendly: 'ببخشید، مسیر فرودگاه از کدام سمت است؟',
    standard: 'فرودگاه در کدام جهت است؟',
    very_casual: 'فرودگاه کجاست؟',
    casual_slang: 'فرودگاه از کدوم طرفه؟'
  },
  'how much does this cost': {
    natural: 'قیمت این چنده؟',
    friendly: 'ببخشید، لطف می‌کنید بفرمایید بهای این چقدر است؟',
    standard: 'بهای این کالا چقدر است؟',
    very_casual: 'این چند درمیاد؟',
    casual_slang: 'این چند آب می‌خوره؟'
  },
  'can i get a discount': {
    natural: 'میشه تخفیف بدید؟',
    friendly: 'امکانش هست یه تخفیف کوچیک برای ما لحاظ کنید؟',
    standard: 'آیا امکان تخفیف وجود دارد؟',
    very_casual: 'تخفیف نمیدی؟',
    casual_slang: 'هوای ما رو داشته باش دیگه!'
  },
  'what time is it': {
    natural: 'ساعت چنده؟',
    friendly: 'ببخشید، ساعت چند هست؟',
    standard: 'ساعت چند است؟',
    very_casual: 'ساعت چنده الان؟',
    casual_slang: 'ساعت چند رو نشون میده؟'
  },
  "i'm hungry": {
    natural: 'من گرسنه‌ام',
    friendly: 'خیلی گرسنه شدم، بریم یه چیزی با هم میل کنیم؟',
    standard: 'من گرسنه هستم',
    very_casual: 'بدجور گشنمه!',
    casual_slang: 'دارم از گشنگی تلف میشم!'
  },
  'the bill please': {
    natural: 'صورتحساب لطفا',
    friendly: 'لطف می‌کنید صورتحساب را مرحمت بفرمایید؟',
    standard: 'صورت‌حساب را مرحمت بفرمایید',
    very_casual: 'حساب رو میارید بی زحمت؟',
    casual_slang: 'حساب کتاب رو بیار داداش!'
  },
  'water please': {
    natural: 'آب لطفا',
    friendly: 'ممکنه لطف بفرمایید یه لیوان آب بیارید؟',
    standard: 'آب آشامیدنی، لطفا',
    very_casual: 'یه لیوان آب لطفا!',
    casual_slang: 'یه آب بده دمت گرم!'
  },
  'this is delicious': {
    natural: 'خیلی خوشمزه است',
    friendly: 'طعمش واقعا فوق‌العاده و دلچسبه!',
    standard: 'بسیار لذیذ است',
    very_casual: 'خیلی خوشمزه‌ست!',
    casual_slang: 'عجب طعمی داره، محشره!'
  },
  'please help me': {
    natural: 'لطفا کمکم کنید',
    friendly: 'خواهش می‌کنم اگر امکان داره بهم کمک کنید',
    standard: 'لطفا به من یاری برسانید',
    very_casual: 'یه کمکی به من برسونید!',
    casual_slang: 'به دادم برس داداش!'
  },
  'i need a doctor': {
    natural: 'به پزشک نیاز دارم',
    friendly: 'عذر می‌خوام، من حالم خوب نیست و نیاز به پزشک دارم',
    standard: 'من به پزشک نیاز دارم',
    very_casual: 'یه دکتر لازم دارم سریع!',
    casual_slang: 'برسونید منو پیش دکتر!'
  },
  "i don't feel well": {
    natural: 'حالم خوب نیست',
    friendly: 'امروز یه مقدار احساس کسالت و ناخوشی دارم',
    standard: 'حال مساعدی ندارم',
    very_casual: 'اصلا روبه‌راه نیستم',
    casual_slang: 'حالم بده داداش'
  },
  'i agree': {
    natural: 'موافقم',
    friendly: 'کاملا با نظر شما هم‌عقیده‌ام',
    standard: 'موافقت خود را اعلام می‌کنم',
    very_casual: 'کاملا موافقم باهات!',
    casual_slang: 'دقیقا همینه، حرف منم هست!'
  },
  'i disagree': {
    natural: 'مخالفم',
    friendly: 'با احترام، نظر من یه مقدار متفاوته',
    standard: 'مخالف هستم',
    very_casual: 'نه، موافق نیستم',
    casual_slang: 'نه بابا، قبول ندارم!'
  }
};

const BILINGUAL_WORD_FA_TO_EN: Record<string, string> = {
  'من': 'I', 'تو': 'you', 'شما': 'you', 'ما': 'we', 'آنها': 'they',
  'این': 'this', 'آن': 'that', 'بله': 'yes', 'خیر': 'no', 'نه': 'no',
  'خوب': 'good', 'بد': 'bad', 'زیبا': 'beautiful', 'سریع': 'fast',
  'کار': 'work', 'خانه': 'home', 'اتاق': 'room', 'هتل': 'hotel',
  'امروز': 'today', 'فردا': 'tomorrow', 'دیروز': 'yesterday',
  'دوست': 'friend', 'پول': 'money', 'زمان': 'time', 'غذا': 'food',
  'شهر': 'city', 'خیابان': 'street', 'ماشین': 'car', 'کتاب': 'book'
};

const BILINGUAL_WORD_EN_TO_FA: Record<string, string> = {
  'i': 'من', 'you': 'شما', 'we': 'ما', 'they': 'آن‌ها',
  'this': 'این', 'that': 'آن', 'yes': 'بله', 'no': 'خیر',
  'good': 'خوب', 'bad': 'بد', 'beautiful': 'زیبا', 'fast': 'سریع',
  'work': 'کار', 'home': 'خانه', 'room': 'اتاق', 'hotel': 'هتل',
  'today': 'امروز', 'tomorrow': 'فردا', 'yesterday': 'دیروز',
  'friend': 'دوست', 'money': 'پول', 'time': 'زمان', 'food': 'غذا',
  'city': 'شهر', 'street': 'خیابان', 'car': 'ماشین', 'book': 'کتاب'
};

function normalizeInput(input: string): string {
  return input
    .replace(/ي/g, 'ی')
    .replace(/ك/g, 'ک')
    .replace(/ة/g, 'ه')
    .replace(/میخوام/g, 'می‌خواهم')
    .replace(/میدونم/g, 'می‌دانم')
    .replace(/نمیدونم/g, 'نمی‌دانم')
    .replace(/مرسی/g, 'ممنون')
    .replace(/[!?.،,;]+$/g, '')
    .trim();
}

function findBestPhrase(query: string, database: Record<string, StyleVariants>): StyleVariants | null {
  if (database[query]) return database[query];
  for (const [key, variants] of Object.entries(database)) {
    if (query.includes(key) || key.includes(query)) {
      return variants;
    }
  }
  return null;
}

export async function translateText(
  text: string,
  from: Language,
  to: Language,
  style: TranslationStyleId = 'natural'
): Promise<TranslationResult> {
  const trimmed = text.trim();
  if (!trimmed) {
    return {
      originalText: text,
      translatedText: '',
      sourceLanguage: from,
      targetLanguage: to,
      style,
      isOffline: true,
      confidence: 1,
      timestamp: Date.now()
    };
  }

  // 1. Try Optional Online Server Translation if reachable
  try {
    const res = await fetch('/api/translate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text: trimmed, from, to, style })
    });
    if (res.ok) {
      const data = await res.json();
      if (data.translatedText) {
        return {
          originalText: text,
          translatedText: data.translatedText,
          sourceLanguage: from,
          targetLanguage: to,
          style,
          isOffline: false,
          confidence: 0.98,
          timestamp: Date.now()
        };
      }
    }
  } catch (_e) {
    // Offline or server not responding; seamless fallback to offline engine
  }

  // 2. Offline-First Translation Engine
  const cleaned = normalizeInput(trimmed);

  if (from === 'fa' && to === 'en') {
    const match = findBestPhrase(cleaned, FA_TO_EN_PHRASES);
    if (match) {
      return {
        originalText: text,
        translatedText: match[style] || match.natural,
        sourceLanguage: from,
        targetLanguage: to,
        style,
        isOffline: true,
        confidence: 0.95,
        timestamp: Date.now()
      };
    }

    // Word-level tokenization fallback
    const tokens = cleaned.split(/\s+/).map((tok) => BILINGUAL_WORD_FA_TO_EN[tok] || tok);
    let synthesized = tokens.join(' ');
    if (style === 'friendly') synthesized += ', hope that helps!';
    if (style === 'casual_slang') synthesized += ' (you know what I mean?)';

    return {
      originalText: text,
      translatedText: synthesized,
      sourceLanguage: from,
      targetLanguage: to,
      style,
      isOffline: true,
      confidence: 0.8,
      timestamp: Date.now()
    };
  } else {
    const match = findBestPhrase(cleaned.toLowerCase(), EN_TO_FA_PHRASES);
    if (match) {
      return {
        originalText: text,
        translatedText: match[style] || match.natural,
        sourceLanguage: from,
        targetLanguage: to,
        style,
        isOffline: true,
        confidence: 0.95,
        timestamp: Date.now()
      };
    }

    const tokens = cleaned.toLowerCase().split(/\s+/).map((tok) => {
      const cleanTok = tok.replace(/[^a-z0-9]/g, '');
      return BILINGUAL_WORD_EN_TO_FA[cleanTok] || tok;
    });
    let synthesized = tokens.join(' ');
    if (style === 'friendly') synthesized += ' (با احترام و صمیمیت)';
    if (style === 'casual_slang') synthesized += '، حله داداش؟';

    return {
      originalText: text,
      translatedText: synthesized,
      sourceLanguage: from,
      targetLanguage: to,
      style,
      isOffline: true,
      confidence: 0.8,
      timestamp: Date.now()
    };
  }
}
