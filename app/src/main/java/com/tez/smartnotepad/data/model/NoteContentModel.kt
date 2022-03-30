package com.tez.smartnotepad.data.model

data class NoteContentModel(
    val contentId: Int,
    val userId: Int,
    val noteId: Int,
    val context: String,
    val type: Int
)
