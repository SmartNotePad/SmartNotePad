package com.tez.smartnotepad.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.NoteRepository
import com.tez.smartnotepad.data.repository.NoteRepositoryImpl
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepositoryImpl: NoteRepository
) : ViewModel() {


    val user = UserModel(
        userId = "2",
        mail = "string1",
        password = "string",
        nameSurname = "string",
        null,
        null
    )


    fun getMyNotes(onSuccess: (notes: MutableList<NoteModel>) -> Unit) {
        makeNetworkRequest(
            requestFunc = {
                noteRepositoryImpl.getMyNotes(user)
            },
            onSuccess = {
                onSuccess.invoke(it)
            },
            onError = {

            }, viewModelScope
        )
    }

    fun getSharedNotesWithMe(onSuccess: (notes: MutableList<NoteModel>) -> Unit) {
        makeNetworkRequest(
            requestFunc = {
                noteRepositoryImpl.getSharedNotesWithMe(user)
            },
            onSuccess = {
                onSuccess.invoke(it)
            },
            onError = {

            }, viewModelScope
        )
    }

    fun deleteNote(
        note: NoteModel,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
        makeNetworkRequest(
            requestFunc = {
                Log.e("Home ViewModel", note.title)
                noteRepositoryImpl.deleteNote(note)
            }, onSuccess = {
                onSuccess.invoke()
            }, onError = {
                onError.invoke(it)
            }, viewModelScope
        )
    }
}