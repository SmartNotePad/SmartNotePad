package com.tez.smartnotepad.network.service

import com.tez.smartnotepad.data.model.BaseResponseModel
import com.tez.smartnotepad.data.model.UserModel
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("users/add")
    suspend fun addUser(
        @Body userModel: UserModel
    ): BaseResponseModel<UserModel>

    @POST("users/login")
    suspend fun login(
        @Body userModel: UserModel
    ): BaseResponseModel<UserModel>
}