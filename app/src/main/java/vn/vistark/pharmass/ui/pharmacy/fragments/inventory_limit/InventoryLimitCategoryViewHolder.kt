package vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit

import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.APIUtils
import vn.vistark.pharmass.core.constants.RequestCode
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.core.model.GoodsCategory
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.processing.GetCountGoodsInCategoryProcessing
import vn.vistark.pharmass.processing.GetPharmacyGoodsInCategoryProcessing
import vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit.InventoryLimitFragment.Companion.ABOVE_LIMIT
import vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit.InventoryLimitFragment.Companion.NORMAL
import vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit.InventoryLimitFragment.Companion.OUT_OF_STOCK
import vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit.InventoryLimitFragment.Companion.UNDER_LIMIT
import vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit.InventoryLimitFragment.Companion.currentFilter

class InventoryLimitCategoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    companion object {
        public const val TEMP_INVENTORY_GOODS_LIST = "TEMP_INVENTORY_GOODS_LIST"
    }

    val goodsList = ArrayList<Goods>()

    val rlRoot: RelativeLayout = v.findViewById(R.id.rlRoot)
    private val tvCategoryName: TextView = v.findViewById(R.id.tvCategoryName)
    private val tvItemInCategoryCount: TextView = v.findViewById(R.id.tvItemInCategoryCount)
    private val pbLoadingCount: ProgressBar = v.findViewById(R.id.pbLoadingCount)

    fun bind(pharmacy: Pharmacy, goodsCategory: GoodsCategory) {
        tvCategoryName.text = goodsCategory.name
        GetPharmacyGoodsInCategoryProcessing(
            tvCategoryName.context,
            pharmacy.id,
            goodsCategory.id
        ).onFinished = {
            pbLoadingCount.visibility = View.GONE
            tvItemInCategoryCount.visibility = View.VISIBLE
            if (it != null && it.isNotEmpty()) {
                goodsFilter(it)
                tvItemInCategoryCount.text = "(${goodsList.size})"
            } else {
                tvItemInCategoryCount.text = "(0)"
            }
        }
        rlRoot.setOnClickListener {
            if (goodsList.size <= 0) {
                Toast.makeText(rlRoot.context, "Không có sản phẩm nào phù hợp", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(rlRoot.context, QuickWareHouseActivity::class.java)
                intent.putExtra(
                    Pharmacy::class.java.simpleName,
                    Gson().toJson(pharmacy)
                )
                intent.putExtra(GoodsCategory::class.java.simpleName, Gson().toJson(goodsCategory))
                intent.putExtra(TEMP_INVENTORY_GOODS_LIST, Gson().toJson(goodsList))
                (rlRoot.context as AppCompatActivity).startActivityForResult(
                    intent,
                    RequestCode.REQUEST_INVENTORY_LIMIT_RELOAD_CODE
                )
            }
        }
    }

    private fun goodsFilter(goodsList: List<Goods>) {
        this.goodsList.clear()
        if (currentFilter == OUT_OF_STOCK) {
            goodsList.forEach { goods ->
                if (goods.amount <= 0)
                    this.goodsList.add(goods)
            }
        } else if (currentFilter == UNDER_LIMIT) {
            goodsList.forEach { goods ->
                if (goods.amount < goods.inventoryAtleast)
                    this.goodsList.add(goods)
            }
        } else if (currentFilter == ABOVE_LIMIT) {
            goodsList.forEach { goods ->
                if (goods.amount > goods.inventoryMost)
                    this.goodsList.add(goods)
            }
        } else if (currentFilter == NORMAL) {
            goodsList.forEach { goods ->
                if (goods.amount <= goods.inventoryMost && goods.amount >= goods.inventoryAtleast)
                    this.goodsList.add(goods)
            }
        } else {
            this.goodsList.addAll(goodsList)
        }
    }
}