package com.tez.smartnotepad.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.core.BaseViewModel
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.ParticipantModel
import com.tez.smartnotepad.data.model.ShareNoteModel
import com.tez.smartnotepad.data.repository.ContentRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewNoteViewModel @Inject constructor(
    private val contentRepositoryImpl: ContentRepository
) : BaseViewModel() {

    fun deleteContent(
        content: ContentModel,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
        makeNetworkRequest(
            requestFunc = {
                Log.e("ViewNoteViewModel", content.contentId.toString())
                contentRepositoryImpl.deleteContent(content.contentId)
            }, onSuccess = {
                onSuccess.invoke()
            }, onError = {
                onError.invoke(it)
            }, viewModelScope
        )
    }

    fun updateContent(
        content: ContentModel,
        onSuccess: (content: ContentModel) -> Unit,
        onError: (error: String) -> Unit
    ) {
        makeNetworkRequest(
            requestFunc = {
                contentRepositoryImpl.updateContent(content)
            }, onSuccess = {
                onSuccess.invoke(it)
            }, onError = {
                onError.invoke(it)
            }, viewModelScope
        )
    }

    fun getContentsOfNote(
        noteId: Int,
        onSuccess: (contents: MutableList<ContentModel>) -> Unit,
        onError: (error: String) -> Unit
    ) {
        makeNetworkRequest(
            requestFunc = {
                contentRepositoryImpl.getContentsOfNote(noteId)
            },
            onSuccess = {
                onSuccess.invoke(it)
            },
            onError = {
                onError.invoke(it)
            },
            viewModelScope
        )
    }

    fun shareNote(
        sharedNoteModel: ShareNoteModel,
        onSuccess: (sharedNote: NoteModel) -> Unit,
        onError: (error: String) -> Unit
    ) {
        makeNetworkRequest(
            requestFunc = {
                contentRepositoryImpl.shareNote(sharedNoteModel)
            },
            onSuccess = {
                onSuccess.invoke(it)
            },
            onError = {
                onError.invoke(it)
            },
            viewModelScope
        )
    }

    fun deleteNote(note: NoteModel) {
        makeNetworkRequest({
            contentRepositoryImpl.deleteNote(note)
        }, {}, {}, viewModelScope)
    }

    fun removeParticipantUser(participantModel: ParticipantModel, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        makeNetworkRequest({
            contentRepositoryImpl.removeParticipant(participantModel)
        }, {
           onSuccess.invoke()
        }, {
           onError.invoke(it)
        }, viewModelScope)
    }
}