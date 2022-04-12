package com.tez.smartnotepad.network.service

import com.tez.smartnotepad.data.model.BaseResponseModel
import com.tez.smartnotepad.data.model.ContentModel
import com.tez.smartnotepad.data.model.NoteModel
import retrofit2.http.*

interface ContentService {

    @POST("contents/add")
    suspend fun createContent(
        @Body content: ContentModel
    ): BaseResponseModel<ContentModel>

    @HTTP(method = "DELETE", path = "contents/delete", hasBody = true)
    suspend fun deleteContent(
        @Body contentId: Int
    ): BaseResponseModel<Any?>

    @PUT("contents/update")
    suspend fun updateContent(
        @Body content: ContentModel
    ): BaseResponseModel<ContentModel>

    @GET("notes/get-all-contents-by-note-id-")
    suspend fun getContentsOfNote(
        @Query("id") noteId: Int
    ): BaseResponseModel<MutableList<ContentModel>>
}