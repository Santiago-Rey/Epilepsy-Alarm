package com.sr.epilepsyalarm.view

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sr.configuration.data.SharedPreferenceDataSourceImpl
import com.sr.configuration.domain.PreferenceUseCase
import com.sr.configuration.data.SharedPrefsRepositoryImpl
import com.sr.configuration.domain.UserModel
import com.sr.epilepsyalarm.data.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {


    var isFirstLogin : MutableLiveData<Boolean> = MutableLiveData(false)


    fun saveBoolean(key: String, value: Boolean,context:Context) {
         val sharedPrefsRepository = SharedPrefsRepositoryImpl(SharedPreferenceDataSourceImpl(context))
         val preferenceUseCase = PreferenceUseCase(sharedPrefsRepository)
        preferenceUseCase.savePreference(key, value)
    }

    fun getBoolean(key: String, value:Boolean,context:Context): Boolean{
        val sharedPrefsRepository = SharedPrefsRepositoryImpl(SharedPreferenceDataSourceImpl(context))
        val preferenceUseCase = PreferenceUseCase(sharedPrefsRepository)
        return preferenceUseCase.getPreference(key, value)
    }

    fun readConfiguration(context:Context){
      isFirstLogin.value = getBoolean("first_login_", false,context)
    }

    fun saveDouble(key: String, value: Double, context:Context){
        val sharedPrefsRepository = SharedPrefsRepositoryImpl(SharedPreferenceDataSourceImpl(context))
        val preferenceUseCase = PreferenceUseCase(sharedPrefsRepository)
        preferenceUseCase.savePreference(key, value)
    }

    fun sendInitMessage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val messageRepository = MessageRepository()
                messageRepository.sendInitMessage()
            }
        }
    }





}