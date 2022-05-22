package com.tez.smartnotepad.util.ext

import java.text.SimpleDateFormat
import java.util.*

fun String.getParsedDate() : String{

    var result = ""
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US)
    val parse = parser.parse(this)

    parse?.let {
        result = formatter.format(parse)
    }

    return result
}