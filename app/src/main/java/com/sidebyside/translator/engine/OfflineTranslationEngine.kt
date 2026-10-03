package com.sidebyside.translator.engine

import com.sidebyside.translator.model.Language
import com.sidebyside.translator.model.TranslationResult
import com.sidebyside.translator.model.TranslationStyle

/**
 * High-performance, offline-first translation engine for Persian ↔ English.
 * Operates without internet connection using pre-bundled phrase maps,
 * grammatical transforms, conversational patterns, and style adapters.
 */
class OfflineTranslationEngine : ITranslationEngine {

    override val supportedPairs: List<Pair<Language, Language>> = listOf(
        Pair(Language.PERSIAN, Language.ENGLISH),
        Pair(Language.ENGLISH, Language.PERSIAN)
    )

    private data class StyleVariants(
        val natural: String,
        val friendly: String,
        val standard: String,
        val veryCasual: String,
        val casualSlang: String
    ) {
        fun get(style: TranslationStyle): String = when (style) {
            TranslationStyle.NATURAL -> natural
            TranslationStyle.FRIENDLY -> friendly
            TranslationStyle.STANDARD -> standard
            TranslationStyle.VERY_CASUAL -> veryCasual
            TranslationStyle.CASUAL_SLANG -> casualSlang
        }
    }

