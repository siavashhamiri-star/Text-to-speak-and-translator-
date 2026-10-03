package com.sidebyside.translator.model

enum class ConversationMode(
    val titleEn: String,
    val titleFa: String,
    val descriptionEn: String,
    val descriptionFa: String
) {
    SIDE_BY_SIDE(
        titleEn = "Side by Side",
        titleFa = "رو در رو",
        descriptionEn = "Split screen for two people facing each other",
        descriptionFa = "صفحه دوطرفه برای گفتگوی زنده دو نفره"
    ),
    CHAT(
        titleEn = "Chat",
        titleFa = "چت متنی و صوتی",
        descriptionEn = "Timeline history with audio replay and typing",
        descriptionFa = "تاریخچه پیام‌ها با پخش صوت و امکان تایپ"
    ),
    LIVE_VOICE(
        titleEn = "Live Voice",
        titleFa = "ترجمه صوتی زنده",
        descriptionEn = "Near-real-time voice to voice with audio waveforms",
        descriptionFa = "ترجمه پیوسته گفتار به گفتار با نمایش موج صدا"
    ),
    PRACTICE(
        titleEn = "Language Practice",
        titleFa = "تمرین مکالمه آفلاین",
        descriptionEn = "Offline English practice scenarios for Persian speakers",
        descriptionFa = "سناریوهای تعاملی تقویت انگلیسی به زبان فارسی"
    ),
    VIDEO(
        titleEn = "Video Call",
        titleFa = "تماس تصویری",
        descriptionEn = "Live camera preview with real-time translation overlay",
        descriptionFa = "ارتباط تصویری با زیرنویس زنده ترجمه دوطرفه"
    );
}
