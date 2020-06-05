package vn.vistark.pharmass.core.api

class APIUtils {
    companion object {
        val mAPIServices: APIServices?
            get() {
                return RetrofitClient.getClient()?.create(APIServices::class.java)
            }
    }
}