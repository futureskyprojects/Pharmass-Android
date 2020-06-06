package vn.vistark.pharmass.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.components_home_menu.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.ui.about.AboutActivity
import vn.vistark.pharmass.ui.home_menu.HomeMenuActivity
import vn.vistark.pharmass.utils.DaySessionWelcome
import vn.vistark.pharmass.utils.GlideUtils

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        inits()
        initEvents()
    }

    private fun initEvents() {
        ivExpandAuthorInfomation.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        civUserAvatar.setOnClickListener {
            val intent = Intent(this, HomeMenuActivity::class.java)
            startActivity(intent)
            this.overridePendingTransition(0, 300);
        }
    }

    private fun inits() {
        GlideUtils.loadWebp(ivExpandAuthorInfomation, R.raw.idle, R.drawable.logo_transparent_black)
        GlideUtils.loadToImageViewWithPlaceHolder(
            civUserAvatar,
            Constants.user.getAvatarFullAddress(),
            R.drawable.no_avatar
        )
    }

}