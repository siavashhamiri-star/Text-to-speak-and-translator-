package com.sidebyside.translator.model

data class PracticeTurn(
    val id: String,
    val promptEn: String,
    val promptFa: String,
    val expectedAnswersEn: List<String>,
    val explanationFa: String,
    val vocabularyNotes: List<VocabularyItem> = emptyList(),
    val audioHint: String? = null
)

data class VocabularyItem(
    val termEn: String,
    val meaningFa: String,
    val pronunciationEn: String,
    val exampleEn: String
)

enum class ScenarioCategory(
    val id: String,
    val titleEn: String,
    val titleFa: String,
    val iconSymbol: String,
    val descriptionEn: String,
    val descriptionFa: String
) {
    EVERYDAY(
        id = "everyday",
        titleEn = "Everyday English",
        titleFa = "مکالمه روزمره",
        iconSymbol = "☀️",
        descriptionEn = "Greetings, weather, daily plans and common questions",
        descriptionFa = "احوال‌پرسی، آب‌وهوا، برنامه‌های روز و پرسش‌های رایج"
    ),
    CASUAL(
        id = "casual",
        titleEn = "Casual English",
        titleFa = "انگلیسی دوستانه",
        iconSymbol = "☕",
        descriptionEn = "Chatting with peers, hobbies, weekend plans",
        descriptionFa = "گفتگوی صمیمی با همکاران و دوستان، تفریحات و آخر هفته"
    ),
    CASUAL_SLANG(
        id = "casual_slang",
        titleEn = "Casual / Slang English",
        titleFa = "اصطلاحات و اسلنگ",
        iconSymbol = "⚡",
        descriptionEn = "Modern idioms, youth slang, phrasal verbs in context",
        descriptionFa = "اصطلاحات خیابانی، تکیه‌کلام‌های روز و عبارات پرکاربرد جوانان"
    ),
    JOB_INTERVIEW(
        id = "job_interview",
        titleEn = "Job Interview",
        titleFa = "مصاحبه کاری",
        iconSymbol = "💼",
        descriptionEn = "Professional self-introduction, strengths, handling questions",
        descriptionFa = "معرفی حرفه‌ای خود، نقاط قوت و پاسخ به سوالات پرتکرار مصاحبه"
    ),
    TRAVEL(
        id = "travel",
        titleEn = "Travel & Airport",
        titleFa = "سفر و فرودگاه",
        iconSymbol = "✈️",
        descriptionEn = "Passport control, finding gates, hotels, and asking for directions",
        descriptionFa = "کنترل پاسپورت، گیت پرواز، هتل و آدرس پرسیدن در شهر"
    ),
    RESTAURANT(
        id = "restaurant",
        titleEn = "Restaurant & Cafe",
        titleFa = "رستوران و کافه",
        iconSymbol = "🍽️",
        descriptionEn = "Ordering food, coffee options, asking for the bill, dietary needs",
        descriptionFa = "سفارش غذا، انتخاب قهوه، درخواست صورت‌حساب و توضیحات رژیمی"
    ),
    SHOPPING(
        id = "shopping",
        titleEn = "Shopping & Retail",
        titleFa = "خرید و فروشگاه",
        iconSymbol = "🛍️",
        descriptionEn = "Asking prices, trying on sizes, bargaining, returns",
        descriptionFa = "قیمت پرسیدن، پرو کردن لباس، تخفیف و پس دادن کالا"
    ),
    SOCIAL(
        id = "social",
        titleEn = "Social Conversation",
        titleFa = "روابط اجتماعی و مهمانی",
        iconSymbol = "🎉",
        descriptionEn = "Meeting new people, complimenting, polite small talk",
        descriptionFa = "آشنایی با افراد جدید، تعارفات محترمانه و گفتگو در مهمانی"
    ),
    FREE_PRACTICE(
        id = "free_practice",
        titleEn = "Free Practice",
        titleFa = "تمرین آزاد آفلاین",
        iconSymbol = "🎯",
        descriptionEn = "Open offline practice across various conversational prompts",
        descriptionFa = "تمرین آزاد روی انواع موضوعات کاربردی بدون نیاز به اینترنت"
    );

    companion object {
        fun fromId(id: String): ScenarioCategory {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: EVERYDAY
        }
    }
}

data class PracticeScenario(
    val id: String,
    val category: ScenarioCategory,
    val titleEn: String,
    val titleFa: String,
    val descriptionEn: String,
    val descriptionFa: String,
    val turns: List<PracticeTurn>
)
