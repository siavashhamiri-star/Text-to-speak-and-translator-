import { PracticeScenario } from '../types';

export const PRACTICE_SCENARIOS: PracticeScenario[] = [
  {
    id: 'everyday_01',
    categoryId: 'everyday',
    categoryTitleEn: 'Everyday English',
    categoryTitleFa: 'مکالمه روزمره',
    iconSymbol: '☀️',
    titleEn: 'Morning Greeting & Daily Routine',
    titleFa: 'احوال‌پرسی صبحگاهی و برنامه‌های روز',
    descriptionEn: 'Practice exchanging greetings, asking how someone slept, and discussing daily plans.',
    descriptionFa: 'تمرین احوال‌پرسی پرکاربرد صبحگاهی، پرسش از حال طرف مقابل و برنامه‌های کاری روزانه.',
    turns: [
      {
        id: 'ev_1',
        promptEn: 'Good morning! How are you doing today?',
        promptFa: 'صبح بخیر! امروز حالت چطوره؟',
        expectedAnswersEn: [
          "Good morning! I'm doing great, thank you.",
          "Morning! I'm good, how about you?",
          "I'm doing well, thanks for asking."
        ],
        explanationFa: "در پاسخ به 'How are you doing today'، پاسخ‌های طبیعی مانند 'I'm doing great, thank you' یا 'Morning! I'm good, how about you?' بهترین انتخاب‌ها هستند.",
        vocabularyNotes: [
          { termEn: 'How are you doing?', meaningFa: 'حالت چطوره؟', pronunciationEn: '/haʊ ɑːr juː ˈduːɪŋ/', exampleEn: 'Hey Sarah, how are you doing?' },
          { termEn: 'Thanks for asking', meaningFa: 'ممنون که حالمو پرسیدی', pronunciationEn: '/θæŋks fɔːr ˈæskɪŋ/', exampleEn: "I'm doing fine, thanks for asking." }
        ]
      },
      {
        id: 'ev_2',
        promptEn: 'Did you sleep well last night?',
        promptFa: 'دیشب خوب خوابیدی؟',
        expectedAnswersEn: [
          'Yes, I slept like a baby, thanks!',
          'Pretty well, thank you.',
          'Not really, I stayed up late working.'
        ],
        explanationFa: "اصطلاح 'sleep like a baby' یعنی خواب خیلی عمیق و راحت داشتن.",
        vocabularyNotes: [
          { termEn: 'Sleep like a baby', meaningFa: 'خواب عمیق و راحت', pronunciationEn: '/sliːp laɪk ə ˈbeɪbi/', exampleEn: 'I was so tired I slept like a baby.' },
          { termEn: 'Stay up late', meaningFa: 'تا دیروقت بیدار ماندن', pronunciationEn: '/steɪ ʌp leɪt/', exampleEn: "Don't stay up late tonight." }
        ]
      }
    ]
  },
  {
    id: 'casual_01',
    categoryId: 'casual',
    categoryTitleEn: 'Casual English',
    categoryTitleFa: 'انگلیسی دوستانه',
    iconSymbol: '☕',
    titleEn: 'Weekend Plans & Catching Up',
    titleFa: 'برنامه آخر هفته و گپ دوستانه',
    descriptionEn: "Friendly conversation about what you're up to this weekend.",
    descriptionFa: 'مکالمه خودمانی با دوستان در مورد برنامه‌های فراغت و تعطیلات آخر هفته.',
    turns: [
      {
        id: 'cas_1',
        promptEn: 'Hey! Any plans for this weekend?',
        promptFa: 'سلام! برای این آخر هفته برنامه‌ای داری؟',
        expectedAnswersEn: [
          'Not much, just going to relax at home.',
          'I might hang out with some friends on Saturday.',
          "I'm thinking of checking out a new cafe in town."
        ],
        explanationFa: "فعل عبارتی 'hang out' یعنی وقت گذراندن دوستانه، و 'check out' یعنی رفتن و دیدن جای جدید.",
        vocabularyNotes: [
          { termEn: 'Hang out', meaningFa: 'وقت گذراندن با دوستان', pronunciationEn: '/hæŋ aʊt/', exampleEn: "Let's hang out this Friday." },
          { termEn: 'Check out', meaningFa: 'دیدن یا امتحان کردن چیزی', pronunciationEn: '/tʃɛk aʊt/', exampleEn: 'We should check out that new coffee spot.' }
        ]
      }
    ]
  },
  {
    id: 'slang_01',
    categoryId: 'casual_slang',
    categoryTitleEn: 'Casual / Slang English',
    categoryTitleFa: 'اصطلاحات و اسلنگ',
    iconSymbol: '⚡',
    titleEn: 'Everyday Idioms & Slang in Context',
    titleFa: 'اصطلاحات عامیانه و اسلنگ‌های روز',
    descriptionEn: "Master natural phrases like 'no worries', 'hits the spot', and 'chill out'.",
    descriptionFa: 'یادگیری اسلنگ‌ها و اصطلاحات زنده خیابانی که در فیلم‌ها و گفتگوهای جوانان استفاده می‌شود.',
    turns: [
      {
        id: 'sl_1',
        promptEn: "Yo! Thanks a million for helping me move that couch, you're a lifesaver!",
        promptFa: 'دمت گرم واقعا بابت کمک برای جابجایی مبل، نجاتم دادی!',
        expectedAnswersEn: [
          'No worries at all, anytime!',
          "Don't sweat it, glad I could help!",
          "All good bro, you'd do the same for me."
        ],
        explanationFa: "'Don't sweat it' یعنی 'اصلا نگران نباش / فدای سرت'، و 'No worries' یک جایگزین کاملا طبیعی برای 'You're welcome' است.",
        vocabularyNotes: [
          { termEn: "Don't sweat it", meaningFa: 'اصلا نگران نباش، فدا سرت', pronunciationEn: '/doʊnt swɛt ɪt/', exampleEn: "Don't sweat it, it was no trouble." },
          { termEn: "You're a lifesaver", meaningFa: 'فرشته نجات من شدی', pronunciationEn: '/jʊr ə ˈlaɪfˌseɪvər/', exampleEn: "Thanks for the ride, you're a lifesaver!" }
        ]
      },
      {
        id: 'sl_2',
        promptEn: 'This pizza hits the spot! Are you ready to chill for a bit?',
        promptFa: 'این پیتزا بدجور چسبید! حاضری یه کم استراحت و ریلکس کنیم؟',
        expectedAnswersEn: [
          "Totally! I'm completely down to chill.",
          'Yeah, that food was amazing, let us relax.',
          "For sure, I'm ready to kick back."
        ],
        explanationFa: "اصطلاح 'hits the spot' یعنی دقیقا همون چیزی بود که می‌چسبید. 'I'm down' یعنی 'کاملا پایه و موافقم'.",
        vocabularyNotes: [
          { termEn: 'Hits the spot', meaningFa: 'بدجور چسبید / دقیقا به موقع بود', pronunciationEn: '/hɪts ðə spɑːt/', exampleEn: 'An iced coffee really hits the spot right now.' },
          { termEn: "I'm down", meaningFa: 'پایه‌ام / موافقم', pronunciationEn: '/aɪm daʊn/', exampleEn: "Are you coming tonight? Yeah, I'm down!" },
          { termEn: 'Kick back', meaningFa: 'ریلکس کردن و لم دادن', pronunciationEn: '/kɪk bæk/', exampleEn: 'I just want to kick back and watch a movie.' }
        ]
      }
    ]
  },
  {
    id: 'interview_01',
    categoryId: 'job_interview',
    categoryTitleEn: 'Job Interview',
    categoryTitleFa: 'مصاحبه کاری',
    iconSymbol: '💼',
    titleEn: 'Tell Me About Yourself',
    titleFa: 'معرفی حرفه‌ای خود در مصاحبه',
    descriptionEn: 'Structured practice for the most decisive interview question.',
    descriptionFa: 'تمرین دقیق پاسخ به سوال طلایی اول مصاحبه: از خودت بگو و تجربیاتت رو خلاصه کن.',
    turns: [
      {
        id: 'int_1',
        promptEn: 'Welcome! Could you tell me a little about yourself and your background?',
        promptFa: 'خوش آمدید! ممکن است کمی درباره خودتان و پیشینه شغلی‌تان توضیح دهید؟',
        expectedAnswersEn: [
          'Certainly! I have extensive experience in problem solving and teamwork.',
          "Sure! I'm a dedicated professional with a strong background in software development.",
          'Thank you for having me. I specialize in building user-friendly solutions.'
        ],
        explanationFa: 'با لحن بااعتماد‌به‌نفس و ساختار گذشته-حال-آینده معرفی کنید.',
        vocabularyNotes: [
          { termEn: 'To start off', meaningFa: 'برای شروع', pronunciationEn: '/tuː stɑːrt ɔːf/', exampleEn: 'To start off, thank you for the opportunity.' },
          { termEn: 'Strong background in', meaningFa: 'پیشینه و سابقه قوی در', pronunciationEn: '/strɔːŋ ˈbækˌɡraʊnd/', exampleEn: 'I have a strong background in communications.' }
        ]
      }
    ]
  },
  {
    id: 'travel_01',
    categoryId: 'travel',
    categoryTitleEn: 'Travel & Airport',
    categoryTitleFa: 'سفر و فرودگاه',
    iconSymbol: '✈️',
    titleEn: 'Airport & Passport Control',
    titleFa: 'فرودگاه و کنترل پاسپورت',
    descriptionEn: 'Essential travel phrases for immigration officer inquiries.',
    descriptionFa: 'پرسش و پاسخ‌های اصلی مامور مهاجرت، مقصد سفر و مدت اقامت.',
    turns: [
      {
        id: 'tr_1',
        promptEn: 'Good afternoon. What is the purpose of your visit today?',
        promptFa: 'عصر بخیر. هدف از سفر امروز شما چیست؟',
        expectedAnswersEn: [
          'I am here for tourism and vacation.',
          "I'm visiting for business and attending a conference.",
          "I'm here on holiday for two weeks."
        ],
        explanationFa: "پاسخ کوتاه و صریح: 'I'm here for tourism' (گردشگری) یا 'I'm visiting on business' (کاری).",
        vocabularyNotes: [
          { termEn: 'Purpose of visit', meaningFa: 'هدف از سفر', pronunciationEn: '/ˈpɜːrpəs ʌv ˈvɪzɪt/', exampleEn: 'Please state the purpose of your visit.' },
          { termEn: 'On vacation', meaningFa: 'در سفر تفریحی', pronunciationEn: '/ɑːn vəˈkeɪʃən/', exampleEn: 'We are currently on vacation.' }
        ]
      }
    ]
  },
  {
    id: 'restaurant_01',
    categoryId: 'restaurant',
    categoryTitleEn: 'Restaurant & Cafe',
    categoryTitleFa: 'رستوران و کافه',
    iconSymbol: '🍽️',
    titleEn: 'Ordering at a Cafe',
    titleFa: 'سفارش در کافی‌شاپ و رستوران',
    descriptionEn: 'Ordering coffee, specifying milk/sweetness, and asking for the bill.',
    descriptionFa: 'سفارش قهوه، درخواست تغییر در سفارش و گرفتن صورت‌حساب.',
    turns: [
      {
        id: 'res_1',
        promptEn: 'Hi there! What can I get started for you today?',
        promptFa: 'سلام! امروز چی براتون بیارم؟',
        expectedAnswersEn: [
          'Could I please get an oat milk latte to go?',
          "I'd like an iced Americano and a croissant, please.",
          'Can I get a cappuccino, for here please?'
        ],
        explanationFa: "برای بیرون بردن بگویید 'to go'. برای نوشیدن داخل کافه بگویید 'for here'.",
        vocabularyNotes: [
          { termEn: 'To go', meaningFa: 'بیرون‌بر', pronunciationEn: '/tuː ɡoʊ/', exampleEn: 'Two coffees to go, please.' },
          { termEn: 'For here', meaningFa: 'برای میل کردن در سالن', pronunciationEn: '/fɔːr hɪr/', exampleEn: 'Is that for here or to go?' }
        ]
      }
    ]
  },
  {
    id: 'shopping_01',
    categoryId: 'shopping',
    categoryTitleEn: 'Shopping & Retail',
    categoryTitleFa: 'خرید و فروشگاه',
    iconSymbol: '🛍️',
    titleEn: 'Clothing Store & Sizes',
    titleFa: 'خرید لباس و سایزبندی',
    descriptionEn: 'Asking to try on clothes, requesting another size, and checking prices.',
    descriptionFa: 'درخواست پرو، پرسیدن سایز دیگر و سوال در مورد قیمت نهایی.',
    turns: [
      {
        id: 'shp_1',
        promptEn: 'Can I help you find anything specific today?',
        promptFa: 'آیا می‌توانم کمکتان کنم چیز خاصی پیدا کنید؟',
        expectedAnswersEn: [
          'Yes please, do you have this shirt in a medium?',
          "I'm just browsing, thank you!",
          'Could I try this jacket on, please?'
        ],
        explanationFa: "اگر فقط قصد تماشا دارید بگویید 'I'm just browsing, thank you'.",
        vocabularyNotes: [
          { termEn: 'Just browsing', meaningFa: 'فقط تماشا کردن بدون قصد خرید قطعی', pronunciationEn: '/dʒʌst ˈbraʊzɪŋ/', exampleEn: "No thank you, I'm just browsing." },
          { termEn: 'Try on', meaningFa: 'پرو کردن لباس', pronunciationEn: '/traɪ ɑːn/', exampleEn: 'Where can I try this on?' }
        ]
      }
    ]
  },
  {
    id: 'social_01',
    categoryId: 'social',
    categoryTitleEn: 'Social Conversation',
    categoryTitleFa: 'روابط اجتماعی و مهمانی',
    iconSymbol: '🎉',
    titleEn: 'Meeting at a Social Gathering',
    titleFa: 'آشنایی در مهمانی و جمع دوستانه',
    descriptionEn: 'Starting conversations with polite small talk and common interests.',
    descriptionFa: 'شروع گفتگو با افراد جدید، صحبت درباره علایق و آشنایی در جمع.',
    turns: [
      {
        id: 'soc_1',
        promptEn: 'Hi! Nice to meet you. How do you know the host?',
        promptFa: 'سلام! از آشنایی خوشحالم. میزبان را از کجا می‌شناسید؟',
        expectedAnswersEn: [
          'We went to university together years ago!',
          'We work together at the same company.',
          "We're neighbors from down the street."
        ],
        explanationFa: "عبارت 'went to university together' یا 'work together' شروع‌کننده مکالمه‌ای طبیعی است.",
        vocabularyNotes: [
          { termEn: 'The host', meaningFa: 'میزبان', pronunciationEn: '/ðə hoʊst/', exampleEn: 'Have you thanked the host yet?' },
          { termEn: 'Down the street', meaningFa: 'پایین‌تر در همین خیابان', pronunciationEn: '/daʊn ðə striːt/', exampleEn: 'They live right down the street.' }
        ]
      }
    ]
  },
  {
    id: 'free_01',
    categoryId: 'free_practice',
    categoryTitleEn: 'Free Practice',
    categoryTitleFa: 'تمرین آزاد آفلاین',
    iconSymbol: '🎯',
    titleEn: 'Free Offline Conversation Topics',
    titleFa: 'موضوعات آزاد مکالمه آفلاین',
    descriptionEn: 'Speak freely on diverse real-world topics with instant offline evaluation.',
    descriptionFa: 'تمرین آزاد و بدون محدودیت روی سناریوهای متنوع با بازخورد آنی.',
    turns: [
      {
        id: 'fr_1',
        promptEn: 'Tell me: what is your favorite hobby and why do you enjoy it?',
        promptFa: 'بگو ببینم: تفریح یا سرگرمی مورد علاقه‌ات چیه و چرا ازش لذت می‌بری؟',
        expectedAnswersEn: [
          'My favorite hobby is reading books because it expands my mind.',
          'I love playing football with friends on weekends because it keeps me active.',
          'I enjoy listening to music and playing guitar to unwind after work.'
        ],
        explanationFa: "از افعالی مثل 'I love...', 'I enjoy...' همراه با 'because' استفاده کنید.",
        vocabularyNotes: [
          { termEn: 'To unwind', meaningFa: 'ریلکس کردن بعد از خستگی', pronunciationEn: '/tuː ʌnˈwaɪnd/', exampleEn: 'I listen to music to unwind.' },
          { termEn: 'Expands my mind', meaningFa: 'دیدگاهم را گسترش می‌دهد', pronunciationEn: '/ɪkˈspændz maɪ maɪnd/', exampleEn: 'Reading truly expands my mind.' }
        ]
      }
    ]
  }
];

