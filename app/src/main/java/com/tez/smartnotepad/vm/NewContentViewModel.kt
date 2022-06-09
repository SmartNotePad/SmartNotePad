package com.tez.smartnotepad.vm

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.core.BaseViewModel
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.repository.ContentRepository
import com.tez.smartnotepad.network.helper.Request.makeNetworkRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewContentViewModel @Inject constructor(val contentRepositoryImpl: ContentRepository) :
    BaseViewModel() {

    fun addContent(
        ownerUserId: String,
        ownerNoteId: Int,
        context: EditText,
        type: Int = 2,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    ) {

        val content = ContentModel(
            0,
            ownerUserId.toInt(),
            "",
            "",
            ownerNoteId.toInt(),
            context.text.toString(),
            "",
            "",
            type
        )

        makeNetworkRequest(
            requestFunc = {
                contentRepositoryImpl.createContent(content)
            }, onSuccess = {
                onSuccess.invoke()
            }, onError = {
                onError.invoke(it)
            }, viewModelScope
        )
    }

    fun deleteContent(content: ContentModel) {

    }
}
