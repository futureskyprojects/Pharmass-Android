package vn.vistark.pharmass.ui.goods_updater

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_good_updater.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.core.model.GoodsCategory
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.databinding.ActivityGoodUpdaterBinding

class GoodsUpdaterActivity : AppCompatActivity() {
    var pharmacyJson: String = ""
    var goodsCategoryJson: String = ""

    lateinit var pharmacy: Pharmacy
    lateinit var goodsCategory: GoodsCategory

    lateinit var binding: ActivityGoodUpdaterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_good_updater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_good_updater)
        binding.requestCreateGoods = Goods()


        if (!getPassingData()) {
            // Bỏ qua các phương thức khởi tạo bên dưới và kết thúc màn hình này
            finish()
            return
        }

        // Xử lý phân biệt đối với thuốc và các sản phẩm không phải là thuốc
        if (goodsCategory.code == Constants.MEDICINE_CATEGORY_CODE) {
            // Nếu danh mục sản phẩm là thuốc
            tvUnit.visibility = View.GONE
            edtUnit.visibility = View.GONE

            tvManufacturerCountry.visibility = View.GONE
            edtManufacturerCountry.visibility = View.GONE

            tvPacking.visibility = View.GONE
            edtPacking.visibility = View.GONE

            tvGoodsName.text = "Chọn tên thuốc (Bắt buộc)"
            edtGoodsName.inputType = InputType.TYPE_NULL
        } else {
            // Nếu danh mục sản phẩm khôn phải thuốc
        }

        inits()
        initEvents()
    }

    private fun getPassingData(): Boolean {
        // Tiến hành lấy thông tin nhà thuốc
        pharmacyJson = intent.getStringExtra(Pharmacy::class.java.simpleName) ?: ""
        pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        if (pharmacyJson.isEmpty()) {
            Toast.makeText(
                this,
                "Không lấy được thông tin nhà thuốc truyền vào",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return false
        }
        // Tiến hành lấy thông tin danh mục loại sản phẩm
        goodsCategoryJson = intent.getStringExtra(GoodsCategory::class.java.simpleName) ?: ""
        goodsCategory = Gson().fromJson(goodsCategoryJson, GoodsCategory::class.java)
        if (goodsCategoryJson.isEmpty()) {
            Toast.makeText(
                this,
                "Không thể lấy được thông tin danh mục loại sản phẩm truyền vào",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return false
        }
        return true
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
        btnGoodsUpdaterConfirm.setOnClickListener {
            println(Gson().toJson(binding.requestCreateGoods!!))
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