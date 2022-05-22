package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.datasource.remote.NoteRemoteDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel

class NoteRepository(private val user: UserModel, private val noteRemoteDataSource: NoteRemoteDataSource) {

    suspend fun getMyNotes(): ResultWrapper<MutableList<NoteModel>>{
        return noteRemoteDataSource.getMyNotes(user)
    }

    suspend fun getSharedNotesWithMe(): ResultWrapper<MutableList<NoteModel>>{
        return noteRemoteDataSource.getSharedNotesWithMe(user)
    }

    suspend fun createEmptyNote(user: UserModel): ResultWrapper<NoteModel> {
        return noteRemoteDataSource.createEmptyNote(user)
    }

    suspend fun updateNoteTitle(note: NoteModel): ResultWrapper<NoteModel> {
        return noteRemoteDataSource.updateNoteTitle(note)
    }

    suspend fun deleteNote(note: NoteModel)
            = noteRemoteDataSource.deleteNote(note)
}