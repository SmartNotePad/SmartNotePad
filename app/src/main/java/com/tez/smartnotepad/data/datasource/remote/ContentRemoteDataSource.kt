package com.tez.smartnotepad.data.datasource.remote

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.*
import com.tez.smartnotepad.network.service.ContentService

class ContentRemoteDataSource(private val contentService: ContentService) : BaseRemoteDataSource() {

    suspend fun createContent(content: ContentModel)
            : ResultWrapper<ContentModel> =
        apiCall { contentService.createContent(content) }

    suspend fun deleteContent(contentId: Int)
            : ResultWrapper<Any?> =
        apiCall { contentService.deleteContent(contentId) }

    suspend fun updateContent(content: ContentModel)
            : ResultWrapper<ContentModel> =
        apiCall { contentService.updateContent(content) }

    suspend fun getContentsOfNote(noteId: Int): ResultWrapper<MutableList<ContentModel>> =
        apiCall { contentService.getContentsOfNote(noteId) }

    suspend fun shareNote(shareNoteModel: ShareNoteModel): ResultWrapper<NoteModel> =
        apiCall { contentService.shareNote(shareNoteModel) }

    suspend fun deleteNote(note: NoteModel) =
        apiCall { contentService.deleteNote(note) }

    suspend fun removeParticipant(participantModel: ParticipantModel) =
        apiCall { contentService.removeParticipant(participantModel) }

}