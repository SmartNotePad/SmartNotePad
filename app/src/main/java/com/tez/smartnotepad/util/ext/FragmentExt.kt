package com.tez.smartnotepad.util.ext

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.name():String =
    this::class.java.simpleName

fun Fragment.showMessage(message: String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}

