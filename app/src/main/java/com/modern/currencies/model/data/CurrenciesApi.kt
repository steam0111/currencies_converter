package com.modern.currencies.model.data

import com.modern.currencies.entity.dto.CurrenciesDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit2 interface to access Revolut Rates API
 *
 */
interface CurrenciesApi {

    @GET("/latest")
    fun getCurrencies(@Query("base") base: String): Single<CurrenciesDTO>
}