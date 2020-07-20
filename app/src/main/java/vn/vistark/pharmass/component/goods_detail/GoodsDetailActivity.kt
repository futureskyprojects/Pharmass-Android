package vn.vistark.pharmass.component.goods_detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_goods_detail.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.databinding.ActivityGoodsDetailBinding
import vn.vistark.pharmass.processing.GetGoodsByIdProcessing
import vn.vistark.pharmass.utils.GlideUtils

class GoodsDetailActivity : AppCompatActivity() {

    companion object {
        private const val IS_FULL_INFO = "IS_FULL_INFO"
        fun start(
            context: Context,
            goodsId: Int,
            isShowFullInfo: Boolean = false
        ) {
            val intent = Intent(context, GoodsDetailActivity::class.java)
            intent.putExtra(
                GoodsDetailActivity::class.java.simpleName,
                goodsId
            )
            intent.putExtra(
                IS_FULL_INFO,
                isShowFullInfo
            )
            (context as AppCompatActivity).overridePendingTransition(0, 300)
            context.startActivity(intent)
        }
    }

    var isShowFullInfo: Boolean = false

    lateinit var binding: ActivityGoodsDetailBinding

    var goodsId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_goods_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_detail)
        setResult(Activity.RESULT_CANCELED)

        if (getData()) {
            showLoad()
            inits()
            initEvents()
            loadData()
            hideData()
        } else {
            Toast.makeText(
                this,
                "Lỗi trong quá trình truyền tải thông tin, vui lòng thử lại!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun hideData() {
        tvGdEntryPriceLabel.visibility = View.GONE
        tvEntryPrice.visibility = View.GONE
        lnInventoryLimitLayout.visibility = View.GONE
        tvAmountLabel.visibility = View.GONE
        tvAmount.visibility = View.GONE
    }

    private fun loadData() {
        GetGoodsByIdProcessing(this, goodsId).onFinished = { goods ->
            if (goods != null) {
                showDetail()
                binding.goods = goods
                loadImageOfGoods(goods.images)
            } else {
                Toast.makeText(this, "Không thể xem chi tiết sản phẩm này!", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun loadImageOfGoods(images: List<String>) {
        if (images.isNotEmpty()) {
            for (i in images.indices) {
                try {
                    GlideUtils.loadToImageViewWithPlaceHolder(
                        findViewById(
                            resources.getIdentifier(
                                "ivGoodsImage$i",
                                "id",
                                packageName
                            )
                        ),
                        images[i],
                        R.drawable.no_image
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            tvGoodsDetailImageLabel.visibility = View.GONE
            hsvGoodsDetailImages.visibility = View.GONE
        }
    }

    private fun getData(): Boolean {
        goodsId = intent.getIntExtra(GoodsDetailActivity::class.java.simpleName, -1)
        isShowFullInfo = intent.getBooleanExtra(IS_FULL_INFO, false)
        return goodsId > 0
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

        ivCloseButton.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
    }

    fun showLoad() {
        rlInfoDisplayArea.visibility = View.GONE
        skvLoadingIcon.visibility = View.VISIBLE
    }

    fun showDetail() {
        rlInfoDisplayArea.visibility = View.VISIBLE
        skvLoadingIcon.visibility = View.GONE
    }

}