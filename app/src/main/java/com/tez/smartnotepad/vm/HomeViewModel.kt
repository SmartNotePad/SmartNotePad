package com.tez.smartnotepad.vm

import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.data.repository.NoteRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import java.util.*

class HomeViewModel(private val noteRepository: NoteRepository, private val startForSpeechResult: ActivityResultLauncher<Intent>, private val startForOcrResult: ActivityResultLauncher<Intent>): ViewModel() {


    fun getMyNotes(onSuccess: (notes: MutableList<NoteModel>) -> Unit){
        makeNetworkRequest(
            requestFunc = {
                noteRepository.getMyNotes()
            },
            onSuccess = {
                onSuccess.invoke(it)
            },
            onError = {

            }, viewModelScope
        )
    }

    fun getSharedNotesWithMe(onSuccess: (notes: MutableList<NoteModel>) -> Unit){
        makeNetworkRequest(
            requestFunc = {
                noteRepository.getSharedNotesWithMe()
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
                noteRepository.deleteNote(note)
            }, onSuccess = {
                onSuccess.invoke()
            }, onError = {
                onError.invoke(it)
            }, viewModelScope
        )
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