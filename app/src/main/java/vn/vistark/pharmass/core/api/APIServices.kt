package vn.vistark.pharmass.core.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import vn.vistark.pharmass.core.api.request.BodyCreatePharmacyRequest
import vn.vistark.pharmass.core.api.request.BodyLoginRequest
import vn.vistark.pharmass.core.api.request.BodyRegisterRequest
import vn.vistark.pharmass.core.api.request.BodyUpdateProfileRequest
import vn.vistark.pharmass.core.api.response.BodyAuthenticationResponse
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.core.api.response.BodyUploadFileResponse
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.model.PharmacyStaff
import vn.vistark.pharmass.core.model.User


public interface APIServices {
    @POST("/auth/local/register")
    fun register(@Body bodyRegisterRequest: BodyRegisterRequest): Call<BodyAuthenticationResponse>

    @POST("/auth/local")
    fun login(@Body bodyLoginRequest: BodyLoginRequest): Call<BodyAuthenticationResponse>

    @GET("/users/me")
    fun getUserSelft(): Call<User>

    @PUT("/users/{id}")
    fun updateUserSelft(
        @Body bodyUpdateProfileRequest: BodyUpdateProfileRequest,
        @Path("id") id: Int = Constants.user.id
    ): Call<User>

    @POST("/upload")
    @Multipart
    fun uploadFile(
        @Part
        files: MultipartBody.Part
    ): Call<BodyUploadFileResponse>

    @POST("/pharmacies")
    fun createPharmacy(@Body bodyCreatePharmacyRequest: BodyCreatePharmacyRequest): Call<Pharmacy>

    @GET("/pharmacies")
    fun getPharmacyOfUser(@Query("owner.id") id: Int = Constants.user.id): Call<List<Pharmacy>>

    @GET("/pharmacy-staffs")
    fun getPharmacyStaffs(@Query("pharmacy.id") id: Int): Call<List<PharmacyStaff>>
}