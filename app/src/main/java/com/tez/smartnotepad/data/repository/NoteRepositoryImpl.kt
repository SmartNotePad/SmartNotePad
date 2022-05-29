package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.datasource.remote.NoteRemoteDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteRemoteDataSource: NoteRemoteDataSource
) : NoteRepository {


    override suspend fun getMyNotes(user: UserModel): ResultWrapper<MutableList<NoteModel>> {
        return noteRemoteDataSource.getMyNotes(user)
    }

    override suspend fun getSharedNotesWithMe(user: UserModel): ResultWrapper<MutableList<NoteModel>> {
        return noteRemoteDataSource.getSharedNotesWithMe(user)
    }

    override suspend fun createEmptyNote(user: UserModel): ResultWrapper<NoteModel> {
        return noteRemoteDataSource.createEmptyNote(user)
    }

    override suspend fun updateNoteTitle(note: NoteModel): ResultWrapper<NoteModel> {
        return noteRemoteDataSource.updateNoteTitle(note)
    }

    override suspend fun deleteNote(note: NoteModel): ResultWrapper<Any?> {
        return noteRemoteDataSource.deleteNote(note)
    }
}

