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
    suspend fun login(userModel: UserModel): ResultWrapper<UserModel> {
        return authRemoteDataSource.login(userModel)
    }
    fun saveUserToPref(user: UserModel) {
        prefDataSource.user = user
    }

/*

    fun isUserLogged(): Boolean = prefDataSource.isUserLogged()

    fun removeUserFromPref() {
        prefDataSource.removeUser()
    }

    fun getUserId(): String? =
        if(prefDataSource.isUserLogged()) prefDataSource.user!!.userId else null

*/
}