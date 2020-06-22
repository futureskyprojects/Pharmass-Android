package vn.vistark.pharmass.ui.goods_updater

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.databinding.ActivityGoodUpdaterBinding

class GoodsUpdaterActivity : AppCompatActivity() {
    lateinit var binding: ActivityGoodUpdaterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_good_updater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_good_updater)
        binding.requestCreateGoods = Goods()
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
        tvToolbarLabel.text = "Cập nhật sản phẩm"
        tvToolbarLabel.setPadding(
            tvToolbarLabel.paddingLeft,
            tvToolbarLabel.paddingTop,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom
        )
        civUserAvatar.visibility = View.GONE
    }
}