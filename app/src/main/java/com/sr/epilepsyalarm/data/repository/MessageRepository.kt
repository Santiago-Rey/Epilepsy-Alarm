package com.sr.epilepsyalarm.data.repository

import android.content.Context
import android.telephony.SmsManager
import com.sr.configuration.data.SharedPrefsRepositoryImpl
import com.sr.configuration.domain.UserUseCase
import com.sr.configuration.data.SharedPreferenceDataSourceImpl
import com.sr.configuration.domain.PreferenceUseCase
import com.sr.configuration.domain.UserModel
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
                Text(true, getMessage() + " \n mi ubicación es esta : " + getLocation(context))
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
        smsManager.sendTextMessage(getPhone(), null, getMessage() + "\n Mi ubicacion es esta: " + getLocation(context), null, null)
    }

    private fun getRetrofit(): Retrofit {
        // Crear un objeto OkHttpClient con un Interceptor que agrega el encabezado Authorization
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()

                val request = original.newBuilder()
                    .header("Authorization", "Bearer EAAKt4SA1Vy0BAAZCbxpLP2mGujGmZAVPvbogXI5NPFDhpQZAX723O0uZBmXrZAqtAwybZCxlTZBYvKCJMMcaPOflUQQ7ipVpOae9EpiVPL992Cw6CxrDPtJ2nZAfMH4V52lwcFBraWBS49akVKa8ZAyZC2ZByLyDw3k9WyXfWvn87VL4zSlPr7ZAyJUe")
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
        val latitude = preferenceUseCase.getPreference("latitude", 0.0).toString()
        val longitude = preferenceUseCase.getPreference("longitude", 0.0).toString()

        return "https://www.google.com/maps/place/$latitude,$longitude"

    }

    private suspend fun getPhone(): String {
        return UserUseCase().getUser()?.numberEmergency?: "000000"
    }


}