    // Persian to English Phrase Database with 5 Styles
    private val faToEnPhrases = mapOf<String, StyleVariants>(
        // Greetings & Introductions
        "سلام" to StyleVariants("Hello", "Hi there, great to see you!", "Hello, greetings.", "Hey!", "What's up!"),
        "سلام چطوری" to StyleVariants("Hey, how are you doing?", "Hi! How are things with you?", "Hello, how are you?", "Hey, how's it going?", "Yo, what's good?"),
        "خوبی" to StyleVariants("You doing good?", "Hope you're having a wonderful day!", "Are you well?", "All good?", "You chill?"),
        "چه خبر" to StyleVariants("What's up?", "How have you been lately?", "What is new?", "What's new?", "What's the tea?"),
        "صبح بخیر" to StyleVariants("Good morning", "Good morning! Hope you slept well.", "Good morning.", "Morning!", "Top of the morning!"),
        "شب بخیر" to StyleVariants("Good night", "Good night, sweet dreams!", "Good night.", "Night night!", "Catch you on the flip side!"),
        "خوشوقتم" to StyleVariants("Nice to meet you", "So wonderful to meet you!", "Pleased to meet you.", "Nice meeting you!", "Great to connect with you!"),
        "اسم شما چیه" to StyleVariants("What's your name?", "May I ask what your name is?", "What is your name?", "What's your name again?", "Who am I talking to?"),
        "اسم من علی است" to StyleVariants("My name is Ali", "I'm Ali, nice to meet you!", "My name is Ali.", "I'm Ali.", "Call me Ali."),
        
        // Politeness, Thanks & Gratitude
        "ممنون" to StyleVariants("Thank you", "Thank you so much, really appreciate it!", "Thank you.", "Thanks!", "Props! Appreciate it!"),
        "خیلی ممنون" to StyleVariants("Thanks a lot", "Thank you so very much, that's so kind!", "Thank you very much.", "Thanks a ton!", "Big thanks! You're a legend!"),
        "متشکرم" to StyleVariants("Thank you", "I really appreciate your help!", "Thank you.", "Thanks a bunch!", "Cheers, mate!"),
        "خواهش میکنم" to StyleVariants("You're welcome", "Don't mention it, happy to help!", "You are welcome.", "No problem at all!", "Anytime, no worries!"),
        "دستت درد نکنه" to StyleVariants("Thank you, appreciate it", "Bless you, thank you so much!", "Thank you for your effort.", "Thanks, really appreciate it!", "You're awesome, thanks!"),
        "دمت گرم" to StyleVariants("Awesome, thanks a lot!", "You're the best, thank you!", "Well done, thank you.", "Props to you!", "You rock!"),
        "قربانت" to StyleVariants("Thanks, you're so kind", "Warm wishes to you, thank you!", "With sincere regards.", "Cheers!", "Much love!"),
        "ببخشید" to StyleVariants("Excuse me", "Pardon me, please", "Excuse me / I apologize.", "Sorry about that!", "My bad!"),
        "شرمنده" to StyleVariants("I'm so sorry", "I'm really sorry about this inconvenience", "I apologize.", "My mistake, sorry!", "My bad completely!"),
        "مشکلی نیست" to StyleVariants("No problem", "Don't worry about it at all!", "It is not a problem.", "No big deal!", "No sweat!"),
        
        // Understanding & Communication
        "من انگلیسی بلد نیستم" to StyleVariants("I don't speak English well", "I'm still learning English, please bear with me!", "I do not speak English.", "My English isn't great.", "English ain't my strong suit."),
        "میشه آروم‌تر صحبت کنید" to StyleVariants("Could you speak a bit slower?", "Would you mind speaking a little more slowly, please?", "Please speak more slowly.", "Could you slow down a notch?", "Slow down a bit, please!"),
        "متوجه نشدم" to StyleVariants("I didn't catch that", "Sorry, could you explain that one more time?", "I did not understand.", "Didn't get that.", "Went right over my head."),
        "میشه تکرار کنید" to StyleVariants("Can you repeat that?", "Could you please say that again for me?", "Please repeat that.", "Say that again?", "Run that by me again?"),
        "منظورتون چیه" to StyleVariants("What do you mean?", "Could you clarify what you mean, please?", "What do you mean?", "What are you getting at?", "What's the angle?"),
        "فهمیدم" to StyleVariants("I got it", "Ah, that makes total sense, thank you!", "I understand.", "Gotcha!", "Heard that loud and clear!"),

        // Questions & Requests
        "کجاست" to StyleVariants("Where is it?", "Could you kindly tell me where that is?", "Where is it located?", "Where's that at?", "Where to?"),
        "دستشویی کجاست" to StyleVariants("Where is the restroom?", "Excuse me, where might I find the restroom?", "Where is the toilet / restroom?", "Where's the bathroom?", "Where's the loo / can?"),
        "ایستگاه مترو کجاست" to StyleVariants("Where is the subway station?", "Excuse me, could you point me toward the subway station?", "Where is the subway station?", "Where's the train station?", "Where's the nearest metro stop?"),
        "فرودگاه کجاست" to StyleVariants("Where is the airport?", "Could you help me find directions to the airport?", "Where is the airport?", "Where's the airport at?", "How do I hit the airport?"),
        "ساعت چنده" to StyleVariants("What time is it?", "Do you happen to have the time, please?", "What time is it?", "Got the time?", "What time we looking at?"),
        "قیمتش چنده" to StyleVariants("How much is this?", "Could you let me know how much this costs, please?", "What is the price of this?", "How much for this?", "What's the damage on this?"),
        "گرونه" to StyleVariants("It's quite expensive", "That's a bit pricey for my budget", "It is expensive.", "That's pretty steep!", "That's way too costly!"),
        "تخفیف میدید" to StyleVariants("Can you give a discount?", "Is there any chance of a small discount on this?", "Could you offer a discount?", "Can you cut me a deal?", "Can you shave some off?"),
        
        // Dining & Food
        "من گرسنه‌ام" to StyleVariants("I'm hungry", "I'm feeling quite hungry!", "I am hungry.", "I'm starving!", "I'm famished, let's grab food!"),
        "آب لطفا" to StyleVariants("Water, please", "Could I please have a glass of water?", "Water, please.", "Just some water!", "Get me some water, please!"),
        "صورتحساب لطفا" to StyleVariants("Check, please", "May we have the bill when you have a moment, please?", "The bill, please.", "Can we get the check?", "Bring the tab over, please!"),
        "خوشمزه است" to StyleVariants("This is delicious!", "This tastes wonderful, compliments to the chef!", "It is very delicious.", "This is so tasty!", "This is fire! / Hits the spot!"),
        "قهوه میخوام" to StyleVariants("I'd like a coffee", "Could I please get a cup of coffee?", "I would like coffee.", "Need a coffee!", "Grab me a coffee, please!"),

        // Travel, Help & Emergency
        "کمک کنید" to StyleVariants("Please help me", "Excuse me, could somebody lend me a hand, please?", "Help, please.", "Help me out here!", "Need some help ASAP!"),
        "حالم خوب نیست" to StyleVariants("I don't feel well", "I'm feeling a bit unwell today", "I do not feel well.", "I'm feeling sick.", "I'm feeling under the weather."),
        "دکتر نیاز دارم" to StyleVariants("I need a doctor", "I really need to see a medical professional, please", "I need a physician.", "Need a doc right away!", "Get me a doctor, stat!"),
        "تاکسی لطفا" to StyleVariants("A taxi, please", "Could you help call a taxi for me, please?", "Please call a taxi.", "Call a cab!", "Hail a ride for me!"),
        "هتل کجاست" to StyleVariants("Where is the hotel?", "Could you guide me to the hotel, please?", "Where is the hotel?", "Where's the hotel located?", "How to get back to the hotel?"),

        // Daily Conversations & Opinions
        "هوا چطوره" to StyleVariants("How's the weather?", "How is the weather looking out there today?", "What is the weather like?", "What's it like outside?", "How's it look out there?"),
        "هوا سرده" to StyleVariants("It's cold today", "It's rather chilly outside today!", "The weather is cold.", "It's freezing!", "It's brick outside!"),
        "هوا گرمه" to StyleVariants("It's hot today", "It's quite warm and sunny today!", "The weather is hot.", "It's boiling!", "It's scorching outside!"),
        "موافقم" to StyleVariants("I agree with you", "I completely agree with you on that!", "I concur.", "Totally agree!", "100 percent with you on that!"),
        "مخالفم" to StyleVariants("I disagree", "I respectfully see things a bit differently", "I disagree.", "Don't really agree.", "Nah, not feeling that.")
    )

