package com.sidebyside.translator.model

/**
 * Translation styles required by the product specification:
 * - Natural (Default)
 * - Friendly
 * - Standard
 * - Very Casual
 * - Casual / Slang
 */
enum class TranslationStyle(
    val id: String,
    val titleEn: String,
    val titleFa: String,
    val descriptionEn: String,
    val descriptionFa: String
) {
    NATURAL(
        id = "natural",
        titleEn = "Natural",
        titleFa = "طبیعی",
        descriptionEn = "Natural conversational flow used in daily life (Default)",
        descriptionFa = "روان و طبیعی مثل مکالمه روزمره (پیش‌فرض)"
    ),
    FRIENDLY(
        id = "friendly",
        titleEn = "Friendly",
        titleFa = "دوستانه",
        descriptionEn = "Warm, polite, approachable tone",
        descriptionFa = "صمیمی، مودبانه و گرم"
    ),
    STANDARD(
        id = "standard",
        titleEn = "Standard",
        titleFa = "استاندارد",
        descriptionEn = "Grammatically clean and clear",
        descriptionFa = "رسمی‌تر و منطبق بر قواعد دستوری"
    ),
    VERY_CASUAL(
        id = "very_casual",
        titleEn = "Very Casual",
        titleFa = "خیلی عامیانه",
        descriptionEn = "Informal colloquial speech used between close peers",
        descriptionFa = "گفتاری خودمانی و شکسته"
    ),
    CASUAL_SLANG(
        id = "casual_slang",
        titleEn = "Casual / Slang",
        titleFa = "اصطلاحات و عامیانه",
        descriptionEn = "Everyday idioms, slang expressions, and colloquial terms",
        descriptionFa = "اصطلاحات روزمره و تکیه‌کلام‌های مدرن"
    );

    companion object {
        fun fromId(id: String): TranslationStyle {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: NATURAL
        }
    }
}
