package vn.vistark.pharmass.ui.pharmacy.fragments.statistical

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Goods

class TopTrendingGoodsAdapter(val topTrendingGoodsInfos: ArrayList<TopTrendingGoodsInfo>) :
    RecyclerView.Adapter<TopTrendingGoodsViewHolder>() {

    var onClicked: ((TopTrendingGoodsInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopTrendingGoodsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.component_pharmacy_goods_item,
            parent,
            false
        )
        return TopTrendingGoodsViewHolder(
            v
        )
    }

    override fun getItemCount(): Int {
        return topTrendingGoodsInfos.size
    }

    override fun onBindViewHolder(holder: TopTrendingGoodsViewHolder, position: Int) {
        val topTrendingGoodsInfo = topTrendingGoodsInfos[position]
        holder.bind(topTrendingGoodsInfo)
        holder.rlRoot.setOnClickListener {
            onClicked?.invoke(topTrendingGoodsInfo)
        }
    }

    companion object {
        data class TopTrendingGoodsInfo(
            var goods: Goods,
            var dosage: Double,
            var totalMoney: Double
        )
    }
}