package com.modern.currencies.presentation.view

import androidx.annotation.StringRes
import com.modern.currencies.entity.ui.Rate
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface AppView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onShowRateList(rateList: List<Rate>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onShowMessage(@StringRes messageResId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onShowStubSmtWentWrong(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onShowProgress(show: Boolean)
}