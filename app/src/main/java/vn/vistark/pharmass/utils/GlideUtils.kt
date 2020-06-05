package vn.vistark.pharmass.utils

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R

class GlideUtils {
    companion object {
        fun loadWebp(iv: ImageView, webpResId: Int, placeHolderImageResId: Int) {
            val circleCrop: Transformation<Bitmap> = CircleCrop()
            val options = RequestOptions()
                .centerCrop()
                .placeholder(placeHolderImageResId)
                .error(placeHolderImageResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
            Glide.with(iv.context)
                .load(webpResId)
                .optionalTransform(circleCrop)
                .optionalTransform(WebpDrawable::class.java, WebpDrawableTransformation(circleCrop))
                .apply(options)
                .into(iv)
        }

        fun loadToImageViewWithPlaceHolder(
            iv: ImageView,
            imageAddress: String,
            placeHolderImageResId: Int
        ) {
            val options = RequestOptions()
                .centerCrop()
                .placeholder(placeHolderImageResId)
                .error(placeHolderImageResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
            try {
                Glide.with(iv.context).load(imageAddress).apply(options).into(iv)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun loadToImageViewWithPlaceHolder(
            iv: ImageView,
            imageUri: Uri,
            placeHolderImageResId: Int
        ) {
            val options = RequestOptions()
                .centerCrop()
                .placeholder(placeHolderImageResId)
                .error(placeHolderImageResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
            Glide.with(iv.context).load(imageUri).apply(options).into(iv)
        }
    }
}