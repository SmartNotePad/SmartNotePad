package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.datasource.remote.ContentRemoteDataSource
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.ParticipantModel
import com.tez.smartnotepad.data.model.ShareNoteModel
import javax.inject.Inject


class ContentRepositoryImpl @Inject constructor(
    private val contentRemoteDataSource: ContentRemoteDataSource
) : ContentRepository {

    override suspend fun createContent(
        content: ContentModel
    ): ResultWrapper<ContentModel> =
        contentRemoteDataSource.createContent(content)

    override suspend fun deleteContent(contentId: Int)
            : ResultWrapper<Any?> =
        contentRemoteDataSource.deleteContent(contentId)

    override suspend fun updateContent(content: ContentModel)
            : ResultWrapper<ContentModel> =
        contentRemoteDataSource.updateContent(content)

    override suspend fun getContentsOfNote(noteId: Int)
            : ResultWrapper<MutableList<ContentModel>> =
        contentRemoteDataSource.getContentsOfNote(noteId)

    override suspend fun shareNote(sharedNoteModel: ShareNoteModel)
            : ResultWrapper<NoteModel> =
        contentRemoteDataSource.shareNote(sharedNoteModel)

    override suspend fun deleteNote(note: NoteModel) = contentRemoteDataSource.deleteNote(note)

    override suspend fun removeParticipant(participantModel: ParticipantModel) =
        contentRemoteDataSource.removeParticipant(participantModel)
}