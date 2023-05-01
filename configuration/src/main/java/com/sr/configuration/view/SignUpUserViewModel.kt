package com.sr.configuration.view
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.sr.configuration.domain.UserModel
import com.sr.configuration.domain.UserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpUserViewModel: ViewModel() {

    private val _user = MutableLiveData<UserModel>()
    val user = _user as LiveData<UserModel>
    val userUseCase = UserUseCase()
    fun saveUser(
        etName: String,
        etLastName: String,
        etRh: String,
        etNumId: String,
        etNameContact: String,
        etPhoneContact: String
    ) {
        _user.value = UserModel(1, etName, etLastName, etRh, etNumId, etNameContact, etPhoneContact)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _user.value?.let { userUseCase.createUser(it) }
            }
        }
    }

    fun obtainUser() {
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                _user.value = userUseCase.getUser()
            }
        }
    }

}