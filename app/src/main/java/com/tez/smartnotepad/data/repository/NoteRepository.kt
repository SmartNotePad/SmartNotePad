package com.tez.smartnotepad.data.repository

import android.util.Log
import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.datasource.remote.NoteRemoteDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel

class NoteRepository(private val user: UserModel, private val noteRemoteDataSource: NoteRemoteDataSource) {

    suspend fun getAllNotesOfUser(): ResultWrapper<List<NoteModel>>{
        return noteRemoteDataSource.getAllNotesOfUser(user)
    }

    suspend fun createEmptyNote(user: UserModel): ResultWrapper<NoteModel> {
        return noteRemoteDataSource.createEmptyNote(user)
    }

    suspend fun updateNoteTitle(note: NoteModel): ResultWrapper<NoteModel> {
        return noteRemoteDataSource.updateNoteTitle(note)
    }
}