package com.modern.currencies.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * listen only onTextChanged()
 *
 * @param onTextChange - lambda call when onTextChanged() triggered
 */
fun EditText.onTextChange(onTextChange: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            //No needed
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //No needed
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChange(s.toString())
        }

    })
}

/**
 * disable or enable editing
 *
 * @param enable
 */
fun EditText.enableEdit(enable: Boolean) {
    this.isFocusable = enable
    this.isFocusableInTouchMode = enable
    this.isCursorVisible = enable
    if (enable) requestFocus()
}