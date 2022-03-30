package com.tez.smartnotepad.network.service

import com.tez.smartnotepad.data.model.BaseResponseModel
import com.tez.smartnotepad.data.model.NoteModel
import retrofit2.http.POST
import retrofit2.http.Query

interface NoteService {

    @POST("notes/get-all-by-owner-user-id")
    suspend fun getAllNotesOfUser(
        @Query("id") userId: Int
    ): BaseResponseModel<List<NoteModel>>
}