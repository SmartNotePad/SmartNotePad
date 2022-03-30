package com.tez.smartnotepad.data.repository

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.datasource.remote.NoteRemoteDataSource
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel

class NoteRepository(private val userModel: UserModel, private val noteRemoteDataSource: NoteRemoteDataSource) {

    suspend fun getAllNotesOfUser(): ResultWrapper<List<NoteModel>>{
        return noteRemoteDataSource.getAllNotesOfUser(userModel)
    }
}