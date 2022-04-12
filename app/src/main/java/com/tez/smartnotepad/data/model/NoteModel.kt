package com.tez.smartnotepad.data.model
import kotlinx.serialization.Serializable

@Serializable
data class NoteModel (
    val noteId: Int,
    val title: String,
    val userUserId: Int,
    val userMail: String?,
    val createdDate: String?,
    val modifiedDate: String?,
    var contentsContentDtos: MutableList<ContentModel>?,
    val participantUsersUserId: List<UserModel>?
)

