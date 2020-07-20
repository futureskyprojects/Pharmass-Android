package vn.vistark.pharmass.component.goods_inventory_limit_update

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_address_picker.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.databinding.ActivityGoodsInventoryLimitUpdateBinding
import vn.vistark.pharmass.processing.CreateOrUpdatePharmacyGoodsInCategoryProcessing

class GoodsInventoryLimitUpdateActivity : AppCompatActivity() {

    lateinit var binding: ActivityGoodsInventoryLimitUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_goods_inventory_limit_update)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_goods_inventory_limit_update)
        setResult(Activity.RESULT_CANCELED)

        if (readData()) {
            inits()
            initEvents()
        } else {
            Toast.makeText(
                this,
                "Có lỗi trong quá trình truyền dữ liệu, vui lòng thử lại",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun readData(): Boolean {
        val goodsJson = intent.getStringExtra(Goods::class.java.simpleName) ?: ""
        val goods = Gson().fromJson(goodsJson, Goods::class.java)
        if (goodsJson.isNotEmpty() && goods != null) {
            binding.goods = goods
            return true
        } else {
            return false
        }
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }

        btnConfirm.setOnClickListener {
            CreateOrUpdatePharmacyGoodsInCategoryProcessing(this, binding.goods!!)
                .onFinished = { goods ->
                if (goods != null) {
                    val dataIntent = Intent()
                    dataIntent.putExtra(
                        Goods::class.java.simpleName,
                        Gson().toJson(goods)
                    )
                    setResult(Activity.RESULT_OK, dataIntent)
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)
    }

}