    // English to Persian Phrase Database with 5 Styles
    private val enToFaPhrases = mapOf<String, StyleVariants>(
        // Greetings
        "hello" to StyleVariants("سلام", "سلام و درود! روزتون خوش", "سلام، وقت بخیر", "سلام چطوری؟", "سلام چه خبر؟"),
        "hi" to StyleVariants("سلام", "سلام، امیدوارم عالی باشی!", "سلام", "سلام خوبی؟", "چطوری!"),
        "how are you" to StyleVariants("حالت چطوره؟", "سلام! حالتون خوبه؟ روزتون چطور می‌گذره؟", "حال شما چطور است؟", "خوبی؟ روبه‌راهی؟", "چه خبر مبر؟ ردیفی؟"),
        "how's it going" to StyleVariants("اوضاع چطوره؟", "سلام! اوضاع بر وفق مراده؟", "اوضاع چگونه است؟", "همه چی خوب پیش میره؟", "چه خبرا؟ رو به راهی؟"),
        "what's up" to StyleVariants("چه خبر؟", "سلام دوست من! چه خبر از احوالاتت؟", "چه خبر است؟", "چه خبر؟ همه چی اوکیه؟", "چه خبر مبر؟"),
        "good morning" to StyleVariants("صبح بخیر", "صبح قشنگتون بخیر و شادی!", "صبح بخیر", "صبح عالی بخیر!", "صبح بخیر رفیق!"),
        "good night" to StyleVariants("شب بخیر", "شب بخیر، خواب‌های آرام و خوب ببینی!", "شب بخیر", "شب خوش!", "شب شیک!"),
        "nice to meet you" to StyleVariants("از آشنایی با شما خوشحالم", "خیلی از دیدن و آشنایی با شما خوشبختم!", "از آشنایی با شما مسرورم", "خوشبختم از آشناییت!", "خوشوقتم رفیق!"),
        "what is your name" to StyleVariants("اسم شما چیه؟", "ببخشید، ممکنه اسمتون رو بدونم؟", "نام شما چیست؟", "اسمت چیه؟", "اسمت چیه داداش؟"),
        "my name is" to StyleVariants("اسم من ... است", "باعث افتخاره، اسم من ... هست", "نام من ... می‌باشد", "من ... هستم", "منم ..."),

        // Politeness & Thanks
        "thank you" to StyleVariants("ممنون", "خیلی ممنون از محبت و لطف شما!", "متشکرم", "دستت درد نکنه!", "دمت گرم!"),
        "thank you very much" to StyleVariants("خیلی ممنون", "یه دنیا از محبت و زحمتت ممنونم!", "بسیار سپاسگزارم", "خیلی لطف کردی، مرسی!", "دمت گرم واقعا، کارت درسته!"),
        "you're welcome" to StyleVariants("خواهش می‌کنم", "خواهش می‌کنم، انجام وظیفه بود!", "قابلی ندارد", "کاری نکردم!", "فدات، وظیفه بود!"),
        "excuse me" to StyleVariants("ببخشید", "با اجازه و عذرخواهی از وقت شما", "عفو بفرمایید", "ببخشید یه لحظه!", "شرمنده داداش!"),
        "i'm sorry" to StyleVariants("متاسفم / ببخشید", "واقعا عذرخواهی می‌کنم، شرمنده‌ام", "پوزش می‌طلبم", "ببخشید واقعا!", "شرمنده، دست خودم نبود!"),
        "no problem" to StyleVariants("مشکلی نیست", "اصلا نگران نباشید، مشکلی پیش نیومده!", "مسئله‌ای نیست", "مشکلی نیست اصلا!", "اصلا فدا سرت!"),

        // Questions & Directions
        "where is the bathroom" to StyleVariants("دستشویی کجاست؟", "عذر می‌خوام، سرویس بهداشتی کجاست؟", "سرویس بهداشتی در کجا قرار دارد؟", "دستشویی کدوم سمته؟", "دستشویی کجاست؟"),
        "where is the train station" to StyleVariants("ایستگاه قطار کجاست؟", "ممکنه راهنمایی کنید ایستگاه قطار کجاست؟", "ایستگاه راه‌آهن در کجاست؟", "ایستگاه قطار کدوم طرفه؟", "مترو یا قطار از کدوم وره؟"),
        "where is the airport" to StyleVariants("فرودگاه کجاست؟", "ببخشید، مسیر فرودگاه از کدوم طرفه؟", "فرودگاه در کدام جهت است؟", "فرودگاه کجاست؟", "فرودگاه از کدوم طرفه؟"),
        "how much does this cost" to StyleVariants("قیمت این چنده؟", "ببخشید، لطف می‌کنید بفرمایید این چنده؟", "بهای این کالا چقدر است؟", "این چند درمیاد؟", "این چند آب می‌خوره؟"),
        "can i get a discount" to StyleVariants("میشه تخفیف بدید؟", "امکانش هست یه تخفیف کوچیک برای ما لحاظ کنید؟", "آیا امکان تخفیف وجود دارد؟", "تخفیف نمیدی؟", "هوای ما رو داشته باش دیگه!"),
        "what time is it" to StyleVariants("ساعت چنده؟", "ببخشید، ساعت چند هست؟", "ساعت چند است؟", "ساعت چنده الان؟", "ساعت چند رو نشون میده؟"),

        // Dining
        "i'm hungry" to StyleVariants("من گرسنه‌ام", "خیلی گرسنه شدم، بریم یه چیزی میل کنیم؟", "من گرسنه هستم", "بدجور گشنمه!", "دارم از گشنگی تلف میشم!"),
        "the bill please" to StyleVariants("صورتحساب لطفا", "لطف می‌کنید صورتحساب رو بیارید؟", "صورت‌حساب را مرحمت بفرمایید", "حساب رو میارید بی زحمت؟", "حساب کتاب رو بیار داداش!"),
        "water please" to StyleVariants("آب لطفا", "ممکنه لطف کنید یه لیوان آب بیارید؟", "آب آشامیدنی، لطفا", "یه لیوان آب لطفا!", "یه آب بده دمت گرم!"),
        "this is delicious" to StyleVariants("خیلی خوشمزه است", "طعمش واقعا فوق‌العاده و دلچسبه!", "بسیار لذیذ است", "خیلی خوشمزه‌ست!", "عجب طعمی داره، محشره!"),

        // Help & Emergency
        "please help me" to StyleVariants("لطفا کمکم کنید", "خواهش می‌کنم اگر امکان داره بهم کمک کنید", "لطفا به من یاری برسانید", "یه کمکی به من برسونید!", "به دادم برس داداش!"),
        "i need a doctor" to StyleVariants("به پزشک نیاز دارم", "عذر می‌خوام، من حالم خوب نیست و نیاز به دکتر دارم", "من به پزشک نیاز دارم", "یه دکتر لازم دارم سریع!", "برسونید منو پیش دکتر!"),
        "i don't feel well" to StyleVariants("حالم خوب نیست", "امروز یه مقدار احساس کسالت و ناخوشی دارم", "حال مساعدی ندارم", "اصلا روبه‌راه نیستم", "حالم بده داداش"),
        
        // Conversational
        "i agree" to StyleVariants("موافقم", "کاملا با نظر شما هم‌عقیده‌ام", "موافقت خود را اعلام می‌کنم", "کاملا موافقم باهات!", "دقیقا همینه، حرف منم هست!"),
        "i disagree" to StyleVariants("مخالفم", "با احترام، نظر من یه مقدار متفاوته", "مخالف هستم", "نه، موافق نیستم", "نه بابا، قبول ندارم!")
    )

