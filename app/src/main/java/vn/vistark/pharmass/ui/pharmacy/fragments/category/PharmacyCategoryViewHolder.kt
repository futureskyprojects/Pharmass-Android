package vn.vistark.pharmass.ui.pharmacy.fragments.category

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.GoodsCategory

class PharmacyCategoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val rlRoot: RelativeLayout = v.findViewById(R.id.rlRoot)
    val tvCategoryName: TextView = v.findViewById(R.id.tvCategoryName)
    val tvItemInCategoryCount: TextView = v.findViewById(R.id.tvItemInCategoryCount)

    fun bind(goodsCategory: GoodsCategory) {
        tvCategoryName.text = goodsCategory.name
    }
}