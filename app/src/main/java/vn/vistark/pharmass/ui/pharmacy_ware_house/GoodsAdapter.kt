package vn.vistark.pharmass.ui.pharmacy_ware_house

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Goods

class GoodsAdapter(val goodsList: ArrayList<Goods>) : RecyclerView.Adapter<GoodsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.component_pharmacy_goods_item,
            parent,
            false
        )
        return GoodsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return goodsList.size
    }

    override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
        val goods = goodsList[position]
        holder.bind(goods)
    }

}