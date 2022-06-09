package com.tez.smartnotepad.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.core.BaseViewModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.AuthRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val authRepositoryImpl: AuthRepository) : BaseViewModel() {

    fun register(
        userModel: UserModel,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        makeNetworkRequest(
            requestFunc = { authRepositoryImpl.register(userModel) },
            onSuccess = {
                onSuccess.invoke()
            }, onError = {
                onError.invoke(it)
            }, viewModelScope
        )
    }
}

