package vn.vistark.pharmass.ui.work

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Pharmacy

class CurrentUserPharmacyAdapter(val pharmacies: ArrayList<Pharmacy>) :
    RecyclerView.Adapter<CurrentUserPharmacyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentUserPharmacyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_work_pharmacy_item, parent, false)
        return CurrentUserPharmacyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return pharmacies.size
    }

    override fun onBindViewHolder(holder: CurrentUserPharmacyViewHolder, position: Int) {
        val pharmacy = pharmacies[position]
        holder.bind(pharmacy)
    }
}