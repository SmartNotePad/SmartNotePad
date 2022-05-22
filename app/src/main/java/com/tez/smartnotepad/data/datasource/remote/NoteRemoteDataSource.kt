package com.tez.smartnotepad.data.datasource.remote

import com.tez.smartnotepad.data.ResultWrapper
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import com.tez.smartnotepad.network.service.NoteService

class NoteRemoteDataSource(private val noteService: NoteService): BaseRemoteDataSource() {

    suspend fun getMyNotes(userModel: UserModel): ResultWrapper<MutableList<NoteModel>> {
        return apiCall { noteService.getMyNotes((userModel.userId).toInt()) }
    }

    suspend fun getSharedNotesWithMe(userModel: UserModel): ResultWrapper<MutableList<NoteModel>> {
        return apiCall { noteService.getSharedNotesWithMe((userModel.userId).toInt()) }
    }
    // hangi yazım daha yakışıklı ? :P
    suspend fun createEmptyNote(
        user: UserModel
    ): ResultWrapper<NoteModel> =
        apiCall { noteService.createNoteWithoutContent(NoteModel(noteId = 0, title = "Empty Note", userUserId = user.userId.toInt(),userMail = user.mail,"",null,null, null)) }

    suspend fun updateNoteTitle(note: NoteModel)
    : ResultWrapper<NoteModel> =
        apiCall { noteService.updateNoteTitle(note) }

    suspend fun deleteNote(note: NoteModel) =
        apiCall { noteService.deleteNote(note) }

}
