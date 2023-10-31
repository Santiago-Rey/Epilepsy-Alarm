package com.sr.epilepsyalarm.data.repository

import android.content.Context
import android.telephony.SmsManager
import com.sr.configuration.data.SharedPrefsRepositoryImpl
import com.sr.configuration.domain.UserUseCase
import com.sr.configuration.data.SharedPreferenceDataSourceImpl
import com.sr.configuration.domain.PreferenceUseCase
import com.sr.configuration.domain.UserModel
import com.sr.configuration.util.Constants.keyPulse
import com.sr.epilepsyalarm.data.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MessageRepository {

    suspend fun sendMessage(context: Context) {
        getRetrofit().create(MessageApi::class.java).postMessage(
            MessageEntity("whatsapp",
                "individual",
                "57${getPhone()}",
                "text",
                Text(true, getMessage() + " \n Mi ubicación es esta : " + getLocation(
                    context
                ))
            )
        )
    }

    suspend fun sendInitMessage() {
        getRetrofit().create(MessageApi::class.java).postMessage(
            MessageEntity("whatsapp",
                null,
                "57${getPhone()}",
                "template",
                null,
                Template("hello_world", Language("en_US"))
            )
        )
    }


    suspend fun sendSMS(context: Context) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(getPhone(), null, getMessage() + "\n Mi ultima ubicacion es esta: " + getLocation(context), null, null)
    }

    private fun getRetrofit(): Retrofit {
        // Crear un objeto OkHttpClient con un Interceptor que agrega el encabezado Authorization
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()

                val request = original.newBuilder()
                    .header("Authorization", "Bearer EAAKt4SA1Vy0BO2yD2au21FdDMbLlbdm7C4LwzJTAroZCxaVbB9DMQRrkgyB6vk5Qbv6abZBwjh9JCB5gw2ayvZCj9eXZCmEMZAywjqhEIlDJwqbFDXYrs2YuIWQfwkgOXmQ3GgJmKZAIRDVXwKEdnYkIRiyRgQlOVkLwZBcKaLskucSe5llCN5oLV2Wj51YS5Ox")
                    .method(original.method(), original.body())
                    .build()

                chain.proceed(request)
            }
            .build()


        return Retrofit.Builder()
            .baseUrl("https://graph.facebook.com/v16.0/107212639037071/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private suspend fun getMessage(): String {
        return UserUseCase().getUser()?.messageAlert?: "Mensaje de alerta"
    }

    suspend fun getUser(): UserModel {
        return UserUseCase().getUser()!!
    }

    private fun getLocation(context: Context): String {

        val sharedPrefsRepository = SharedPrefsRepositoryImpl(SharedPreferenceDataSourceImpl(context))
        val preferenceUseCase = PreferenceUseCase(sharedPrefsRepository)
        val latitude = preferenceUseCase.getPreference("latitude", 0.0)
        val longitude = preferenceUseCase.getPreference("longitude", 0.0)


        return "https://www.google.com/maps/place/$latitude,$longitude"

    }

    fun saveLocation(context: Context, location: Pair<Double, Double>) {

        val sharedPrefsRepository = SharedPrefsRepositoryImpl(SharedPreferenceDataSourceImpl(context))
        val preferenceUseCase = PreferenceUseCase(sharedPrefsRepository)
        preferenceUseCase.savePreference("latitude", location.first)
        preferenceUseCase.savePreference("longitude", location.second)

    }

    fun getPulseCount(context: Context): Int {
        val sharedPrefsRepository = SharedPrefsRepositoryImpl(SharedPreferenceDataSourceImpl(context))
        val preferenceUseCase = PreferenceUseCase(sharedPrefsRepository)
        return preferenceUseCase.getPreference(keyPulse, 2)

    }

    private suspend fun getPhone(): String {
        return UserUseCase().getUser()?.numberEmergency?: "000000"
    }


}