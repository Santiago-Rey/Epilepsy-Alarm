package com.sr.configuration.util

import android.text.InputFilter
import android.text.Spanned

class LetterInputFilter  : InputFilter {
    private val regex = "[a-zA-Z]+".toRegex()

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val input = source?.toString() ?: ""
        return if (regex.matches(input)) {
            null // Permitir el texto ingresado
        } else {
            "" // Bloquear el texto ingresado
        }
    }
}