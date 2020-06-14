package vn.vistark.pharmass.ui.pharmacy.fragments.staff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.PharmacyStaff

class PharmacyStaffAdapter(val staffs: ArrayList<PharmacyStaff>) :
    RecyclerView.Adapter<PharmacyStaffViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyStaffViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_pharmacy_staff_item, parent, false)
        return PharmacyStaffViewHolder(v)
    }

    override fun getItemCount(): Int {
        return staffs.size
    }

    override fun onBindViewHolder(holder: PharmacyStaffViewHolder, position: Int) {
        val staff = staffs[position]
        holder.bind(staff)
    }

}