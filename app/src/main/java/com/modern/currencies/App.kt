package com.modern.currencies

import android.app.Application
import com.modern.currencies.di.appModule
import com.modern.currencies.di.networkModule
import org.koin.core.context.startKoin

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    /**
     * start Koin modules
     *
     */
    private fun initKoin() {
        startKoin {
            modules(
                listOf(
                    appModule, networkModule
                )
            )
        }
    }
}