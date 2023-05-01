package com.sr.configuration.data

import com.sr.configuration.domain.SharedPrefsRepository

class SharedPrefsRepositoryImpl(private val sharedPreferenceDataSource: SharedPreferenceDataSourceImpl) :
    SharedPrefsRepository {

    override fun saveBoolean(key: String, value: Boolean) {
        sharedPreferenceDataSource.saveBoolean(key, value)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferenceDataSource.getBoolean(key, defaultValue)
    }

    override fun saveDouble(key: String, value: Double) {
        sharedPreferenceDataSource.saveDouble(key, value)
    }

    override fun getDouble(key: String, value: Double): Double {
        return sharedPreferenceDataSource.getDouble(key, value)
    }
}