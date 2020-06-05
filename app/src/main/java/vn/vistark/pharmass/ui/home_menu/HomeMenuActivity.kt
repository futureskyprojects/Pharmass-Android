package vn.vistark.pharmass.ui.home_menu

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_home_menu.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.ui.update_profile.UpdateProfileActivity
import vn.vistark.pharmass.ui.work.WorkActivity

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
//        rlHomeMenuRoot.postDelayed({
//            rlHomeMenuRoot.setBackgroundColor(Color.parseColor("#B2000000"))
//        }, 500)
    }

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
    }
}