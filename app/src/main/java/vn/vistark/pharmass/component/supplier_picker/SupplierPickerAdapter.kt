package vn.vistark.pharmass.ui.medicine_category_picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.MedicineCategory
import vn.vistark.pharmass.core.model.Supplier

class SupplierPickerAdapter(val suppliers: ArrayList<Supplier>) :
    RecyclerView.Adapter<SupplierPickerViewHolder>() {

    var onClicked: ((Supplier) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierPickerViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_supplier_item, parent, false)
        return SupplierPickerViewHolder(v)
    }

    override fun getItemCount(): Int {
        return suppliers.size
    }

    override fun onBindViewHolder(holder: SupplierPickerViewHolder, position: Int) {
        val supplier = suppliers[position]
        holder.bind(supplier)
        holder.lnRoot.setOnClickListener {
            onClicked?.invoke(supplier)
        }
    }

}