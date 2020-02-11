package com.modern.currencies.entity.dto

import com.squareup.moshi.Json

data class CurrenciesDTO(
    @field:Json(name = "base")
    val base: String?,
    @field:Json(name = "date")
    val date: String?,
    @field:Json(name = "rates")
    val rates: Map<String, Float>
)