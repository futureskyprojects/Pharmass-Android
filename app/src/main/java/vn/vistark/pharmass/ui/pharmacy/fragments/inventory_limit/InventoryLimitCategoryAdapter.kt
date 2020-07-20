package vn.vistark.pharmass.ui.pharmacy.fragments.inventory_limit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.GoodsCategory
import vn.vistark.pharmass.core.model.Pharmacy

class InventoryLimitCategoryAdapter(val pharmacy: Pharmacy, val goodsCategories: ArrayList<GoodsCategory>) :
    RecyclerView.Adapter<InventoryLimitCategoryViewHolder>() {

    var onClicked: ((GoodsCategory) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryLimitCategoryViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_pharmacy_category_item, parent, false)
        return InventoryLimitCategoryViewHolder(v)
    }

    override fun getItemCount(): Int {
        return goodsCategories.size
    }

    override fun onBindViewHolder(holder: InventoryLimitCategoryViewHolder, position: Int) {
        val goodsCategory = goodsCategories[position]
        holder.bind(pharmacy, goodsCategory)
    }

}