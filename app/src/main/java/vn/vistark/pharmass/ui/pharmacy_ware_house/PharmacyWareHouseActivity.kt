package vn.vistark.pharmass.ui.pharmacy_ware_house

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pharmacy_ware_house.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.core.model.GoodsCategory
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.processing.GetPharmacyGoodsInCategoryProcessing
import vn.vistark.pharmass.ui.goods_updater.GoodsUpdaterActivity

class PharmacyWareHouseActivity : AppCompatActivity() {

    var pharmacy: Pharmacy? = null
    var goodsCategory: GoodsCategory? = null

    val goodsList = ArrayList<Goods>()
    lateinit var adapter: GoodsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_ware_house)
        if (getPassingData()) {
            tvToolbarLabel.text = pharmacy!!.name
            tvCategoryName.text = goodsCategory!!.name
            tvCategoryName.isSelected = true
        } else {
            // Bỏ qua các phương thức khởi tạo bên dưới và kết thúc màn hình này
            finish()
            return
        }
        inits()
        initEvents()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rvGoods.setHasFixedSize(true)
        rvGoods.layoutManager = LinearLayoutManager(this)

        adapter = GoodsAdapter(goodsList)
        rvGoods.adapter = adapter

        GetPharmacyGoodsInCategoryProcessing(this, pharmacy!!.id, goodsCategory!!.id).onFinished = {
            loadingIcon.visibility = View.GONE
            if (it != null) {
                goodsList.clear()
                goodsList.addAll(it)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    this,
                    "Hiện tại bạn chưa có nhân viên nào!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun getPassingData(): Boolean {
        // Tiến hành lấy thông tin nhà thuốc
        val pharmacyJson = intent.getStringExtra(Pharmacy::class.java.simpleName) ?: ""
        pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        if (pharmacyJson.isEmpty() || pharmacy == null) {
            Toast.makeText(
                this,
                "Không lấy được thông tin nhà thuốc truyền vào",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return false
        }
        // Tiến hành lấy thông tin danh mục loại sản phẩm
        val goodsCategoryJson = intent.getStringExtra(GoodsCategory::class.java.simpleName) ?: ""
        goodsCategory = Gson().fromJson(goodsCategoryJson, GoodsCategory::class.java)
        if (goodsCategoryJson.isEmpty() || goodsCategory == null) {
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

    private fun inits() {
        ivExpandAuthorInfomation.visibility = View.GONE
        ivBackButton.visibility = View.VISIBLE

        tvToolbarLabel.setPadding(
            tvToolbarLabel.paddingLeft,
            tvToolbarLabel.paddingTop,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom
        )
        tvToolbarLabel.isAllCaps = false
        civUserAvatar.visibility = View.GONE
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
        lnCreateNewGoods.setOnClickListener {
            val intent = Intent(this, GoodsUpdaterActivity::class.java)
            startActivity(intent)
        }
    }
}