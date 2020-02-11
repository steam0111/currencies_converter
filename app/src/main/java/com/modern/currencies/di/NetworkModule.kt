package com.modern.currencies.di

import com.modern.currencies.BuildConfig
import com.modern.currencies.model.data.CurrenciesApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single { provideCurrenciesApi(BuildConfig.BASE_URL) }
}

/**
 * Provide @[CurrenciesApi]
 */
fun provideCurrenciesApi(baseUrl: String): CurrenciesApi = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(MoshiConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build().create(CurrenciesApi::class.java)