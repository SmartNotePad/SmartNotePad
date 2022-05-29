package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel

// Interface vs Abstract Impl. ? diff?

interface NoteRepository {

    suspend fun getMyNotes(user: UserModel): ResultWrapper<MutableList<NoteModel>>

    suspend fun getSharedNotesWithMe(user: UserModel): ResultWrapper<MutableList<NoteModel>>

    suspend fun createEmptyNote(user: UserModel): ResultWrapper<NoteModel>

    suspend fun updateNoteTitle(note: NoteModel): ResultWrapper<NoteModel>

    suspend fun deleteNote(note: NoteModel): ResultWrapper<Any?>
}
