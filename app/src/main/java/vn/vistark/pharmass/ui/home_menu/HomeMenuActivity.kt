package vn.vistark.pharmass.ui.home_menu

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_home_menu.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.ui.login.LoginActivity
import vn.vistark.pharmass.ui.update_profile.UpdateProfileActivity
import vn.vistark.pharmass.ui.work.WorkActivity
import vn.vistark.pharmass.utils.GlideUtils

class HomeMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_menu)
        inits()
        initEvents()
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)
        GlideUtils.loadToImageViewWithPlaceHolder(
            civUserAvatar,
            Constants.user.getAvatarFullAddress(),
            R.drawable.no_avatar
        )
    }

    @SuppressLint("DefaultLocale")
    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
        rlBtnWork.setOnClickListener {
            val intent = Intent(this, WorkActivity::class.java)
            startActivity(intent)
            finish()
        }
        rlBtnUpdateProfile.setOnClickListener {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnLogOut.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).apply {
                titleText = "Bạn thực sự muốn đăng xuất khỏi tài khoản hiện tại?"
                contentText = "Đăng xuất".toUpperCase()
                setConfirmButton("Thoát") {
                    it.dismiss()
                    Constants.token = ""
                    val intent = Intent(this@HomeMenuActivity, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                setCancelButton("Không") {
                    it.dismissWithAnimation()
                }
                show()
            }
        }
    }
}