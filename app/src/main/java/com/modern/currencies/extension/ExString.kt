package com.modern.currencies.extension

/**
 * check String if not empty before cast
 *
 * @return 0f if String empty else result of toFloat
 */
fun String.safeToFloat(): Float =
    if (this.isEmpty())
        0f
    else
        this
            .replace(',', '.')
            .toFloat()