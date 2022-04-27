package com.tez.smartnotepad.util.ext

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun Context.showMessage(msg: String)  {
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}