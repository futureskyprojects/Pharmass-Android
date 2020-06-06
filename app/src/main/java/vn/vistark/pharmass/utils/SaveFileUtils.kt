package vn.vistark.pharmass.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class SaveFileUtils {
    companion object {
        fun saveImages(context: Context, fileName: String, bitmap: Bitmap): File? {
            val path = "${context.externalCacheDir}${File.separator}${fileName}"
            val f = File(path)
            try {
                if (!f.exists()) {
                    f.delete()
                }
                val fo = FileOutputStream(f)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fo)
                fo.flush()
                fo.close()
                return f
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
}