function levenshtein(a: string, b: string): number {
  const an = a ? a.length : 0;
  const bn = b ? b.length : 0;
  if (an === 0) return bn;
  if (bn === 0) return an;
  const matrix = Array.from({ length: bn + 1 }, () => new Array<number>(an + 1));
  for (let i = 0; i <= bn; ++i) matrix[i][0] = i;
  for (let i = 0; i <= an; ++i) matrix[0][i] = i;
  for (let i = 1; i <= bn; ++i) {
    for (let j = 1; j <= an; ++j) {
      if (b.charAt(i - 1) === a.charAt(j - 1)) {
        matrix[i][j] = matrix[i - 1][j - 1];
      } else {
        matrix[i][j] = Math.min(matrix[i - 1][j - 1] + 1, matrix[i][j - 1] + 1, matrix[i - 1][j] + 1);
      }
    }
  }
  return matrix[bn][an];
}

export function evaluateSpeechPractice(userInput: string, expectedList: string[]) {
  const cleanUser = userInput.trim().toLowerCase();
  if (!cleanUser) {
    return {
      scorePercent: 0,
      feedbackEn: 'No input detected. Try speaking or typing your answer.',
      feedbackFa: 'پاسخی دریافت نشد. لطفا صحبت کنید یا پاسخ را تایپ کنید.',
      isAccurate: false,
      bestMatchingExpected: expectedList[0] || '',
      matchedKeywords: []
    };
  }

  let maxSim = 0;
  let best = expectedList[0] || '';

  for (const exp of expectedList) {
    const cleanExp = exp.toLowerCase();
    const dist = levenshtein(cleanUser, cleanExp);
    const maxL = Math.max(cleanUser.length, cleanExp.length) || 1;
    const sim = 1 - dist / maxL;
    if (sim > maxSim) {
      maxSim = sim;
      best = exp;
    }
  }

  const userWords = new Set(cleanUser.split(/\s+/).map((w) => w.replace(/[^a-z0-9]/g, '')));
  const expWords = new Set(best.toLowerCase().split(/\s+/).map((w) => w.replace(/[^a-z0-9]/g, '')));
  const matched = Array.from(userWords).filter((w) => expWords.has(w));
  const keywordRatio = expWords.size > 0 ? matched.length / expWords.size : 0;

  const scorePercent = Math.min(100, Math.max(15, Math.round((maxSim * 0.5 + keywordRatio * 0.5) * 100)));

  let feedbackEn = 'Nice attempt! Review the native phrase suggestions below.';
  let feedbackFa = 'تلاش خوبی بود! جملات پیشنهادی را مطالعه کنید و مجدداً تلاش کنید.';
  if (scorePercent >= 80) {
    feedbackEn = 'Excellent! Natural phrasing and clear communication.';
    feedbackFa = 'عالی بود! جمله‌بندی بسیار روان و دقیقی به کار بردید.';
  } else if (scorePercent >= 65) {
    feedbackEn = 'Good job! Clear meaning. Compare with native suggestions to refine.';
    feedbackFa = 'خیلی خوب! منظورتان کاملا قابل فهم بود. برای تسلط بیشتر الگوهای دیگر را هم ببینید.';
  }

  return {
    scorePercent,
    feedbackEn,
    feedbackFa,
    isAccurate: scorePercent >= 65,
    bestMatchingExpected: best,
    matchedKeywords: matched
  };
}
