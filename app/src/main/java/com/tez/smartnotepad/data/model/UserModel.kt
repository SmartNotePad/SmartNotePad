package com.tez.smartnotepad.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel (
    val userId: String,
    val mail: String,
    val password: String?,
    val nameSurname: String? // userDtoList içinde dönsün
)