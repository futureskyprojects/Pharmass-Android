package vn.vistark.pharmass.component.medicine_category_picker

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.MedicineCategory

class MedicineCategoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val lnRoot: LinearLayout = v.findViewById(R.id.lnRoot)
    val tvMedicineCategoryName: TextView = v.findViewById(R.id.tvMedicineCategoryName)
    val tvManufacturer: TextView = v.findViewById(R.id.tvManufacturer)
    val tvMedicineCategoryPacking: TextView = v.findViewById(R.id.tvMedicineCategoryPacking)

    fun bind(medicineCategory: MedicineCategory) {
        tvMedicineCategoryName.text = medicineCategory.name
        tvManufacturer.text = medicineCategory.manufacturer
        tvMedicineCategoryPacking.text = medicineCategory.packing
    }
}