package com.tez.smartnotepad.data.datasource.remote

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.network.service.ContentService

class ContentRemoteDataSource(private val contentService: ContentService) : BaseRemoteDataSource() {

    suspend fun createContent(
        contentOwnerId: Int,
        contentOfNoteId: Int,
        content: String,
        type: Int
    ): ResultWrapper<ContentModel> =
        apiCall {
            contentService.createContent(
                ContentModel(
                    0,
                    contentOwnerId,
                    contentOfNoteId.toString(),
                    content,
                    0,
                    "",
                    "",
                    "",
                    2
                )
            )
        }

    suspend fun deleteContent(contentId: Int)
            : ResultWrapper<Any?> =
        apiCall { contentService.deleteContent(contentId) }

    suspend fun updateontent(content: ContentModel)
            : ResultWrapper<ContentModel> =
        apiCall { contentService.updateContent(content) }

    suspend fun getContentsOfNote(noteId: Int): ResultWrapper<MutableList<ContentModel>> =
        apiCall { contentService.getContentsOfNote(noteId) }
}