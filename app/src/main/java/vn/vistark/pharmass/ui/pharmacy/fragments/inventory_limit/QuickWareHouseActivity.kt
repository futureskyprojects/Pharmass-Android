package vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_quick_ware_house.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.component.goods_detail.GoodsDetailActivity
import vn.vistark.pharmass.component.goods_inventory_limit_update.GoodsInventoryLimitUpdateActivity
import vn.vistark.pharmass.core.constants.RequestCode
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.core.model.GoodsCategory
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.processing.GetPharmacyGoodsInCategoryProcessing
import vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit.InventoryLimitCategoryViewHolder.Companion.TEMP_INVENTORY_GOODS_LIST
import vn.vistark.pharmass.ui.pharmacy.pharmacy_ware_house.GoodsAdapter

class QuickWareHouseActivity : AppCompatActivity() {
    var pharmacyJson: String = ""
    var goodsCategoryJson: String = ""

    var pharmacy: Pharmacy? = null
    var goodsCategory: GoodsCategory? = null

    val goodsList = ArrayList<Goods>()
    lateinit var adapter: GoodsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_ware_house)
        setResult(Activity.RESULT_OK)
        if (getPassingData()) {
            tvToolbarLabel.text = InventoryLimitFragment.currentFilter.name
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

        adapter =
            GoodsAdapter(
                goodsList
            )
        rvGoods.adapter = adapter

        adapter.onLongClicked = {
            GoodsDetailActivity.start(this, it.id, true)
        }

        adapter.onClicked = {
            val intent = Intent(this, GoodsInventoryLimitUpdateActivity::class.java)
            intent.putExtra(
                Goods::class.java.simpleName,
                Gson().toJson(it)
            )
            startActivityForResult(intent, RequestCode.REQUEST_GOODS_UPDATE_CODE)
        }
    }


    private fun getPassingData(): Boolean {
        // Tiến hành lấy thông tin nhà thuốc
        pharmacyJson = intent.getStringExtra(Pharmacy::class.java.simpleName) ?: ""
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
        goodsCategoryJson = intent.getStringExtra(GoodsCategory::class.java.simpleName) ?: ""
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
        // Lay ds sp
        val goodsListJson = intent.getStringExtra(TEMP_INVENTORY_GOODS_LIST) ?: ""
        val tempGoodsList =
            Gson().fromJson<ArrayList<Goods>>(
                goodsListJson,
                object : TypeToken<ArrayList<Goods>>() {}.type
            )
        if (goodsListJson.isEmpty() || tempGoodsList == null) {
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
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == RequestCode.REQUEST_GOODS_UPDATE_CODE) && resultCode == Activity.RESULT_OK && data != null) {
            val goodsJson = data.getStringExtra(Goods::class.java.simpleName) ?: ""
            val goods = Gson().fromJson(goodsJson, Goods::class.java)
            if (goodsJson.isNotEmpty() && goods != null) {
                for (i in goodsList.indices) {
                    if (goodsList[i].id == goods.id) {
                        goodsList[i].amount = goods.amount
                        goodsList[i].inventoryMost = goods.inventoryMost
                        goodsList[i].inventoryAtleast = goods.inventoryAtleast
                        adapter.notifyDataSetChanged()
                        break
                    }
                }
            }
        }
    }
}