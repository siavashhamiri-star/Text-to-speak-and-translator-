package com.sidebyside.translator.practice

import com.sidebyside.translator.model.PracticeScenario
import com.sidebyside.translator.model.PracticeTurn
import com.sidebyside.translator.model.ScenarioCategory
import com.sidebyside.translator.model.VocabularyItem
import kotlin.math.max

data class EvaluationResult(
    val scorePercent: Int,
    val feedbackEn: String,
    val feedbackFa: String,
    val isAccurate: Boolean,
    val bestMatchingExpected: String,
    val matchedKeywords: List<String>
)

class PracticeEngine {

    val scenarios: List<PracticeScenario> = listOf(
        // 1. Everyday English
        PracticeScenario(
            id = "sc_everyday_01",
            category = ScenarioCategory.EVERYDAY,
            titleEn = "Morning Greeting & Daily Routine",
            titleFa = "احوال‌پرسی صبحگاهی و برنامه‌های روز",
            descriptionEn = "Practice exchanging greetings, asking how someone slept, and discussing daily plans.",
            descriptionFa = "تمرین احوال‌پرسی پرکاربرد صبحگاهی، پرسش از حال طرف مقابل و برنامه‌های کاری روزانه.",
            turns = listOf(
                PracticeTurn(
                    id = "turn_everyday_1",
                    promptEn = "Good morning! How are you doing today?",
                    promptFa = "صبح بخیر! امروز حالت چطوره؟",
                    expectedAnswersEn = listOf(
                        "Good morning! I'm doing great, thank you.",
                        "Morning! I'm good, how about you?",
                        "I'm doing well, thanks for asking."
                    ),
                    explanationFa = "در پاسخ به 'How are you doing today'، پاسخ‌های طبیعی مانند 'I'm doing great, thank you' یا 'Morning! I'm good, how about you?' بهترین انتخاب‌ها هستند.",
                    vocabularyNotes = listOf(
                        VocabularyItem("How are you doing?", "حالت چطوره؟", "/haʊ ɑːr juː ˈduːɪŋ/", "Hey Sarah, how are you doing?"),
                        VocabularyItem("Thanks for asking", "ممنون که حالمو پرسیدی", "/θæŋks fɔːr ˈæskɪŋ/", "I'm doing fine, thanks for asking.")
                    )
                ),
                PracticeTurn(
                    id = "turn_everyday_2",
                    promptEn = "Did you sleep well last night?",
                    promptFa = "دیشب خوب خوابیدی؟",
                    expectedAnswersEn = listOf(
                        "Yes, I slept like a baby, thanks!",
                        "Pretty well, thank you.",
                        "Not really, I stayed up late working."
                    ),
                    explanationFa = "اصطلاح 'sleep like a baby' یعنی خواب خیلی عمیق و راحت داشتن.",
                    vocabularyNotes = listOf(
                        VocabularyItem("Sleep like a baby", "خواب عمیق و راحت", "/sliːp laɪk ə ˈbeɪbi/", "I was so tired I slept like a baby."),
                        VocabularyItem("Stay up late", "تا دیروقت بیدار ماندن", "/steɪ ʌp leɪt/", "Don't stay up late tonight.")
                    )
                )
            )
        ),

        // 2. Casual English
        PracticeScenario(
            id = "sc_casual_01",
            category = ScenarioCategory.CASUAL,
            titleEn = "Weekend Plans & Catching Up",
            titleFa = "برنامه آخر هفته و گپ دوستانه",
            descriptionEn = "Friendly conversation about what you're up to this weekend.",
            descriptionFa = "مکالمه خودمانی با دوستان در مورد برنامه‌های فراغت و تعطیلات آخر هفته.",
            turns = listOf(
                PracticeTurn(
                    id = "turn_casual_1",
                    promptEn = "Hey! Any plans for this weekend?",
                    promptFa = "سلام! برای این آخر هفته برنامه‌ای داری؟",
                    expectedAnswersEn = listOf(
                        "Not much, just going to relax at home.",
                        "I might hang out with some friends on Saturday.",
                        "I'm thinking of checking out a new cafe in town."
                    ),
                    explanationFa = "فعل عبارتی 'hang out' یعنی وقت گذراندن دوستانه، و 'check out' یعنی رفتن و دیدن جای جدید.",
                    vocabularyNotes = listOf(
                        VocabularyItem("Hang out", "وقت گذراندن با دوستان", "/hæŋ aʊt/", "Let's hang out this Friday."),
                        VocabularyItem("Check out", "دیدن یا امتحان کردن چیزی", "/tʃɛk aʊt/", "We should check out that new coffee spot.")
                    )
                )
            )
        ),

        // 3. Casual / Slang English
        PracticeScenario(
            id = "sc_slang_01",
            category = ScenarioCategory.CASUAL_SLANG,
            titleEn = "Everyday Idioms & Slang in Context",
            titleFa = "اصطلاحات عامیانه و اسلنگ‌های روز",
            descriptionEn = "Master natural phrases like 'no worries', 'hits the spot', and 'chill out'.",
            descriptionFa = "یادگیری اسلنگ‌ها و اصطلاحات زنده خیابانی که در فیلم‌ها و گفتگوهای جوانان استفاده می‌شود.",
            turns = listOf(
                PracticeTurn(
                    id = "turn_slang_1",
                    promptEn = "Yo! Thanks a million for helping me move that couch, you're a lifesaver!",
                    promptFa = "دمت گرم واقعا بابت کمک برای جابجایی مبل، نجاتم دادی!",
                    expectedAnswersEn = listOf(
                        "No worries at all, anytime!",
                        "Don't sweat it, glad I could help!",
                        "All good bro, you'd do the same for me."
                    ),
                    explanationFa = "'Don't sweat it' یعنی 'اصلا نگران نباش / فدای سرت'، و 'No worries' یک جایگزین کاملا طبیعی برای 'You're welcome' است.",
                    vocabularyNotes = listOf(
                        VocabularyItem("Don't sweat it", "اصلا نگران نباش، فدا سرت", "/doʊnt swɛt ɪt/", "Don't sweat it, it wasn't a problem."),
                        VocabularyItem("You're a lifesaver", "فرشته نجات من شدی", "/jʊr ə ˈlaɪfˌseɪvər/", "Thanks for the ride, you're a lifesaver!")
                    )
                ),
                PracticeTurn(
                    id = "turn_slang_2",
                    promptEn = "This pizza hits the spot! Are you ready to chill for a bit?",
                    promptFa = "این پیتزا بدجور چسبید! حاضری یه کم استراحت و ریلکس کنیم؟",
                    expectedAnswersEn = listOf(
                        "Totally! I'm completely down to chill.",
                        "Yeah, that food was amazing, let's relax.",
                        "For sure, I'm ready to kick back."
                    ),
                    explanationFa = "اصطلاح 'hits the spot' یعنی دقیقا همون چیزی بود که می‌چسبید. 'I'm down' یعنی 'کاملا پایه و موافقم'.",
                    vocabularyNotes = listOf(
                        VocabularyItem("Hits the spot", "بدجور چسبید / دقیقا به موقع بود", "/hɪts ðə spɑːt/", "An iced coffee really hits the spot right now."),
                        VocabularyItem("I'm down", "پایه‌ام / موافقم", "/aɪm daʊn/", "Are you coming tonight? Yeah, I'm down!"),
                        VocabularyItem("Kick back", "ریلکس کردن و لم دادن", "/kɪk bæk/", "I just want to kick back and watch a movie.")
                    )
                )
            )
        ),

        // 4. Job Interview
        PracticeScenario(
            id = "sc_interview_01",
            category = ScenarioCategory.JOB_INTERVIEW,
            titleEn = "Professional Interview: Tell Me About Yourself",
            titleFa = "مصاحبه شغلی: معرفی حرفه‌ای خود",
            descriptionEn = "Structured practice for the most common and decisive interview question.",
            descriptionFa = "تمرین دقیق پاسخ به سوال طلایی اول مصاحبه: از خودت بگو و تجربیاتت رو خلاصه کن.",
            turns = listOf(
                PracticeTurn(
                    id = "turn_interview_1",
                    promptEn = "Welcome! To start off, could you tell me a little about yourself and your background?",
                    promptFa = "خوش آمدید! برای شروع، ممکن است کمی درباره خودتان و پیشینه شغلی‌تان توضیح دهید؟",
                    expectedAnswersEn = listOf(
                        "Certainly! I have over five years of experience in software development and communication.",
                        "Sure! I'm a dedicated professional with a strong background in problem solving and teamwork.",
                        "Thank you for having me. I specialize in building user-friendly solutions and collaborating with teams."
                    ),
                    explanationFa = "با لحن بااعتماد‌به‌نفس و ساختار زمانی گذشته-حال-آینده (تجربه قبلی، مهارت کنونی، انگیزه برای این موقعیت) پاسخ دهید.",
                    vocabularyNotes = listOf(
                        VocabularyItem("To start off", "برای شروع", "/tuː stɑːrt ɔːf/", "To start off, let me thank you for the opportunity."),
                        VocabularyItem("Strong background in", "پیشینه و سابقه قوی در", "/strɔːŋ ˈbækˌɡraʊnd/", "I have a strong background in customer relations.")
                    )
                )
            )
        ),

        // 5. Travel & Airport
        PracticeScenario(
            id = "sc_travel_01",
            category = ScenarioCategory.TRAVEL,
            titleEn = "Airport & Passport Control",
            titleFa = "فرودگاه و کنترل پاسپورت",
            descriptionEn = "Essential travel phrases for immigration officer inquiries and boarding gates.",
            descriptionFa = "پرسش و پاسخ‌های اصلی مامور مهاجرت، مقصد سفر و مدت اقامت.",
            turns = listOf(
                PracticeTurn(
                    id = "turn_travel_1",
                    promptEn = "Good afternoon. What is the purpose of your visit today?",
                    promptFa = "عصر بخیر. هدف از سفر امروز شما چیست؟",
                    expectedAnswersEn = listOf(
                        "I am here for tourism and vacation.",
                        "I'm visiting for business and attending a conference.",
                        "I'm here on holiday for two weeks."
                    ),
                    explanationFa = "پاسخ کوتاه و صریح: 'I'm here for tourism' (گردشگری) یا 'I'm visiting on business' (کاری).",
                    vocabularyNotes = listOf(
                        VocabularyItem("Purpose of visit", "هدف از سفر", "/ˈpɜːrpəs ʌv ˈvɪzɪt/", "Please state the purpose of your visit."),
                        VocabularyItem("On holiday / On vacation", "در سفر تفریحی", "/ɑːn ˈhɑːləˌdeɪ/", "We are currently on vacation.")
                    )
                )
            )
        ),

        // 6. Restaurant & Cafe
        PracticeScenario(
            id = "sc_restaurant_01",
            category = ScenarioCategory.RESTAURANT,
            titleEn = "Ordering at a Cafe",
            titleFa = "سفارش در کافی‌شاپ و رستوران",
            descriptionEn = "Ordering coffee, specifying milk/sweetness, and asking for the bill.",
            descriptionFa = "سفارش قهوه، درخواست تغییر در سفارش و گرفتن صورت‌حساب.",
            turns = listOf(
                PracticeTurn(
                    id = "turn_restaurant_1",
                    promptEn = "Hi there! What can I get started for you today?",
                    promptFa = "سلام! امروز چی براتون بیارم؟",
                    expectedAnswersEn = listOf(
                        "Could I please get an oat milk latte to go?",
                        "I'd like an iced Americano and a croissant, please.",
                        "Can I get a cappuccino, for here please?"
                    ),
                    explanationFa = "برای بیرون بردن بگویید 'to go' یا 'takeaway'. برای نوشیدن داخل کافه بگویید 'for here'.",
                    vocabularyNotes = listOf(
                        VocabularyItem("To go / Takeaway", "بیرون‌بر", "/tuː ɡoʊ/", "Two coffees to go, please."),
                        VocabularyItem("For here", "برای میل کردن در سالن", "/fɔːr hɪr/", "Is this for here or to go?")
                    )
                )
            )
        ),

        // 7. Shopping & Retail
        PracticeScenario(
            id = "sc_shopping_01",
            category = ScenarioCategory.SHOPPING,
            titleEn = "Clothing Store & Sizes",
            titleFa = "خرید لباس و سایزبندی",
            descriptionEn = "Asking to try on clothes, requesting another size, and checking prices.",
            descriptionFa = "درخواست پرو، پرسیدن سایز دیگر و سوال در مورد قیمت نهایی.",
            turns = listOf(
                PracticeTurn(
                    id = "turn_shopping_1",
                    promptEn = "Can I help you find anything specific today?",
                    promptFa = "آیا می‌توانم کمکتان کنم چیز خاصی پیدا کنید؟",
                    expectedAnswersEn = listOf(
                        "Yes please, do you have this shirt in a medium?",
                        "I'm just browsing, thank you!",
                        "Could I try this jacket on, please?"
                    ),
                    explanationFa = "اگر فقط قصد تماشا دارید بگویید 'I'm just browsing, thank you' (فقط دارم نگاه می‌کنم).",
                    vocabularyNotes = listOf(
                        VocabularyItem("Just browsing", "فقط تماشا کردن بدون قصد خرید قطعی", "/dʒʌst ˈbraʊzɪŋ/", "No thank you, I'm just browsing."),
                        VocabularyItem("Try on", "پرو کردن لباس", "/traɪ ɑːn/", "Where can I try this on?")
                    )
                )
            )
        ),

        // 8. Social Conversation
        PracticeScenario(
            id = "sc_social_01",
            category = ScenarioCategory.SOCIAL,
            titleEn = "Meeting at a Social Gathering",
            titleFa = "آشنایی در مهمانی و جمع دوستانه",
            descriptionEn = "Starting conversations with polite small talk and common interests.",
            descriptionFa = "شروع گفتگو با افراد جدید، صحبت درباره علایق و آشنایی در جمع.",
            turns = listOf(
                PracticeTurn(
                    id = "turn_social_1",
                    promptEn = "Hi! Nice to meet you. How do you know the host?",
                    promptFa = "سلام! از آشنایی خوشحالم. میزبان را از کجا می‌شناسید؟",
                    expectedAnswersEn = listOf(
                        "We went to university together years ago!",
                        "We work together at the same company.",
                        "We're neighbors from down the street."
                    ),
                    explanationFa = "عبارت 'went to university together' یا 'work together' شروع‌کننده مکالمه‌ای طبیعی است.",
                    vocabularyNotes = listOf(
                        VocabularyItem("The host", "میزبان", "/ðə hoʊst/", "Have you thanked the host yet?"),
                        VocabularyItem("Down the street", "پایین‌تر در همین خیابان", "/daʊn ðə striːt/", "They live right down the street.")
                    )
                )
            )
        ),

        // 9. Free Practice
        PracticeScenario(
            id = "sc_free_01",
            category = ScenarioCategory.FREE_PRACTICE,
            titleEn = "Free Offline Conversation Topics",
            titleFa = "موضوعات آزاد مکالمه آفلاین",
            descriptionEn = "Speak freely on diverse real-world topics with instant offline evaluation.",
            descriptionFa = "تمرین آزاد و بدون محدودیت روی سناریوهای متنوع با بازخورد آنی.",
            turns = listOf(
                PracticeTurn(
                    id = "turn_free_1",
                    promptEn = "Tell me: what is your favorite hobby and why do you enjoy it?",
                    promptFa = "بگو ببینم: تفریح یا سرگرمی مورد علاقه‌ات چیه و چرا ازش لذت می‌بری؟",
                    expectedAnswersEn = listOf(
                        "My favorite hobby is reading books because it expands my mind.",
                        "I love playing football with friends on weekends because it keeps me active.",
                        "I enjoy listening to music and playing guitar to unwind after work."
                    ),
                    explanationFa = "از افعالی مثل 'I love...', 'I enjoy...', یا 'My favorite hobby is...' همراه با 'because' برای دلیل آوردن استفاده کنید.",
                    vocabularyNotes = listOf(
                        VocabularyItem("To unwind", "ریلکس کردن بعد از خستگی", "/tuː ʌnˈwaɪnd/", "I listen to music to unwind."),
                        VocabularyItem("Expands my mind", "دیدگاهم را گسترش می‌دهد", "/ɪkˈspændz maɪ maɪnd/", "Traveling truly expands my mind.")
                    )
                )
            )
        )
    )

