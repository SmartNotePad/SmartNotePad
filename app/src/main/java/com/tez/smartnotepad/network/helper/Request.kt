package com.tez.smartnotepad.network.helper

import android.util.Log
import com.tez.smartnotepad.data.ResultWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object Request {
    fun <T> makeNetworkRequest(
        requestFunc: suspend () -> ResultWrapper<T>,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (String) -> Unit,
        scope: CoroutineScope
    ){
        scope.launch{
            when (val response = requestFunc.invoke()) {
                is ResultWrapper.Success -> {
                    onSuccess.invoke(response.value)
                }
                is ResultWrapper.Error -> {
                    Log.e(Request::class.java.simpleName, "Hata oluştu")
                    Log.e(Request::class.java.simpleName, response.value)
                    onError.invoke(response.value)
                }
            }
        }
    }
}