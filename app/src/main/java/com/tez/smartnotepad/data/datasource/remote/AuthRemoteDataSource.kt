package com.tez.smartnotepad.data.datasource.remote

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.network.service.UserService

class AuthRemoteDataSource(private val userService: UserService): BaseRemoteDataSource() {

    suspend fun register(user: UserModel): ResultWrapper<UserModel>{
        return apiCall { userService.addUser(user) }
    }

    suspend fun login(user: UserModel): ResultWrapper<UserModel>{
        return apiCall { userService.login(user) }
    }
}