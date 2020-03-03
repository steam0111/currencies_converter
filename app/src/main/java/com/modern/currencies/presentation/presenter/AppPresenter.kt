package com.modern.currencies.presentation.presenter

import android.util.Log
import com.modern.currencies.BuildConfig
import com.modern.currencies.R
import com.modern.currencies.entity.currency.Rate
import com.modern.currencies.extension.removeFirst
import com.modern.currencies.model.interactor.CurrenciesInteractor
import com.modern.currencies.presentation.view.AppView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject
import java.util.concurrent.TimeUnit

/**
 * Presenter to manage @[AppView]
 *
 */

@InjectViewState
class AppPresenter : MvpPresenter<AppView>(), KoinComponent {

    private val currenciesInteractor: CurrenciesInteractor by inject()
    private var disposableGettingCurrencies: Disposable? = null

    private val repeatRequestTime: Long = 1
    private val retryRequestTimeIfError: Long = 3

    private var rates = arrayListOf<Rate>()

    private var baseRate: Rate
        //abstraction for a better understanding what's base rate
        get() = rates.first()
        set(value) {
            if (rates.isEmpty()) {
                rates.add(0, value)
            } else {
                rates[0] = value
            }
        }

    private var isWorkOfflineShow: Boolean = false

    override fun onFirstViewAttach() {
        baseRate = get()
        disposableGettingCurrencies = getCurrencies(baseRate, rates)
    }

    override fun onDestroy() {
        disposableGettingCurrencies?.dispose()
    }

    //Events
    fun onRateSelected(rate: Rate) {
        if (rate.name == baseRate.name) return
        disposableGettingCurrencies?.dispose()

        baseRate.isEditable = false
        setBaseRate(rates, rate)

        viewState.onShowRateList(
            currenciesInteractor.calculateCurrenciesValues(
                baseRate,
                rates
            )
        )

        disposableGettingCurrencies = getCurrencies(baseRate, rates)
    }

    fun onRateValueChanged(rate: Rate) {
        if (rate.name == baseRate.name) {
            baseRate.value = rate.value

            rates = ArrayList(currenciesInteractor.calculateCurrenciesValues(baseRate, rates))
            viewState.onShowRateList(rates)
        }
    }

    fun onBtnRetryClick() {
        viewState.onShowStubSmtWentWrong(false)
        disposableGettingCurrencies = getCurrencies(baseRate, rates)
    }

    /**
     * set new base rate
     *
     * @param rates all rates
     * @param rate new base rate
     * @return new base rate
     */
    private fun setBaseRate(rates: ArrayList<Rate>, rate: Rate): Rate {
        rates.removeFirst { it.name == rate.name }
        rates.add(0, rate.copy(isEditable = true))
        return rate
    }

    /**
     * get currencies in interval @[repeatRequestTime] and
     * retry request if error @[retryRequestTimeIfError]
     *
     * @param baseRate - other currencies return to the unit of this parameter
     * @param rates - prev list of rates
     * @return @[Disposable] for cancel requesting
     */
    private fun getCurrencies(
        baseRate: Rate,
        rates: List<Rate>
    ): Disposable? {
        return currenciesInteractor
            .getCurrencies(baseRate, rates)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if (this.rates.size == 1) {
                    viewState.onShowProgress(true)
                }
            }
            .doAfterSuccess {
                if (this.rates.size == 1) {
                    viewState.onShowProgress(false)
                }
            }
            .doOnError {
                when {
                    this.rates.size > 1 && !isWorkOfflineShow -> {
                        viewState.onShowMessage(R.string.work_offline)
                        isWorkOfflineShow = true
                    }

                    this.rates.size == 1 -> {
                        disposableGettingCurrencies?.dispose()
                        viewState.onShowProgress(false)
                        viewState.onShowStubSmtWentWrong(true)
                    }
                }
            }
            .observeOn(Schedulers.io())
            .retryWhen {
                return@retryWhen it.delay(retryRequestTimeIfError, TimeUnit.SECONDS)
            }
            .repeatWhen { handler ->
                return@repeatWhen handler.delay(repeatRequestTime, TimeUnit.SECONDS)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rateList ->
                this.rates = rateList
                viewState.onShowRateList(rateList)
                if (isWorkOfflineShow) {
                    viewState.onShowMessage(R.string.back_to_online)
                    isWorkOfflineShow = false
                }
            }, {
                if (BuildConfig.DEBUG) Log.d(AppPresenter::class.java.simpleName, it.toString())
            })
    }
}