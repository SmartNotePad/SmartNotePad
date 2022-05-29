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
    override suspend fun register(userModel: UserModel): ResultWrapper<UserModel> {
        return authRemoteDataSource.register(userModel)
    }

    override suspend fun login(userModel: UserModel): ResultWrapper<UserModel> {
        return authRemoteDataSource.login(userModel)
    }

    override suspend fun saveUserToPref(user: UserModel) {
        prefDataSource.user = user
    }
}