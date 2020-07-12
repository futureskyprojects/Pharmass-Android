package vn.vistark.pharmass.ui.medicine_category_picker

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.MedicineCategory
import vn.vistark.pharmass.core.model.Supplier

class SupplierPickerViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val lnRoot: LinearLayout = v.findViewById(R.id.lnRoot)
    val tvSupplierName: TextView = v.findViewById(R.id.tvSupplierName)
    val tvSupplierPhoneNumber: TextView = v.findViewById(R.id.tvSupplierPhoneNumber)
    val tvSupplierAddress: TextView = v.findViewById(R.id.tvSupplierAddress)

    fun bind(supplier: Supplier) {
        tvSupplierName.text = supplier.name
        tvSupplierPhoneNumber.text = supplier.phoneNumber
        tvSupplierAddress.text = supplier.address
    }
}