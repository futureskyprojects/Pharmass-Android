package vn.vistark.pharmass.ui.about

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.ui.create_medicine_category.AddMedicineCategoryActivity

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

        // For develop
        ivHiddenInitMedicineCategoryButon.setOnClickListener {
            val intent = Intent(this, AddMedicineCategoryActivity::class.java)
            startActivity(intent)
        }
    }
}