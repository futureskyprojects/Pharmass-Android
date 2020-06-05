package vn.vistark.pharmass.core.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.vistark.pharmass.core.constants.Constants
import java.util.concurrent.TimeUnit


class RetrofitClient {
    companion object {
        const val BASE_URL = "http://139.180.190.56:1337"

        private var retrofit: Retrofit? = null
        fun getClient(): Retrofit? {

            retrofit = Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                if (Constants.token.length > 30) {
                    client(OkHttpClient.Builder().apply {
                        addInterceptor { chain ->
                            val request = chain.request().newBuilder().addHeader(
                                "Authorization",
                                "Bearer ${Constants.token}"
                            ).build()
                            chain.proceed(request)
                        }
                        connectTimeout(60, TimeUnit.SECONDS)
                        readTimeout(60, TimeUnit.SECONDS)
                    }.build())
                }
            }.build()
            return retrofit
        }
    }
}