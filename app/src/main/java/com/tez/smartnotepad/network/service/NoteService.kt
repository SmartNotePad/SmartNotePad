package com.tez.smartnotepad.network.service

import com.tez.smartnotepad.data.model.BaseResponseModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import retrofit2.http.*

interface NoteService {

    @GET("notes/get-all-by-owner-user-id")
    suspend fun getMyNotes(
        @Query("id") userId: Int
    ): BaseResponseModel<MutableList<NoteModel>>

    @GET("notes/get-all-by-shared-notes-user-id")
    suspend fun getSharedNotesWithMe(
        @Query("id") userId: Int
    ): BaseResponseModel<MutableList<NoteModel>>

    @POST("notes/add")
    suspend fun createNoteWithoutContent(
        @Body emptyNote: NoteModel
    ): BaseResponseModel<NoteModel>

    @PUT("notes/update-note-title")
    suspend fun updateNoteTitle(
        @Body note: NoteModel
    ): BaseResponseModel<NoteModel>

    @HTTP(method = "DELETE", path = "notes/delete", hasBody = true)
    suspend fun deleteNote(
        @Body note: NoteModel
    ): BaseResponseModel<Any?>
}