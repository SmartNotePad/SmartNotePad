package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.UserModel

interface AuthRepository {
    suspend fun register(user: UserModel): ResultWrapper<UserModel>

    suspend fun login(user: UserModel): ResultWrapper<UserModel>

    suspend fun saveUserToPref(user: UserModel)
}