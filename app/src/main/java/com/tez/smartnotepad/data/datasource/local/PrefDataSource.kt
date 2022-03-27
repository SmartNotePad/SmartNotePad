package com.tez.smartnotepad.data.datasource.local

import android.content.SharedPreferences

class PrefDataSource(private val sharedPref: SharedPreferences ) {

    var userId: Int
        get() = sharedPref.getInt("USER_ID",0)
        set(value) {
            sharedPref.edit().putInt("USER_ID",value)
        }

    fun isUserLogged(): Boolean {
        return false
    }

}