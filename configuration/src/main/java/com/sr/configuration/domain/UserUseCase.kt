package com.sr.configuration.domain

import android.content.Context
import com.sr.configuration.data.User
import com.sr.configuration.data.config.UserApp

class UserUseCase {

    suspend fun createUser(userModel: UserModel){
        userModel.run {
            UserApp.db.userDao().insert( User(1, name, lastName, blood, documentType, userId, nameEmergency, countryNumber, numberEmergency, messageAlert, messageInstruction))
        }
    }

    suspend fun getUser(): UserModel? {
       val user = UserApp.db.userDao().getUser(1)
        return user?.run { UserModel(1, name, lastName, blood, documentType, userId, nameEmergency, countryNumber, numberEmergency, messageAlert, messageInstruction) }
    }

}