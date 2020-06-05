package vn.vistark.pharmass.ui.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        initEvents()
    }

    private fun initEvents() {
        civUserAvatar.visibility = View.GONE
        ivExpandAuthorInfomation.visibility = View.GONE
        ivBackButton.visibility = View.VISIBLE
        tvToolbarLabel.visibility = View.GONE
//        tvToolbarLabel.isSelected = true
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}