package vn.vistark.pharmass.component.pharmacy_options_picker

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_pharmacy_options_picker.*
import vn.vistark.pharmass.R

class PharmacyOptionsPickerActivity : AppCompatActivity() {
    companion object {
        val EDIT = "EDIT"
        val REMOVE = "REMOVE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_options_picker)
        setResult(Activity.RESULT_CANCELED)
        inits()
        initEvents()
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
        btnEditPharmacy.setOnClickListener {
            val dataIntent = Intent()
            dataIntent.putExtra(
                PharmacyOptionsPickerActivity::class.java.simpleName,
                EDIT
            )
            setResult(Activity.RESULT_OK, dataIntent)
            finish()
        }

        btnRemovePharmacy.setOnClickListener {
            val dataIntent = Intent()
            dataIntent.putExtra(
                PharmacyOptionsPickerActivity::class.java.simpleName,
                REMOVE
            )
            setResult(Activity.RESULT_OK, dataIntent)
            finish()
        }
    }
}