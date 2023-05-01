package com.sr.epilepsyalarm.data

import com.google.gson.annotations.SerializedName

data class MessageData(
    @SerializedName("messaging_product") val messagingProduct: String,
    val contacts: List<Contact>,
    val messages: List<MessageId>
)

data class Contact(
    val input: String,
    @SerializedName("wa_id") val waId: String
)

data class MessageId(
    val id: String
)
