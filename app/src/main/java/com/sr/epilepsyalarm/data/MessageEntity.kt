package com.sr.epilepsyalarm.data

import com.google.gson.annotations.SerializedName

data class MessageEntity(@SerializedName("messaging_product") val messagingProduct: String,
                   @SerializedName("recipient_type") val recipientType: String? = null,
                   val to: String,
                   val type: String? = null,
                   val text: Text? = null,
                   val template: Template? = null)

data class Text(
    @SerializedName("preview_url") val previewUrl: Boolean? = null,
    val body: String
)

data class Template(
    val name: String,
    val language: Language
)

data class Language(
    val code: String
)



