package com.sr.configuration.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: Int,
    val name: String,
    val lastName: String,
    val blood: String,
    val documentType: String,
    val userId: String,
    val nameEmergency: String,
    val numberEmergency: String,
    var messageAlert: String = "",
    var messageInstruction : String = "") : Parcelable
