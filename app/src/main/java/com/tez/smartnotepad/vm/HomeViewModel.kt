package com.tez.smartnotepad.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.NoteRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest

class HomeViewModel(private val noteRepository: NoteRepository): ViewModel() {

    fun getAllNotesOfUser(onSuccess: (user: UserModel) -> Unit){
        makeNetworkRequest(
            requestFunc = {
                noteRepository.getAllNotesOfUser()
            },
            onSuccess = {
                onSuccess.invoke(it)
            },
            onError = {

            }, viewModelScope
        )
    }
}