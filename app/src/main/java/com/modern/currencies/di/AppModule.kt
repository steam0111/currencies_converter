package com.modern.currencies.di

import com.modern.currencies.entity.ui.Rate
import com.modern.currencies.model.interactor.CurrenciesInteractor
import com.modern.currencies.model.repository.CurrenciesRepository
import org.koin.dsl.module
import java.text.DecimalFormat

val appModule = module {

    //Currencies
    factory { CurrenciesInteractor(get(), get()) }
    factory { CurrenciesRepository(get()) }

    //Info for base currency
    factory {
        DecimalFormat("#.##")
    }

    factory {
        Rate("EUR", 1f, 1f, true, get())
    }
}