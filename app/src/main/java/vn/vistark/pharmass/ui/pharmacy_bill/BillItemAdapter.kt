package vn.vistark.pharmass.ui.pharmacy_bill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.BillItem
import vn.vistark.pharmass.core.model.Goods

class BillItemAdapter(val billItems: ArrayList<BillItem>) :
    RecyclerView.Adapter<BillItemViewHolder>() {

    var onClicked: ((BillItem) -> Unit)? = null
    var onItemLongClicked: ((BillItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.component_pharmacy_goods_item,
            parent,
            false
        )
        return BillItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return billItems.size
    }

    override fun onBindViewHolder(holder: BillItemViewHolder, position: Int) {
        val billItem = billItems[position]
        holder.bind(billItem)
        holder.rlRoot.setOnClickListener {
            onClicked?.invoke(billItem)
        }
        holder.rlRoot.setOnLongClickListener {
            onItemLongClicked?.invoke(billItem)
            return@setOnLongClickListener true
        }
    }

}