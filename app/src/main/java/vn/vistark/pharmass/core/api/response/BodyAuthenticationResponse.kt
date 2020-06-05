package vn.vistark.pharmass.core.api.response


import com.google.gson.annotations.SerializedName
import vn.vistark.pharmass.core.model.User

data class BodyAuthenticationResponse(
    @SerializedName("jwt")
    var jwt: String = "",
    @SerializedName("user")
    var user: User = User()
)