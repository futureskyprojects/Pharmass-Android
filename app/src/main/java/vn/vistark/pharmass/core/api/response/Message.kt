package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("messages")
    var messages: List<MessageX> = listOf()
)