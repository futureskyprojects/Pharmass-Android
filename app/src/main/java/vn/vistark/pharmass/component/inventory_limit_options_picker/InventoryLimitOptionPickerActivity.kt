package vn.vistark.pharmass.component.inventory_limit_options_picker

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_inventory_limit_option_picker.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit.InventoryLimitFragment

class InventoryLimitOptionPickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_limit_option_picker)
        inits()
        initEvents()
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
        btnOutOfStock.setOnClickListener {
            InventoryLimitFragment.currentFilter = InventoryLimitFragment.OUT_OF_STOCK
            InventoryLimitFragment.reload()
            finish()
        }
        btnUnderLimit.setOnClickListener {
            InventoryLimitFragment.currentFilter = InventoryLimitFragment.UNDER_LIMIT
            InventoryLimitFragment.reload()
            finish()
        }
        btnAboveLimit.setOnClickListener {
            InventoryLimitFragment.currentFilter = InventoryLimitFragment.ABOVE_LIMIT
            InventoryLimitFragment.reload()
            finish()
        }
        btnNormal.setOnClickListener {
            InventoryLimitFragment.currentFilter = InventoryLimitFragment.NORMAL
            InventoryLimitFragment.reload()
            finish()
        }
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)
    }
}