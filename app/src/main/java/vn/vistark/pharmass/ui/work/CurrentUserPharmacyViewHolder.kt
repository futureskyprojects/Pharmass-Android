package vn.vistark.pharmass.ui.work

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.ui.pharmacy_detail.PharmacyDetailActivity
import vn.vistark.pharmass.utils.GlideUtils

class CurrentUserPharmacyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val ivUserPharmacyFeatureImages: ImageView = v.findViewById(R.id.ivUserPharmacyFeatureImages)
    val ivUserPharmacyLogo: ImageView = v.findViewById(R.id.ivUserPharmacyLogo)
    val tvUserPharmacyName: TextView = v.findViewById(R.id.tvUserPharmacyName)
    val tvuserPharmacyRatingNumber: TextView = v.findViewById(R.id.tvuserPharmacyRatingNumber)
    val tvUserPharmacyWorkTime: TextView = v.findViewById(R.id.tvUserPharmacyWorkTime)
    val tvUserPharmacyAdress: TextView = v.findViewById(R.id.tvUserPharmacyAdress)
    val tvUserPharmacyShortDescription: TextView =
        v.findViewById(R.id.tvUserPharmacyShortDescription)
    val cvRootLayout: CardView = v.findViewById(R.id.cvRootLayout)

    @SuppressLint("SetTextI18n")
    fun bind(pharmacy: Pharmacy) {
        GlideUtils.loadToImageViewWithPlaceHolder(
            ivUserPharmacyFeatureImages,
            pharmacy.getFeatureImageFullAddress(),
            R.drawable.pharmacy_background
        )
        GlideUtils.loadToImageViewWithPlaceHolder(
            ivUserPharmacyLogo,
            pharmacy.getLogoImageFullAddress(),
            R.drawable.no_logo
        )
        tvUserPharmacyName.text = pharmacy.name
        tvUserPharmacyName.isSelected = true

//        tvuserPharmacyRatingNumber

        tvUserPharmacyWorkTime.text = "${pharmacy.openTime} - ${pharmacy.closeTime}"
        tvUserPharmacyWorkTime.isSelected = true

        tvUserPharmacyAdress.text = pharmacy.address.toString()
        tvUserPharmacyAdress.isSelected = true

        tvUserPharmacyShortDescription.text = pharmacy.shortDescription
        tvUserPharmacyShortDescription.isSelected = true

        cvRootLayout.setOnClickListener {
            val intent = Intent(cvRootLayout.context, PharmacyDetailActivity::class.java)
            intent.putExtra(PharmacyDetailActivity::class.java.simpleName, Gson().toJson(pharmacy))
            cvRootLayout.context.startActivity(intent)
            (cvRootLayout.context as AppCompatActivity).overridePendingTransition(0, 300);
        }
    }
}