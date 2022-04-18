package com.tez.smartnotepad.network.service

import com.tez.smartnotepad.data.model.BaseResponseModel
import com.tez.smartnotepad.data.model.NoteModel
import com.tez.smartnotepad.data.model.UserModel
import retrofit2.http.*

interface NoteService {

    @GET("users/get-by-id")
    suspend fun getAllNotesOfUser(
        @Query("id") userId: Int
    ): BaseResponseModel<UserModel>

    @POST("notes/add")
    suspend fun createNoteWithoutContent(
        @Body emptyNote: NoteModel
    ): BaseResponseModel<NoteModel>

    @PUT("notes/update-note-title")
    suspend fun updateNoteTitle(
        @Body note: NoteModel
    ): BaseResponseModel<NoteModel>
}