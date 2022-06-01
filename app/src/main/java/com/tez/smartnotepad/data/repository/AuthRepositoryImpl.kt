package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.datasource.local.PrefDataSource
import com.tez.smartnotepad.data.datasource.remote.AuthRemoteDataSource
import com.tez.smartnotepad.data.model.UserModel
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val prefDataSource: PrefDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun register(user: UserModel): ResultWrapper<UserModel> {
        return authRemoteDataSource.register(user)
    }

    override suspend fun login(user: UserModel): ResultWrapper<UserModel> {
        return authRemoteDataSource.login(user)
    }

    override suspend fun saveUserToPref(user: UserModel) {
        prefDataSource.user = user
    }

    override suspend fun updateUser(userData: UserModel): ResultWrapper<UserModel> {
        return authRemoteDataSource.update(userData)
    }

    override suspend fun removeUserFromPref()
        = prefDataSource.removeUser()
}