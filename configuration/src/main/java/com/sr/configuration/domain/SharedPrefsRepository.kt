package com.sr.configuration.domain

interface SharedPrefsRepository {

    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun saveDouble(key: String, value: Double)
    fun getDouble(key: String, value: Double): Double
    fun saveInt(key: String, value: Int)
    fun getInt(key: String, value: Int): Int

}