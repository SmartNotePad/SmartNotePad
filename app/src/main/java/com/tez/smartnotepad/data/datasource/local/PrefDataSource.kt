package com.tez.smartnotepad.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.tez.smartnotepad.data.model.UserModel


class PrefDataSource(private val sharedPref: SharedPreferences) {

    private var gson: Gson = Gson()

    var user: UserModel?
        get() {
            return gson.fromJson<UserModel>(
                sharedPref.getString("USER", ""),UserModel::class.java)
        }
        set(value: UserModel?) {
            val result = sharedPref.edit().putString(
                "USER",gson.toJson(value)).commit()
        }

    // bad usage like requireContext() / When we are in fragment, we %100 have the user
    fun requireUser(): UserModel {
        return user
            ?: throw IllegalStateException("SharedPreferences not have the user.")
    }

    fun isUserLogged(): Boolean {
        Log.e(PrefDataSource::class.java.simpleName,user.toString())
        return user.toString().isNotEmpty()
    }

    fun removeUser() {
        sharedPref.edit().remove("USER").apply()
    }
}