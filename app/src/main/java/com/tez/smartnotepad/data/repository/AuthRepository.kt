package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.datasource.remote.AuthRemoteDataSource
import com.tez.smartnotepad.data.model.UserModel

class AuthRepository (
    private val prefDataSource: PrefDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
){
    suspend fun register(userModel: UserModel): ResultWrapper<UserModel> {
        return authRemoteDataSource.register(userModel)
    }

    fun isUserLogged(): Boolean = prefDataSource.isUserLogged()

    fun saveUserIdToPref(userId: Int) {
        prefDataSource.userId = userId
    }
    fun removeUserFromPref() {
        prefDataSource.userId = 0
    }
    fun getUserId(): Int = prefDataSource.userId

}