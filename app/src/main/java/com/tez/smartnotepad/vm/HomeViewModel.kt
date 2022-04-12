package com.tez.smartnotepad.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.repository.NoteRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val noteRepository: NoteRepository): ViewModel() {

    private val _notes = MutableLiveData<List<NoteModel>>()
    val notes: LiveData<List<NoteModel>>
        get()  = _notes

    fun getAllNotesOfUser() {
        makeNetworkRequest(
            requestFunc = { noteRepository.getAllNotesOfUser() }
        )
    }

    private fun <T> makeNetworkRequest(
        requestFunc: suspend () -> ResultWrapper<T>
    ) {
        viewModelScope.launch {
            when (val response = requestFunc.invoke()) {
                is ResultWrapper.Success -> {

                    val responseNotes = response.value as List<NoteModel>
                    if (responseNotes.isEmpty())
                        _notes.value = emptyList()
                    else
                        _notes.value = responseNotes
                }
                is ResultWrapper.Error -> {
                    // TODO handle error
                    Log.e(HomeViewModel::class.java.simpleName,"Notlar dönerken hata oluştu")
                    Log.e(HomeViewModel::class.java.simpleName,response.toString())
                    _notes.value = emptyList()
                }
            }
        }
    }
}