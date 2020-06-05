package vn.vistark.pharmass.ui.update_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.components_search_bar.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R

class UpdateProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        inits()
        initEvents()
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun inits() {
        ivExpandAuthorInfomation.visibility = View.GONE
        ivBackButton.visibility = View.VISIBLE
        tvToolbarLabel.text = "Cập nhật hồ sơ"
        tvToolbarLabel.setPadding(
            tvToolbarLabel.paddingLeft + 25,
            tvToolbarLabel.paddingTop,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom
        )
        civUserAvatar.visibility = View.INVISIBLE
    }
}