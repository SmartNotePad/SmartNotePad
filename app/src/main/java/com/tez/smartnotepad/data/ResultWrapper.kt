package com.tez.smartnotepad.data

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val value: String) : ResultWrapper<Nothing>()
}