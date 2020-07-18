package vn.vistark.pharmass.core.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import vn.vistark.pharmass.core.api.request.BodyCreatePharmacyRequest
import vn.vistark.pharmass.core.api.request.BodyLoginRequest
import vn.vistark.pharmass.core.api.request.BodyRegisterRequest
import vn.vistark.pharmass.core.api.request.BodyUpdateProfileRequest
import vn.vistark.pharmass.core.api.response.BodyAuthenticationResponse
import vn.vistark.pharmass.core.api.response.BodyUploadFileResponse
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.model.*


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

    @GET("/goods-categories?pharmacy_null=true")
    fun getDefaultGoodsCategory(): Call<List<GoodsCategory>>

    @GET("/goods-categories?pharmacy_null=false")
    fun getPharmacyGoodsCategory(@Query("pharmacy.id") id: Int): Call<List<GoodsCategory>>

    @GET("/goods/count")
    fun getCountPharmacyGoodsInCategory(
        @Query("goods_category.id") goodsCategoryId: Int,
        @Query("pharmacy.id") pharmacyId: Int
    ): Call<Int>

    @GET("/goods")
    fun getPharmacyGoods(
        @Query("pharmacy.id") pharmacyId: Int
    ): Call<List<Goods>>

    @GET("/goods")
    fun getGoodsById(
        @Query("id") id: Int
    ): Call<List<Goods>>

    @GET("/goods")
    fun getPharmacyGoodsInCategory(
        @Query("goods_category.id") goodsCategoryId: Int,
        @Query("pharmacy.id") pharmacyId: Int
    ): Call<List<Goods>>


    @GET("/goods")
    fun getPharmacyGoodsByName(
        @Query("pharmacy.id") pharmacyId: Int,
        @Query("name_contains") name: String
    ): Call<List<Goods>>


    @POST("/goods")
    fun createPharmacyGoodsInCategory(@Body goods: Goods): Call<Goods>


    // Chỉ dành cho mục đích phát triển
    @POST("/medicine-categories(BoQua)")
    fun createMedicineCategory(@Body medicineCategory: MedicineCategory): Call<MedicineCategory>
    //=====================================================

    @GET("/medicine-categories?_limit=-1")
    fun findMedicineCategoryByName(@Query("name_contains") name: String): Call<List<MedicineCategory>>

    @GET("/suppliers?_limit=-1")
    fun findSupplierByName(@Query("name_contains") name: String): Call<List<Supplier>>

    @GET("/users?_limit=30")
    fun findUserByPhoneNumber(@Query("phoneNumber_contains") phone: String): Call<List<User>>

    @POST("/bill-items")
    fun createBillItem(@Body simpleBillItem: SimpleBillItem): Call<BillItem>

    @PUT("/bill-items")
    fun updateBillItem(@Path("id") id: Int, @Body simpleBillItem: SimpleBillItem): Call<BillItem>

    @GET("/bill-items")
    fun getBillItemById(@Query("id") id: Int): Call<List<SimpleBillItem>>

    @POST("/bills")
    fun createBill(@Body bill: Bill): Call<Bill>

    @GET("/bills")
    fun getBillById(@Query("id") id: Int): Call<List<Bill>>
}