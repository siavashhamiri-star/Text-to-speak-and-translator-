package com.sidebyside.translator.model

import java.util.Locale

/**
 * Supported languages in V1.
 * Architected with ISO-639 codes and RTL flags so future language pairs can be added seamlessly.
 */
enum class Language(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val isRtl: Boolean,
    val locale: Locale
) {
    PERSIAN(
        code = "fa",
        displayName = "Persian",
        nativeName = "فارسی",
        isRtl = true,
        locale = Locale("fa", "IR")
    ),
    ENGLISH(
        code = "en",
        displayName = "English",
        nativeName = "English",
        isRtl = false,
        locale = Locale.US
    );

    val opposite: Language
        get() = if (this == PERSIAN) ENGLISH else PERSIAN

    companion object {
        fun fromCode(code: String): Language {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}
