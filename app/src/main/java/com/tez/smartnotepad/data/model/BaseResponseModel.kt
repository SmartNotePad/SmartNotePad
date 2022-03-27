package com.tez.smartnotepad.data.model

data class BaseResponseModel<T> (
    val success: Boolean,
    val message: String?,
    val data: T
)
