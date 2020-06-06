package vn.vistark.pharmass.processing

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.provider.MediaStore
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import vn.vistark.pharmass.core.api.APIUtils
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.utils.DialogNotify
import vn.vistark.pharmass.utils.SaveFileUtils


class UserUploadImageProcessing(val context: Context, val fUri: Uri) :
    AsyncTask<Void, Int, String?>() {
    var onFinished: ((String?) -> Unit)? = null
    val loading = DialogNotify.loading(context)

    companion object {
        fun sync(context: Context, fUri: Uri): String? {
            val bitmap =
                if (Build.VERSION.SDK_INT >= 29) {
                    // To handle deprication use
                    val source =
                        ImageDecoder.createSource(context.contentResolver, fUri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    // Use older version
                    MediaStore.Images.Media.getBitmap(context.contentResolver, fUri)
                }
            val f = SaveFileUtils.saveImages(
                context,
                "${System.currentTimeMillis()}.jpg",
                bitmap
            ) ?: return null
            if (!f.exists()) {
                println("Tệp tin không tồn tại")
                return null
            }
            APIUtils.mAPIServices?.uploadFile(
                MultipartBody.Part.createFormData(
                    "files",
                    f.name,
                    RequestBody.create(
                        MediaType.parse(context.contentResolver.getType(fUri)!!),
                        f
                    )
                )
            )?.execute().apply {
                if (this != null && this.isSuccessful) {
                    f.deleteOnExit()
                    return this.body()?.firstOrNull()?.url
                }
                println(this?.message())
                println(Gson().toJson(this?.errorBody()))
            }
            return null
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        loading.show("Đang gửi ảnh lên máy chủ")
    }

    override fun doInBackground(vararg params: Void?): String? {
        return sync(
            context,
            fUri
        )
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        loading.close()
        onFinished?.invoke(result)
    }

}