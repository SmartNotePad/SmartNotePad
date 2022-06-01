package com.tez.smartnotepad.network.service

import com.tez.smartnotepad.data.model.*
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

    // TODO note service çek
    @GET("notes/get-all-contents-by-note-id-")
    suspend fun getContentsOfNote(
        @Query("id") noteId: Int
    ): BaseResponseModel<MutableList<ContentModel>>

    @POST("notes/share")
    suspend fun shareNote(
        @Body shareNoteModel: ShareNoteModel
    ): BaseResponseModel<NoteModel>

    @HTTP(method = "DELETE", path = "notes/delete", hasBody = true)
    suspend fun deleteNote(
        @Body note: NoteModel
    ): BaseResponseModel<Any>

    @HTTP(method = "DELETE", path = "notes/delete-partipitiantuser-from-note", hasBody = true)
    suspend fun removeParticipant(
        @Body participantModel: ParticipantModel
    ): BaseResponseModel<Any>

}