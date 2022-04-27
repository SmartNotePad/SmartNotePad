package com.tez.smartnotepad.vm

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.repository.ContentRepository
import com.tez.smartnotepad.network.helper.Request

class NewContentViewModel(private val contentRepository: ContentRepository) : ViewModel() {

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

        Request.makeNetworkRequest(
            requestFunc = {
                contentRepository.createContent(content)
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
