package com.tez.smartnotepad.data.model

data class ShareNoteModel(
    val ownerUserId: Int,
    val noteId: Int,
    val mailToShare: String
)