    fun evaluateAnswer(userInput: String, turn: PracticeTurn): EvaluationResult {
        val cleanUser = userInput.trim().lowercase()
        if (cleanUser.isBlank()) {
            return EvaluationResult(
                scorePercent = 0,
                feedbackEn = "No input detected. Try speaking or typing your answer.",
                feedbackFa = "پاسخی دریافت نشد. لطفا صحبت کنید یا پاسختان را بنویسید.",
                isAccurate = false,
                bestMatchingExpected = turn.expectedAnswersEn.first(),
                matchedKeywords = emptyList()
            )
        }

        var maxSimilarity = 0f
        var bestExpected = turn.expectedAnswersEn.first()

        for (expected in turn.expectedAnswersEn) {
            val sim = calculateSimilarity(cleanUser, expected.lowercase())
            if (sim > maxSimilarity) {
                maxSimilarity = sim
                bestExpected = expected
            }
        }

        val userTokens = cleanUser.split(" ").map { it.replace(Regex("[^a-z0-9]"), "") }.toSet()
        val expectedTokens = bestExpected.lowercase().split(" ").map { it.replace(Regex("[^a-z0-9]"), "") }.toSet()
        val matchedWords = userTokens.intersect(expectedTokens).toList()

        val keywordRatio = if (expectedTokens.isNotEmpty()) {
            matchedWords.size.toFloat() / expectedTokens.size.toFloat()
        } else 0f

        val finalScore = ((maxSimilarity * 0.5f + keywordRatio * 0.5f) * 100).toInt().coerceIn(15, 100)

        val (feedbackEn, feedbackFa) = when {
            finalScore >= 85 -> Pair(
                "Excellent! Natural phrasing and clear communication.",
                "عالی بود! جمله‌بندی روان و دقیقی به کار بردید."
            )
            finalScore >= 65 -> Pair(
                "Good job! Clear meaning. Compare with native suggestions to refine.",
                "خیلی خوب! منظورتان کاملا قابل فهم بود. پیشنهادهای دیگر را هم برای تسلط بیشتر بررسی کنید."
            )
            else -> Pair(
                "Nice attempt! Review the native phrase suggestions below.",
                "تلاش خوبی بود! جملات پیشنهادی بومی‌زبانان را مطالعه و یک بار دیگر تکرار کنید."
            )
        }

        return EvaluationResult(
            scorePercent = finalScore,
            feedbackEn = feedbackEn,
            feedbackFa = feedbackFa,
            isAccurate = finalScore >= 65,
            bestMatchingExpected = bestExpected,
            matchedKeywords = matchedWords
        )
    }

    private fun calculateSimilarity(s1: String, s2: String): Float {
        val distance = levenshteinDistance(s1, s2)
        val maxLen = max(s1.length, s2.length)
        if (maxLen == 0) return 1.0f
        return 1.0f - (distance.toFloat() / maxLen.toFloat())
    }

    private fun levenshteinDistance(lhs: CharSequence, rhs: CharSequence): Int {
        val lhsLength = lhs.length
        val rhsLength = rhs.length
        var cost = IntArray(lhsLength + 1) { it }
        var newCost = IntArray(lhsLength + 1) { 0 }

        for (i in 1..rhsLength) {
            newCost[0] = i
            for (j in 1..lhsLength) {
                val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1
                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1
                newCost[j] = minOf(costInsert, costDelete, costReplace)
            }
            val swap = cost
            cost = newCost
            newCost = swap
        }
        return cost[lhsLength]
    }
}
