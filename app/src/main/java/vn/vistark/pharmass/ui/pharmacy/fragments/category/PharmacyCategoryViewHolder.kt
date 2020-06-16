package vn.vistark.pharmass.ui.pharmacy.fragments.category

import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.APIUtils
import vn.vistark.pharmass.core.model.GoodsCategory
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.processing.GetCountGoodsInCategoryProcessing

class PharmacyCategoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val rlRoot: RelativeLayout = v.findViewById(R.id.rlRoot)
    private val tvCategoryName: TextView = v.findViewById(R.id.tvCategoryName)
    private val tvItemInCategoryCount: TextView = v.findViewById(R.id.tvItemInCategoryCount)
    private val pbLoadingCount: ProgressBar = v.findViewById(R.id.pbLoadingCount)

    fun bind(pharmacy: Pharmacy, goodsCategory: GoodsCategory) {
        tvCategoryName.text = goodsCategory.name
        GetCountGoodsInCategoryProcessing(pharmacy.id, goodsCategory.id).onFinished = {
            pbLoadingCount.visibility = View.GONE
            tvItemInCategoryCount.visibility = View.VISIBLE
            tvItemInCategoryCount.text = "($it)"
        }
    }
}