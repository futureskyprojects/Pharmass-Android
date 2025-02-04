package vn.vistark.pharmass.component.bill_item_updater

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bill_item_updater.*
import kotlinx.android.synthetic.main.activity_goods_picker.lnMenuContainer
import kotlinx.android.synthetic.main.activity_goods_picker.rlHomeMenuRoot
import kotlinx.android.synthetic.main.component_pharmacy_goods_item.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.component.goods_detail.GoodsDetailActivity
import vn.vistark.pharmass.core.model.SimpleBillItem
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.databinding.ActivityBillItemUpdaterBinding
import vn.vistark.pharmass.utils.DialogNotify
import vn.vistark.pharmass.utils.GlideUtils
import vn.vistark.pharmass.utils.NumberUtils

class BillItemUpdaterActivity : AppCompatActivity() {

    lateinit var goods: Goods

    lateinit var binding: ActivityBillItemUpdaterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bill_item_updater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_item_updater)
        binding.billItem = SimpleBillItem()

        setResult(Activity.RESULT_CANCELED)
        if (getPassingData()) {
            inits()
            initEvents()
        } else {
            Toast.makeText(
                this,
                "Không nhận diện được sản sẩm hiện đang thao tác",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)
        bind(goods)
    }

    private fun getPassingData(): Boolean {
        // Tiến hành lấy thông tin sản phẩm được truyền vào
        val goodsJson = intent.getStringExtra(Goods::class.java.simpleName) ?: ""
        goods = Gson().fromJson(goodsJson, Goods::class.java)
        if (goodsJson.isEmpty()) {
            Toast.makeText(
                this,
                "Không lấy được thông tin sản phẩm truyền vào",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return false
        }
        return true
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
        btnConfirm.setOnClickListener {
            if (binding.billItem!!.dosage <= 0) {
                DialogNotify.missingInput(this, "Số lượng không phù hợp")
            } else if (binding.billItem!!.dosage > goods.amount) {
                DialogNotify.missingInput(
                    this,
                    "Số lượng trong kho không đáp ứng đủ, vui lòng điều chỉnh lại"
                )
            } else if (binding.billItem!!.direction.length < 10) {
                DialogNotify.missingInput(
                    this,
                    "Vui lòng hướng dẫn người mua sử dụng một cách rõ ràng"
                )
            } else {
                binding.billItem!!.goods = goods.id
                binding.billItem!!.tempGoods = goods
                val intent = Intent()
                intent.putExtra(
                    SimpleBillItem::class.java.simpleName,
                    Gson().toJson(binding.billItem!!)
                )
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    fun bind(goods: Goods) {
        rlRoot.setOnClickListener {
            GoodsDetailActivity.start(this, goods.id, true)
        }
        tvGoodsName.text = goods.name
        tvGoodsName.isSelected = true

        GlideUtils.loadToImageViewWithPlaceHolder(
            ivGoodsImage,
            goods.getImageRandom(),
            R.drawable.no_image
        )

        val unit = if (goods.medicineCategory == null) goods.unit else goods.medicineCategory?.unit

        tvAmount.text =
            "Số lượng: ${NumberUtils.removeUnMean(goods.amount.toDouble())} ($unit)"
        if (goods.manufacturerCountry.isNotEmpty()) {
            tvLimit.text =
                goods.manufacturerCountry
            tvLimit.isSelected = true
        } else {
            tvLimit.visibility = View.GONE
        }
    }
}