package com.tez.smartnotepad.vm

import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.ShareNoteModel
import com.tez.smartnotepad.data.repository.ContentRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import kotlinx.coroutines.launch
import java.util.*

class ViewNoteViewModel(private val contentRepository: ContentRepository, private val startForSpeechResult: ActivityResultLauncher<Intent>, private val startForOcrResult: ActivityResultLauncher<Intent>) : ViewModel() {



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


    fun displaySpeechRecognizer() {
        startForSpeechResult.launch(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("en_US"))
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                Locale("Hi from the inside of the android on windows. Sanki inception.")
            )
        })
    }

    fun displayOcr() {
        val chooseIntent = Intent()
        chooseIntent.type = "image/*"
        chooseIntent.action = Intent.ACTION_GET_CONTENT
        startForOcrResult.launch(chooseIntent)
    }

}