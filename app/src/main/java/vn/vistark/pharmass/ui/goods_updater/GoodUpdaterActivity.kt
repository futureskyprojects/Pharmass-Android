package vn.vistark.pharmass.ui.goods_updater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.request.BodyCreatePharmacyRequest
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.databinding.ActivityGoodUpdaterBinding

class GoodUpdaterActivity : AppCompatActivity() {

    lateinit var binding: ActivityGoodUpdaterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_good_updater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_good_updater)
        binding.requestCreateGoods = Goods()
    }
}