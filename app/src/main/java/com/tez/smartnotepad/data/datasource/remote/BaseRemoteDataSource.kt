package com.tez.smartnotepad.data.datasource.remote

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.BaseResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRemoteDataSource {

    internal suspend fun<T> apiCall(call: suspend() -> BaseResponseModel<T>): ResultWrapper<T> {

        return withContext(Dispatchers.IO){
            try {
                val response = call.invoke()
                if(!response.success){
                    throw Exception(response.message)
                }
                ResultWrapper.Success(response.data)
            }catch (throwable: Exception){
                ResultWrapper.Error(throwable.message.toString())
            }
        }
    }
}