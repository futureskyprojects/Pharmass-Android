package vn.vistark.pharmass.ui.pharmacy.pharmacy_detail.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_mini_map_pharmacy.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Coordinates
import vn.vistark.pharmass.utils.SimpfyLocationUtils
import vn.vistark.pharmass.utils.VistarkMapView

class MiniMapPharmacyFragment : Fragment() {
    var coordinates: Coordinates? = null
    private var param1: String? = null

    var googleMap: GoogleMap? = null
    var marker: Marker? = null

    lateinit var mvPharmacyCoordinates: VistarkMapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            coordinates = Gson().fromJson(param1, Coordinates::class.java) ?: null
            if (coordinates != null) {

            } else {
                Toast.makeText(
                    context,
                    "Không thể lấy tọa độ của nhà thuốc này",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_mini_map_pharmacy, container, false)
        mvPharmacyCoordinates = v.findViewById(R.id.mvPharmacyCoordinates)
        initMaps(savedInstanceState)
        return v
    }


    private fun initMaps(savedInstanceState: Bundle?) {
        mvPharmacyCoordinates.onCreate(savedInstanceState)
        mvPharmacyCoordinates.onResume()
        MapsInitializer.initialize(context)

        mvPharmacyCoordinates.getMapAsync { gMaps ->
            googleMap = gMaps
            googleMap?.setOnMapLoadedCallback {
                marker = googleMap!!.addMarker(
                    MarkerOptions()
                        .icon(
                            BitmapDescriptorFactory.fromResource(R.drawable.pharmacy_location_marker)
                        )
                        .position(
                            LatLng(
                                coordinates!!.lat,
                                coordinates!!.lng
                            )
                        )
                )
                val camUp = CameraUpdateFactory.newLatLngZoom(marker!!.position, 18F)
                googleMap!!.animateCamera(camUp)
//                if (SimpfyLocationUtils.mLastLocation != null) {
//                    val latLng = LatLng(
//                        SimpfyLocationUtils.mLastLocation!!.latitude,
//                        SimpfyLocationUtils.mLastLocation!!.longitude
//                    )
//                    val camUp = CameraUpdateFactory.newLatLngZoom(latLng, 18F)
//                    googleMap!!.animateCamera(camUp)
//                }
            }
            googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            googleMap?.uiSettings?.isZoomControlsEnabled = true
            googleMap?.uiSettings?.isCompassEnabled = true
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                googleMap?.isMyLocationEnabled = true
            }
        }
    }


    companion object {
        private const val ARG_PARAM1 = "param1"

        @JvmStatic
        fun newInstance(param1: String) =
            MiniMapPharmacyFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}