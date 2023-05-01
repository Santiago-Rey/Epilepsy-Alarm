package com.sr.configuration.domain


data class UserModel(
    val id: Int,
    val name: String,
    val lastName: String,
    val blood: String,
    val userId: String,
    val nameEmergency: String,
    val numberEmergency: String,
    var messageAlert: String = "")
