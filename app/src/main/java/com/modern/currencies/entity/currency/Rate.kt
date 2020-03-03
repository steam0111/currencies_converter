package com.modern.currencies.entity.currency

import java.text.DecimalFormat

data class Rate(
    val name: String,
    var value: Float = 0f,
    var rateFactor: Float, //ratio of base currency to current
    var isEditable: Boolean = false,
    val valueDecimalFormat: DecimalFormat
) {
    fun getTextRate(): String {
        return valueDecimalFormat.format(value)
    }
}