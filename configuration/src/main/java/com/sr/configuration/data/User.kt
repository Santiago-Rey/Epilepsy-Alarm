package com.sr.configuration.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: Int,
    val name: String,
    val lastName: String,
    val blood: String,
    val documentType : String,
    val userId: String,
    val nameEmergency: String,
    val countryNumber : String,
    val numberEmergency: String,
    val messageAlert: String,
    val messageInstruction:String
)
