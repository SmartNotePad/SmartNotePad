package com.tez.smartnotepad.vm

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.ContentRepository
import com.tez.smartnotepad.data.repository.NoteRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    private val noteRepositoryImpl: NoteRepository,
    private val contentRepositoryImpl: ContentRepository
) : ViewModel() {

    private var _note = MutableLiveData<NoteModel>()
    val note: LiveData<NoteModel>
        get() = _note

    fun createEmptyNote(user: UserModel) {
        makeNetworkRequest(
            requestFunc = {
                noteRepositoryImpl.createEmptyNote(user)
            },
            onSuccess = {
                Log.e(NewNoteViewModel::class.java.simpleName + " - createEmptyNote", it.toString())
                _note.value = it
            },
            onError = {

            },
            viewModelScope
        )
    }

    private fun updateNoteTitle(etTitle: EditText) {
        makeNetworkRequest(
            requestFunc = {
                noteRepositoryImpl.updateNoteTitle(_note.value!!.copy(title = etTitle.text.toString()))
            },
            onSuccess = {
                Log.e(NewNoteViewModel::class.java.simpleName + " - updateNoteTitle", it.toString())
                _note.value = it
            },
            onError = {

            },
            viewModelScope
        )
    }

    fun createContentAndUpdateTitle(
        etTitle: EditText,
        etContent: EditText,
        type: Int,
        ownerUserId: String
    ) {

        // fix çözüm. sonra detay giricem.
        //val noteId = if(note.value?.noteId != null) {note.value!!.noteId} else {1}

        val content = ContentModel(
            0,
            ownerUserId.toInt(),
            "",
            "",
            _note.value!!.noteId,
            etContent.text.toString(),
            "",
            "",
            type
        )

        makeNetworkRequest(
            requestFunc = {
                contentRepositoryImpl.createContent(content)
            },
            onSuccess = {
                Log.e(NewNoteViewModel::class.java.simpleName + " - createContent ", it.toString())
                updateNoteTitle(etTitle)
            },
            onError = {
                Log.e(NewNoteViewModel::class.java.simpleName, " Hata !" + it)
            },
            viewModelScope
        )
    }

    fun deleteNote(note: NoteModel) {
        Log.e(NewNoteViewModel::class.java.simpleName, "Bu not silinecek " + note.noteId.toString())
    }

}