package com.sr.configuration.data

import android.content.Context
import android.content.SharedPreferences
import com.sr.configuration.util.Constants.sharedPreferenceName


class SharedPreferenceDataSourceImpl(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun saveDouble(key: String, value: Double) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }

    fun getDouble(key: String, defaultValue: Double): Double {
        return sharedPreferences.getString(key, defaultValue.toString())?.toDouble() ?: 0.0
    }

    fun saveInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

}