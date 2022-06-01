package com.tez.smartnotepad.network.service

import com.tez.smartnotepad.data.model.BaseResponseModel
import com.tez.smartnotepad.data.model.UserModel
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {

    @POST("users/add")
    suspend fun addUser(
        @Body userModel: UserModel
    ): BaseResponseModel<UserModel>

    @POST("users/login")
    suspend fun login(
        @Body userModel: UserModel
    ): BaseResponseModel<UserModel>

    @PUT("users/update")
    suspend fun update(
        @Body userData: UserModel
    ): BaseResponseModel<UserModel>
}