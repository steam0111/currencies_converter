package com.modern.currencies.ui.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.modern.currencies.entity.ui.Rate
import com.modern.currencies.ui.list.delegate.CurrenciesDaPayloads
import com.modern.currencies.ui.list.delegate.currenciesDelegateAdapter

fun Rate.isSame(rate: Rate) = this.name == rate.name
fun Rate.isContentSame(rate: Rate) =
    !(this.value != rate.value || this.isEditable != rate.isEditable)

class CurrenciesAdapter(
    onCurrencySelected: (Rate) -> Unit,
    onCurrencyValueChanged: (Rate) -> Unit
) :
    AsyncListDifferDelegationAdapter<Rate>(
        object : DiffUtil.ItemCallback<Rate>() {
            override fun areItemsTheSame(oldItem: Rate, newItem: Rate): Boolean {
                return oldItem.isSame(newItem)
            }


            override fun areContentsTheSame(oldItem: Rate, newItem: Rate): Boolean {
                return oldItem.isContentSame(newItem)
            }

            override fun getChangePayload(oldItem: Rate, newItem: Rate): List<Any?> {
                return listOf(
                    if (oldItem.value != newItem.value) {
                        CurrenciesDaPayloads.ChangeRate
                    } else {
                        null
                    },
                    if (oldItem.isEditable != newItem.isEditable) {
                        CurrenciesDaPayloads.ChangeEditable
                    } else {
                        null
                    }
                )
            }
        }
    ) {
    init {
        items = mutableListOf()

        delegatesManager.addDelegate(
            currenciesDelegateAdapter(
                onCurrencySelected,
                onCurrencyValueChanged
            )
        )
    }

    fun update(data: List<Rate>) {
        items = mutableListOf<Rate>().apply {
            addAll(data.map { it.copy() })
        }
    }

}