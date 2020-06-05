package vn.vistark.pharmass.core.api

import retrofit2.Call
import retrofit2.http.*
import vn.vistark.pharmass.core.api.request.BodyLoginRequest
import vn.vistark.pharmass.core.api.request.BodyRegisterRequest
import vn.vistark.pharmass.core.api.response.BodyAuthenticationResponse
import vn.vistark.pharmass.core.model.User


public interface APIServices {
    @POST("/auth/local/register")
    fun register(@Body bodyRegisterRequest: BodyRegisterRequest): Call<BodyAuthenticationResponse>

    @POST("/auth/local")
    fun login(@Body bodyLoginRequest: BodyLoginRequest): Call<BodyAuthenticationResponse>

    @GET("/users/me")
    fun getUserSelft(): Call<User>
}