package vn.vistark.pharmass.component.patient_picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.pharmass.R
import vn.vistark.pharmass.component.user_basic_info.UserBasicInfoActivity
import vn.vistark.pharmass.core.model.MedicineCategory
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.core.model.User

class PatientAdapter(val pharmacy: Pharmacy, var patients: ArrayList<User>) :
    RecyclerView.Adapter<PatientViewHolder>() {
    var onClicked: ((User) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_bill_patient_item, parent, false)
        return PatientViewHolder(v)
    }

    override fun getItemCount(): Int {
        return patients.size
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]
        holder.rlBillPatientItem.setOnClickListener {
            onClicked?.invoke(patient)
        }
        holder.rlBillPatientItem.setOnLongClickListener {
            UserBasicInfoActivity.start(holder.rlBillPatientItem.context, patient.id, true)
            return@setOnLongClickListener true
        }

        holder.bind(pharmacy, patient)
    }

}