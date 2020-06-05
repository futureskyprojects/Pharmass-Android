package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("messages")
    var messages: List<MessageXX> = listOf()
)