package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.datasource.remote.ContentRemoteDataSource
import com.tez.smartnotepad.data.model.*

// user ve note alıp, parametrelerde gerekli alanları burda doldurmak mı mantıklı yoksa viewModel üstünden mi idleri direk göndermeli?
class ContentRepository(
    private val user: UserModel,
    private val contentRemoteDataSource: ContentRemoteDataSource
) {

    suspend fun createContent(
        content: ContentModel
    ): ResultWrapper<ContentModel> =
        contentRemoteDataSource.createContent(content)

    suspend fun deleteContent(contentId: Int)
            : ResultWrapper<Any?> =
        contentRemoteDataSource.deleteContent(contentId)

    suspend fun updateContent(content: ContentModel)
            : ResultWrapper<ContentModel> =
        contentRemoteDataSource.updateContent(content)

    suspend fun getContentsOfNote(noteId: Int)
            : ResultWrapper<MutableList<ContentModel>> =
        contentRemoteDataSource.getContentsOfNote(noteId)

    suspend fun shareNote(sharedNoteModel: ShareNoteModel)
            : ResultWrapper<NoteModel> =
        contentRemoteDataSource.shareNote(sharedNoteModel)

    suspend fun deleteNote(note: NoteModel)
        = contentRemoteDataSource.deleteNote(note)

    suspend fun removeParticipant(participantModel: ParticipantModel)
        = contentRemoteDataSource.removeParticipant(participantModel)
}