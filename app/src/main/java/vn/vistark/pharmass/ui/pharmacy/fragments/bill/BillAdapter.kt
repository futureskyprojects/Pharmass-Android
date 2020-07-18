package vn.vistark.pharmass.ui.pharmacy.fragments.bill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Bill

class BillAdapter(val bills: ArrayList<Bill>) : RecyclerView.Adapter<BillViewHolder>() {

    var onClicked: ((Bill) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_pharmacy_bill_layout_item, parent, false)
        return BillViewHolder(v)
    }

    override fun getItemCount(): Int {
        return bills.size
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val bill = bills[position]
        holder.bind(bill)
        holder.rlRoot.setOnClickListener {
            onClicked?.invoke(bill)
        }
    }
}