package com.tez.smartnotepad.data.datasource.remote

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.network.service.NoteService

class NoteRemoteDataSource(private val noteService: NoteService): BaseRemoteDataSource() {

    suspend fun getAllNotesOfUser(userModel: UserModel): ResultWrapper<List<NoteModel>> {
        return apiCall { noteService.getAllNotesOfUser((userModel.userId!!).toInt()) }
    }
}