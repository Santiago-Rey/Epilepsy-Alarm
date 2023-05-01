package com.sr.epilepsyalarm.data.repository

import android.content.Context
import android.telephony.SmsManager
import com.sr.configuration.data.SharedPrefsRepositoryImpl
import com.sr.configuration.domain.UserUseCase
import com.sr.epilepsyalarm.data.MessageApi
import com.sr.epilepsyalarm.data.MessageEntity
import com.sr.configuration.data.SharedPreferenceDataSourceImpl
import com.sr.epilepsyalarm.data.Text
import com.sr.configuration.domain.PreferenceUseCase
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
                    .header("Authorization", "Bearer EAAKt4SA1Vy0BALegWXgFglHpYGNEQgBLsVVFqdX7WSRXZBZBL9uzKopZCzKKdOJXEaHLcwh4JY0gNpVbh8GKGG6EeblsvDwOgSkpCURmtGVBLe5ooj8ZCPvNZAtSsfoOa4ktxu4O65Dz51jCamlWk0lclaRF6sY6c1FQ8Qh3L09MZBviHuC8ZBeWW6ZB4ElHvOZCyWuSF5t8XBdtJWDGOsl3f")
                    .method(original.method(), original.body())
                    .build()

                chain.proceed(request)
            }
            .build()


        return Retrofit.Builder()
            .baseUrl("https://graph.facebook.com/v16.0/108029642271423/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private suspend fun getMessage(): String {
        return UserUseCase().getUser()?.messageAlert?: "Mensaje de alerta"
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