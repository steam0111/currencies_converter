package com.modern.currencies.model.repository

import com.modern.currencies.model.data.CurrenciesApi

/**
 * Data source for Currencies
 *
 */
class CurrenciesRepository(private val currenciesApi: CurrenciesApi) {

    /**
     * get info with currencies rates from network request
     *
     * @param base - other currencies return to the unit of this parameter
     */
    fun getCurrencies(base: String) = currenciesApi.getCurrencies(base)

}