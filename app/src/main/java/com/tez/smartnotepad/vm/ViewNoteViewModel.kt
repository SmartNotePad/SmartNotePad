package com.tez.smartnotepad.vm

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.ShareNoteModel
import com.tez.smartnotepad.data.repository.ContentRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import kotlinx.coroutines.Dispatchers

class ViewNoteViewModel(private val contentRepository: ContentRepository) : ViewModel() {

    fun deleteContent(
        content: ContentModel,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
        makeNetworkRequest(
            requestFunc = {
                Log.e("ViewNoteViewModel", content.contentId.toString())
                contentRepository.deleteContent(content.contentId)
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
                contentRepository.updateContent(content)
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
                contentRepository.getContentsOfNote(noteId)
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
    )
    {
        makeNetworkRequest(
            requestFunc = {
                contentRepository.shareNote(sharedNoteModel)
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

    fun deleteNote(note: NoteModel){
        makeNetworkRequest({
            contentRepository.deleteNote(note)
        },{},{},viewModelScope)
    }

}