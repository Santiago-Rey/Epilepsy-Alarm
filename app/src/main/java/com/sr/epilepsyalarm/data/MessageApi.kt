package com.sr.epilepsyalarm.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MessageApi {

    @Headers("Content-Type: application/json")
    @POST("messages")
    suspend fun postMessage(@Body message: MessageEntity) : Response<MessageData>

}