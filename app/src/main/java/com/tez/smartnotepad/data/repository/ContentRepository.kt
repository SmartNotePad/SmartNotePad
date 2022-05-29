package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.ParticipantModel
import com.tez.smartnotepad.data.model.ShareNoteModel

interface ContentRepository {

    suspend fun createContent(content: ContentModel): ResultWrapper<ContentModel>

    suspend fun deleteContent(contentId: Int): ResultWrapper<Any?>

    suspend fun updateContent(content: ContentModel): ResultWrapper<ContentModel>

    suspend fun getContentsOfNote(noteId: Int): ResultWrapper<MutableList<ContentModel>>

    suspend fun shareNote(sharedNoteModel: ShareNoteModel): ResultWrapper<NoteModel>

    suspend fun deleteNote(note: NoteModel): ResultWrapper<Any>

    suspend fun removeParticipant(participantModel: ParticipantModel) : ResultWrapper<Any>
}
