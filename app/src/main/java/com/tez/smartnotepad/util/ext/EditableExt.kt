package com.tez.smartnotepad.util.ext

import android.widget.EditText

fun EditText.textAsString(): String {
    return this.text.toString()
}