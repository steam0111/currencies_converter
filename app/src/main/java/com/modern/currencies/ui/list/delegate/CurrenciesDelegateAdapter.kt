package com.modern.currencies.ui.list.delegate

import android.widget.EditText
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.modern.currencies.R
import com.modern.currencies.entity.ui.Rate
import com.modern.currencies.extension.enableEdit
import com.modern.currencies.extension.onTextChange
import com.modern.currencies.extension.safeToFloat


sealed class CurrenciesDaPayloads {
    object ChangeRate : CurrenciesDaPayloads()
    object ChangeEditable : CurrenciesDaPayloads()
}

/**
 * get delegate adapter for currency row
 *
 */
fun currenciesDelegateAdapter(
    onCurrencySelected: (Rate) -> Unit,
    onCurrencyValueChanged: (Rate) -> Unit
) =
    adapterDelegate<Rate, Rate>(R.layout.item_currency) {

        val tvName = findViewById<TextView>(R.id.tvName)
        val etRate = findViewById<EditText>(R.id.etRate)

        itemView.setOnClickListener {
            onCurrencySelected(item)
        }

        etRate.setOnClickListener {
            onCurrencySelected(item)
        }

        etRate.onTextChange { text ->
            etRate.setSelection(text.length)

            onCurrencyValueChanged(item.apply {
                value = text.safeToFloat()
            })
        }

        bind { payloads ->
            if (payloads.isEmpty()) {

                tvName.text = item.name
                etRate.setText(item.getTextRate())
                etRate.enableEdit(item.isEditable)

            } else {
                val firstPayload = payloads[0]

                if (firstPayload is List<*>) {

                    if (firstPayload[0] == CurrenciesDaPayloads.ChangeRate) {
                        etRate.setText(item.getTextRate())
                    }

                    if (firstPayload[1] == CurrenciesDaPayloads.ChangeEditable) {
                        etRate.enableEdit(item.isEditable)
                    }
                }
            }
        }

        //for hide keyboard when we scroll down
        onViewDetachedFromWindow {
            etRate.clearFocus()
        }
    }