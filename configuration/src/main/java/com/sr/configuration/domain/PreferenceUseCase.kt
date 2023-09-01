package com.sr.configuration.domain

import com.sr.configuration.domain.SharedPrefsRepository

class PreferenceUseCase (private val sharedPrefsRepository: SharedPrefsRepository) {

    fun savePreference(key: String, value: Double) {
        sharedPrefsRepository.saveDouble(key, value)
    }

    fun getPreference(key: String, value: Boolean): Boolean{
        return sharedPrefsRepository.getBoolean(key, value)

    }

    fun savePreference(key: String, value: Boolean) {
        sharedPrefsRepository.saveBoolean(key, value)
    }

    fun getPreference(key: String, value: Double): Double{
        return sharedPrefsRepository.getDouble(key, value)

    }

    fun savePreference(key: String, value: Int) {
        sharedPrefsRepository.saveInt(key, value)
    }

    fun getPreference(key: String, value: Int): Int{
        return sharedPrefsRepository.getInt(key, value)

    }
}