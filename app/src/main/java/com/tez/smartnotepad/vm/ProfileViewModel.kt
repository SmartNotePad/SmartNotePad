package com.tez.smartnotepad.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.AuthRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val authRepository: AuthRepository) : ViewModel() {

    fun updateUser(userData: UserModel,onSuccess:()->Unit,onError:(error: String)->Unit) {

        makeNetworkRequest(
            requestFunc = {
                authRepository.updateUser(userData)
            },
            onSuccess = {
                authRepository.removeUserFromPref()
                authRepository.saveUserToPref(it)
                onSuccess.invoke()
                Log.e("updated user", it.nameSurname.toString())
            },
            onError = {
                onError.invoke(it)
                Log.e("error updating user", it)
            },
            viewModelScope
        )
    }

}