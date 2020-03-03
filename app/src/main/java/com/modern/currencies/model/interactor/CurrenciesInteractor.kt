package com.modern.currencies.model.interactor

import com.modern.currencies.entity.currency.Rate
import com.modern.currencies.model.repository.CurrenciesRepository
import java.text.DecimalFormat

/**
 * Business logic and mappers for currencies
 *
 */
class CurrenciesInteractor(
    private val currenciesRepository: CurrenciesRepository,
    private val decimalFormat: DecimalFormat
) {

    /**
     * get and map currencies from @[CurrenciesRepository]
     *
     * @param baseRate - other currencies return to the unit of this parameter
     * @param rates - prev list of rates
     */
    fun getCurrencies(baseRate: Rate, rates: List<Rate>) =
        currenciesRepository
            .getCurrencies(baseRate.name)
            .map { currenciesDTO ->
                rates[0].rateFactor = 1f //bcs currenciesDTO.rates doesn't contains base rate
                val completedRates = ArrayList<Rate>(rates)
                for ((name, rateFactor) in currenciesDTO.rates) {
                    val findIndex = completedRates.indexOfFirst { it.name == name }
                    if (findIndex == -1) {
                        completedRates.add(
                            Rate(
                                name,
                                value = baseRate.value * rateFactor,
                                rateFactor = rateFactor,
                                valueDecimalFormat = decimalFormat
                            )
                        )
                    } else {
                        completedRates[findIndex].apply {
                            this.rateFactor = rateFactor
                            this.value = baseRate.value * rateFactor
                        }
                    }
                }
                return@map completedRates
            }

    /**
     * recalculate list of rates by new baseRate
     *
     * @param baseRate new base rate
     * @param rates current list of rates
     * @return
     */
    fun calculateCurrenciesValues(baseRate: Rate, rates: List<Rate>): List<Rate> {
        return rates.map {
            it.value = it.rateFactor / baseRate.rateFactor * baseRate.value
            it
        }
    }
}