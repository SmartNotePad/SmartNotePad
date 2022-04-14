package com.tez.smartnotepad.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.repository.NoteRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest

class HomeViewModel(private val noteRepository: NoteRepository): ViewModel() {

    fun getAllNotesOfUser(onSuccess: (list: MutableList<NoteModel>) -> Unit){
        makeNetworkRequest(
            requestFunc = {
                noteRepository.getAllNotesOfUser()
            },
            onSuccess = {
                onSuccess.invoke(it as MutableList<NoteModel>)
            },
            onError = {

            }, viewModelScope
        )
    }
}