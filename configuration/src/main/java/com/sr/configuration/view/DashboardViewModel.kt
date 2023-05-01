package com.sr.configuration.view

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sr.configuration.data.SharedPreferenceDataSourceImpl
import com.sr.configuration.data.SharedPrefsRepositoryImpl
import com.sr.configuration.domain.PreferenceUseCase
import com.sr.configuration.domain.UserModel
import com.sr.configuration.domain.UserUseCase
import com.sr.configuration.util.Constants.keySound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel : ViewModel() {

    var messageAlert : MutableLiveData<String> = MutableLiveData("")
    var user : MutableLiveData<UserModel> = MutableLiveData()
    val userUseCase = UserUseCase()



    fun getMessageAlert() {
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                user.value = userUseCase.getUser()
                messageAlert.value = user.value?.messageAlert?:""
            }
        }
    }

    fun setMessageAlert(message: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                user.value?.messageAlert = message
                user.value?.let { userUseCase.createUser(it)}
            }
        }
    }

    fun getAlarm(context: Context) : Int{
        val sharedPrefsRepository = SharedPrefsRepositoryImpl(SharedPreferenceDataSourceImpl(context))
        val preferenceUseCase = PreferenceUseCase(sharedPrefsRepository)
        return preferenceUseCase.getPreference(keySound, com.sr.configuration.R.raw.alarm_one.toDouble()).toInt()
    }
}