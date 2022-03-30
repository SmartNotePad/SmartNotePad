package com.tez.smartnotepad.data.model

data class NoteModel (
    val noteId: Int,
    val userDtoList: UserModel?,
    val contentsContentDtos: List<NoteContentModel>
)

