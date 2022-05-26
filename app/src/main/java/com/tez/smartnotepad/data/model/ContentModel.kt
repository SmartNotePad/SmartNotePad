package com.tez.smartnotepad.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ContentModel(
    val contentId: Int,
    val userId: Int,
    val nameSurname: String,
    val mail: String,
    val noteId: Int,
    val context: String,
    val createdDate: String,
    val modifiedDate: String,
    val type: Int
){
    override fun toString(): String {
        return this.context
    }
}