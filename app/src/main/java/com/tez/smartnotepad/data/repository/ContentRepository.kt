package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.datasource.remote.ContentRemoteDataSource
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.UserModel

// user ve note alıp, parametrelerde gerekli alanları burda doldurmak mı mantıklı yoksa viewModel üstünden mi idleri direk göndermeli?
class ContentRepository(
    private val user: UserModel,
    private val contentRemoteDataSource: ContentRemoteDataSource
) {

    suspend fun createContent(
        content: String, type: Int, noteId: Int
    ): ResultWrapper<ContentModel> =
        contentRemoteDataSource.createContent(user.userId.toInt(), noteId, content, type)

    suspend fun deleteContent(contentId: Int)
            : ResultWrapper<Any?> =
        contentRemoteDataSource.deleteContent(contentId)

    suspend fun updateContent(content: ContentModel)
            : ResultWrapper<ContentModel> =
        contentRemoteDataSource.updateontent(content)

    suspend fun getContentsOfNote(noteId: Int)
            : ResultWrapper<MutableList<ContentModel>> =
        contentRemoteDataSource.getContentsOfNote(noteId)
}