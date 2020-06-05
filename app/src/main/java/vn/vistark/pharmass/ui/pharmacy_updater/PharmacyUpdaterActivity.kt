package vn.vistark.pharmass.ui.pharmacy_updater

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_pharmacy_updater.*
import kotlinx.android.synthetic.main.components_search_bar.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R

class PharmacyUpdaterActivity : AppCompatActivity() {
    var googleMap: GoogleMap? = null
    var marker: Marker? = null

    var isCreateNewPharmacy = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_updater)
        isCreateNewPharmacy =
            (intent?.getStringExtra(PharmacyUpdaterActivity::class.java.simpleName)
                ?: "") == "CREATE"

        inits()
        initEvents()
        initMaps(savedInstanceState)
    }

    private fun initMaps(savedInstanceState: Bundle?) {
        mvPharmacyCoordinates.onCreate(savedInstanceState)
        mvPharmacyCoordinates.onResume()
        MapsInitializer.initialize(this)

        mvPharmacyCoordinates.getMapAsync { gMaps ->
            googleMap = gMaps
            googleMap?.setOnMapLoadedCallback {
                googleMap!!.clear()
                marker = googleMap!!.addMarker(
                    MarkerOptions()
                        .icon(
                            BitmapDescriptorFactory.fromResource(R.drawable.pharmacy_location_marker)
                        )
                        .position(
                            LatLng(
                                0.0,
                                0.0
                            )
                        )
                )
            }
            googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            googleMap?.uiSettings?.isZoomControlsEnabled = true
            googleMap?.uiSettings?.isCompassEnabled = true
            googleMap?.uiSettings?.isMyLocationButtonEnabled = true

            googleMap?.setOnCameraMoveListener {
                marker?.position = googleMap?.cameraPosition?.target
            }
        }
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun inits() {
        ivExpandAuthorInfomation.visibility = View.GONE
        ivBackButton.visibility = View.VISIBLE
        tvToolbarLabel.text =
            if (isCreateNewPharmacy) "Thêm nhà thuốc mới" else "Sửa thông tin nhà thuốc"
        tvToolbarLabel.setPadding(
            tvToolbarLabel.paddingLeft,
            tvToolbarLabel.paddingTop,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom
        )
        civUserAvatar.visibility = View.GONE
    }
}