    // Bilingual Vocabulary Dictionary for Word-level fallback & grammar synthesis
    private val bilingualDictionaryFaToEn = mapOf(
        "من" to "I", "تو" to "you", "شما" to "you", "ما" to "we", "آنها" to "they",
        "این" to "this", "آن" to "that", "بله" to "yes", "خیر" to "no", "نه" to "no",
        "خوب" to "good", "بد" to "bad", "زیبا" to "beautiful", "سریع" to "fast",
        "کار" to "work", "خانه" to "home", "اتاق" to "room", "هتل" to "hotel",
        "امروز" to "today", "فردا" to "tomorrow", "دیروز" to "yesterday",
        "دوست" to "friend", "پول" to "money", "زمان" to "time", "غذا" to "food",
        "شهر" to "city", "خیابان" to "street", "ماشین" to "car", "کتاب" to "book"
    )

    private val bilingualDictionaryEnToFa = mapOf(
        "i" to "من", "you" to "شما", "we" to "ما", "they" to "آن‌ها",
        "this" to "این", "that" to "آن", "yes" to "بله", "no" to "خیر",
        "good" to "خوب", "bad" to "بد", "beautiful" to "زیبا", "fast" to "سریع",
        "work" to "کار", "home" to "خانه", "room" to "اتاق", "hotel" to "هتل",
        "today" to "امروز", "tomorrow" to "فردا", "yesterday" to "دیروز",
        "friend" to "دوست", "money" to "پول", "time" to "زمان", "food" to "غذا",
        "city" to "شهر", "street" to "خیابان", "car" to "ماشین", "book" to "کتاب"
    )

