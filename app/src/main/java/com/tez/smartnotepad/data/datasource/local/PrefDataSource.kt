package com.tez.smartnotepad.data.datasource.local

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.tez.smartnotepad.data.model.UserModel


class PrefDataSource(private val sharedPref: SharedPreferences) {

    private var gson: Gson = Gson()

    var user: UserModel?
        get() {
            return gson.fromJson(
                sharedPref.getString("USER", ""),UserModel::class.java)
        }
        set(value) {
            sharedPref.edit().putString(
                "USER",gson.toJson(value)).apply()
        }

    fun isUserLogged(): Boolean {
        Log.e(PrefDataSource::class.java.simpleName,user.toString())
        return user != null
    }

    /*
    fun removeUser() =
        sharedPref.edit().remove("USER").apply()
    */
}