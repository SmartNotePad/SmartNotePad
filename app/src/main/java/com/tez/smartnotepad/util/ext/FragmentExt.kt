package com.tez.smartnotepad.util.ext

import androidx.fragment.app.Fragment

fun Fragment.name():String =
    this::class.java.simpleName