    override suspend fun translate(
        text: String,
        from: Language,
        to: Language,
        style: TranslationStyle
    ): TranslationResult {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) {
            return TranslationResult(
                originalText = text,
                translatedText = "",
                sourceLanguage = from,
                targetLanguage = to,
                style = style,
                isOffline = true
            )
        }

        val cleaned = normalizeInput(trimmed)

        // 1. Direct Phrase Match
        if (from == Language.PERSIAN && to == Language.ENGLISH) {
            val phraseMatch = findBestPhraseMatch(cleaned, faToEnPhrases)
            if (phraseMatch != null) {
                return TranslationResult(
                    originalText = text,
                    translatedText = phraseMatch.get(style),
                    sourceLanguage = from,
                    targetLanguage = to,
                    style = style,
                    isOffline = true,
                    confidence = 0.95f
                )
            }

            // Fallback word-level synthesis with natural phrasing
            val translatedTokens = cleaned.split(" ").map { token ->
                bilingualDictionaryFaToEn[token] ?: token
            }
            val synthesized = translatedTokens.joinToString(" ")
            return TranslationResult(
                originalText = text,
                translatedText = applyEnStylePostProcessing(synthesized, style),
                sourceLanguage = from,
                targetLanguage = to,
                style = style,
                isOffline = true,
                confidence = 0.80f
            )
        } else {
            val phraseMatch = findBestPhraseMatch(cleaned.lowercase(), enToFaPhrases)
            if (phraseMatch != null) {
                return TranslationResult(
                    originalText = text,
                    translatedText = phraseMatch.get(style),
                    sourceLanguage = from,
                    targetLanguage = to,
                    style = style,
                    isOffline = true,
                    confidence = 0.95f
                )
            }

            // Fallback word-level synthesis
            val translatedTokens = cleaned.lowercase().split(" ").map { token ->
                val cleanWord = token.replace(Regex("[^a-z0-9]"), "")
                bilingualDictionaryEnToFa[cleanWord] ?: token
            }
            val synthesized = translatedTokens.joinToString(" ")
            return TranslationResult(
                originalText = text,
                translatedText = applyFaStylePostProcessing(synthesized, style),
                sourceLanguage = from,
                targetLanguage = to,
                style = style,
                isOffline = true,
                confidence = 0.80f
            )
        }
    }

    private fun normalizeInput(input: String): String {
        return input
            .replace("ي", "ی")
            .replace("ك", "ک")
            .replace("ة", "ه")
            .replace("میخوام", "می‌خواهم")
            .replace("میدونم", "می‌دانم")
            .replace("نمیدونم", "نمی‌دانم")
            .replace("مرسی", "ممنون")
            .replace(Regex("[!?.،,;]+$"), "")
            .trim()
    }

    private fun findBestPhraseMatch(query: String, database: Map<String, StyleVariants>): StyleVariants? {
        val exact = database[query]
        if (exact != null) return exact

        // Substring / partial key matching
        for ((key, value) in database) {
            if (query.contains(key) || key.contains(query)) {
                return value
            }
        }
        return null
    }

    private fun applyEnStylePostProcessing(text: String, style: TranslationStyle): String {
        return when (style) {
            TranslationStyle.NATURAL -> text
            TranslationStyle.FRIENDLY -> "$text, hope that helps!"
            TranslationStyle.STANDARD -> text
            TranslationStyle.VERY_CASUAL -> text.replace("I would like", "I want").replace("do not", "don't")
            TranslationStyle.CASUAL_SLANG -> "$text (you know what I mean?)"
        }
    }

    private fun applyFaStylePostProcessing(text: String, style: TranslationStyle): String {
        return when (style) {
            TranslationStyle.NATURAL -> text
            TranslationStyle.FRIENDLY -> "$text (با احترام و محبت)"
            TranslationStyle.STANDARD -> text
            TranslationStyle.VERY_CASUAL -> text.replace("می‌خواهم", "میخوام").replace("نمی‌دانم", "نمیدونم")
            TranslationStyle.CASUAL_SLANG -> "$text، حله؟"
        }
    }
}
