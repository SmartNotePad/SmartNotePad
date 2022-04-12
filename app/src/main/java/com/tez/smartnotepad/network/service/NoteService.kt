package com.tez.smartnotepad.network.service

import com.tez.smartnotepad.data.model.BaseResponseModel
import com.tez.smartnotepad.data.model.NoteModel
import retrofit2.http.*

interface NoteService {

    @GET("notes/get-all-by-owner-user-id")
    suspend fun getAllNotesOfUser(
        @Query("id") userId: Int
    ): BaseResponseModel<List<NoteModel>>

    @POST("notes/add")
    suspend fun createNoteWithoutContent(
        @Body emptyNote: NoteModel
    ): BaseResponseModel<NoteModel>

    @PUT("notes/update-note-title")
    suspend fun updateNoteTitle(
        @Body note: NoteModel
    ): BaseResponseModel<NoteModel>
}