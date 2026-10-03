package com.sidebyside.translator.model

enum class Speaker(
    val id: String,
    val defaultLanguage: Language,
    val labelEn: String,
    val labelFa: String,
    val badgeSymbol: String
) {
    PERSON_A(
        id = "person_a",
        defaultLanguage = Language.PERSIAN,
        labelEn = "Person A (Persian)",
        labelFa = "طرف اول (فارسی)",
        badgeSymbol = "🇮🇷"
    ),
    PERSON_B(
        id = "person_b",
        defaultLanguage = Language.ENGLISH,
        labelEn = "Person B (English)",
        labelFa = "طرف دوم (انگلیسی)",
        badgeSymbol = "🇬🇧"
    );

    val other: Speaker
        get() = if (this == PERSON_A) PERSON_B else PERSON_A
}
