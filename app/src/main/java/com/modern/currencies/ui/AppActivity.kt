package com.modern.currencies.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.modern.currencies.R
import com.modern.currencies.entity.ui.Rate
import com.modern.currencies.presentation.presenter.AppPresenter
import com.modern.currencies.presentation.view.AppView
import com.modern.currencies.ui.list.adapter.CurrenciesAdapter
import com.modern.currencies.ui.list.decorator.addMarginVerticalItemDecorator
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class AppActivity : MvpAppCompatActivity(), AppView {

    private val presenter by moxyPresenter { AppPresenter() }

    private val currencyRvAdapter: CurrenciesAdapter by lazy {
        CurrenciesAdapter({
            presenter.onRateSelected(it)
        }, {
            presenter.onRateValueChanged(it)
        })
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this).apply {
            isSmoothScrollbarEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(rvCurrencies) {
            adapter = currencyRvAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addMarginVerticalItemDecorator(R.dimen.margin_10)
        }

        currencyRvAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                linearLayoutManager.scrollToPosition(0)
            }
        })

        btnRetry.setOnClickListener {
            presenter.onBtnRetryClick()
        }
    }

    override fun onShowRateList(rateList: List<Rate>) {
        rvCurrencies.visibility = View.VISIBLE
        currencyRvAdapter.update(rateList)
    }

    override fun onShowMessage(messageResId: Int) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_LONG).show()
    }

    override fun onShowStubSmtWentWrong(show: Boolean) {
        if (show) {
            btnRetry.visibility = View.VISIBLE
            tvMessageOops.visibility = View.VISIBLE
        } else {
            btnRetry.visibility = View.GONE
            tvMessageOops.visibility = View.GONE
        }
    }

    override fun onShowProgress(show: Boolean) {
        if (show) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
    }
}
