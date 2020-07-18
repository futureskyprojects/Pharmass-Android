package vn.vistark.pharmass.ui.pharmacy_updater

import android.Manifest
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pharmacy_updater.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.request.BodyCreatePharmacyRequest
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.constants.RequestCode
import vn.vistark.pharmass.core.constants.RequestCode.Companion.PICK_FEATURE_IMAGE
import vn.vistark.pharmass.core.constants.RequestCode.Companion.PICK_LOGO
import vn.vistark.pharmass.core.model.Coordinates
import vn.vistark.pharmass.core.model.UserAddress
import vn.vistark.pharmass.databinding.ActivityPharmacyUpdaterBinding
import vn.vistark.pharmass.processing.CreatePharmacyProcessing
import vn.vistark.pharmass.processing.UserUploadImageProcessing
import vn.vistark.pharmass.component.address_picker.AddressPickerActivity
import vn.vistark.pharmass.utils.DialogNotify
import vn.vistark.pharmass.utils.GlideUtils
import vn.vistark.pharmass.utils.SimpfyLocationUtils

class PharmacyUpdaterActivity : AppCompatActivity() {
    var logoUri: Uri? = null
    var featureImageUri: Uri? = null

    var googleMap: GoogleMap? = null
    var marker: Marker? = null
    var workInWeek = arrayListOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")

    var isCreateNewPharmacy = true

    lateinit var binding: ActivityPharmacyUpdaterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pharmacy_updater)
//        setContentView(R.layout.activity_pharmacy_updater)
        binding.requestCreatePharmacy = BodyCreatePharmacyRequest()
        checkAllDays()
        val crNewPharmacyJson =
            intent.getStringExtra(PharmacyUpdaterActivity::class.java.simpleName) ?: ""
        if (crNewPharmacyJson.isNotEmpty()) {
            val tempBindingData =
                Gson().fromJson(crNewPharmacyJson, BodyCreatePharmacyRequest::class.java)
            if (tempBindingData != null) {
                isCreateNewPharmacy = false
                binding.requestCreatePharmacy = tempBindingData
            }
        }

        inits()
        initEvents()
        initMaps(savedInstanceState)
    }

    private fun checkAllDays() {
        rbPharmacyUpdaterTuesday.isChecked = true
        rbPharmacyUpdaterWednesday.isChecked = true
        rbPharmacyUpdaterThursday.isChecked = true
        rbPharmacyUpdaterFriday.isChecked = true
        rbPharmacyUpdaterSaturday.isChecked = true
        rbPharmacyUpdaterSunday.isChecked = true
        workInWeek = arrayListOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
        generateWorkInWeekValues()
    }

    fun generateWorkInWeekValues() {
        binding.requestCreatePharmacy?.dayInterval = workInWeek.joinToString()
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
                if (SimpfyLocationUtils.mLastLocation != null) {
                    val latLng = LatLng(
                        SimpfyLocationUtils.mLastLocation!!.latitude,
                        SimpfyLocationUtils.mLastLocation!!.longitude
                    )
                    val camUp = CameraUpdateFactory.newLatLngZoom(latLng, 18F)
                    googleMap!!.animateCamera(camUp)
                }
            }
            googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            googleMap?.uiSettings?.isZoomControlsEnabled = true
            googleMap?.uiSettings?.isCompassEnabled = true
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                googleMap?.isMyLocationEnabled = true
            }

            googleMap?.setOnCameraMoveListener {
                marker?.position = googleMap?.cameraPosition?.target
                if (marker != null) {
                    binding.requestCreatePharmacy?.coordinates =
                        Coordinates(marker!!.position.latitude, marker!!.position.longitude)
                }
            }
        }
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
        ivPharmacyUpdaterFeature.setOnClickListener {
            // Chọn ảnh biểu trưng
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Chọn ảnh biểu trưng"),
                PICK_FEATURE_IMAGE
            )
        }
        ivPharmacyUpdaterLogo.setOnClickListener {
            // Chọn logo
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Chọn logo"),
                PICK_LOGO
            )
        }
        tvPharmacyUpdaterAddress.setOnClickListener {
            val intent = Intent(this, AddressPickerActivity::class.java)
            intent.putExtra(
                AddressPickerActivity::class.java.simpleName,
                Gson().toJson(binding.requestCreatePharmacy!!.address)
            )
            startActivityForResult(intent, RequestCode.REQUEST_ADDRESS_CODE)
            this.overridePendingTransition(0, 300);
        }
        tvPharmacyUpdaterOpenTime.setOnClickListener {
            TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    if (timePickerValidate()) {
                        tvPharmacyUpdaterOpenTime.text =
                            String.format("%02d:%02d", hourOfDay, minute)
                        binding.requestCreatePharmacy?.openTime =
                            tvPharmacyUpdaterOpenTime.text.toString()
                    }
                }, 7, 0, true
            ).show()
        }
        tvPharmacyUpdaterCloseTime.setOnClickListener {
            TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    if (timePickerValidate()) {
                        tvPharmacyUpdaterCloseTime.text =
                            String.format("%02d:%02d", hourOfDay, minute)
                        binding.requestCreatePharmacy?.closeTime =
                            tvPharmacyUpdaterCloseTime.text.toString()
                    }
                }, 22, 0, true
            ).show()
        }
        rbPharmacyUpdaterMonday.setOnCheckedChangeListener { _, _ ->
            if (!rbPharmacyUpdaterMonday.isChecked) {
                workInWeek.remove("T2")
            } else {
                workInWeek.add("T2")
            }
            generateWorkInWeekValues()
        }
        rbPharmacyUpdaterTuesday.setOnCheckedChangeListener { _, _ ->
            if (!rbPharmacyUpdaterTuesday.isChecked) {
                workInWeek.remove("T3")
            } else {
                workInWeek.add("T3")
            }
            generateWorkInWeekValues()
        }
        rbPharmacyUpdaterWednesday.setOnCheckedChangeListener { _, _ ->
            if (!rbPharmacyUpdaterWednesday.isChecked) {
                workInWeek.remove("T4")
            } else {
                workInWeek.add("T4")
            }
            generateWorkInWeekValues()
        }
        rbPharmacyUpdaterThursday.setOnCheckedChangeListener { _, _ ->
            if (!rbPharmacyUpdaterThursday.isChecked) {
                workInWeek.remove("T5")
            } else {
                workInWeek.add("T5")
            }
            generateWorkInWeekValues()
        }
        rbPharmacyUpdaterFriday.setOnCheckedChangeListener { _, _ ->
            if (!rbPharmacyUpdaterFriday.isChecked) {
                workInWeek.remove("T6")
            } else {
                workInWeek.add("T6")
            }
            generateWorkInWeekValues()
        }
        rbPharmacyUpdaterSaturday.setOnCheckedChangeListener { _, _ ->
            if (!rbPharmacyUpdaterSaturday.isChecked) {
                workInWeek.remove("T7")
            } else {
                workInWeek.add("T7")
            }
            generateWorkInWeekValues()
        }
        rbPharmacyUpdaterSunday.setOnCheckedChangeListener { _, _ ->
            if (!rbPharmacyUpdaterSunday.isChecked) {
                workInWeek.remove("CN")
            } else {
                workInWeek.add("CN")
            }
            generateWorkInWeekValues()
        }
        btnCreatePharmacyConfirm.setOnClickListener {
            if (binding.requestCreatePharmacy == null) {
                DialogNotify.error(this, "Có lỗi khi khởi tạo nhà thuốc mới")
            } else {
                val rp = binding.requestCreatePharmacy!!
                if (rp.name.length < 10) {
                    DialogNotify.missingInput(
                        this,
                        "Vui lòng kiểm tra lại tên nhà thuốc của bạn!"
                    )
                } else if (rp.shortDescription.length < 10) {
                    DialogNotify.missingInput(
                        this,
                        "Mô tả của bạn nên dài hơn một chút để người dùng dễ hình dung!"
                    )
                } else if (tvPharmacyUpdaterAddress.text.length < 10) {
                    DialogNotify.missingInput(this, "Vui lòng cung cấp địa chỉ nhà thuốc của bạn!")
                } else if (marker == null) {
                    DialogNotify.missingInput(
                        this,
                        "Vui lòng ghim chính xác vị trí nhà thuốc của bạn trên bản đồ"
                    )
                } else if (rp.openTime.isEmpty()) {
                    DialogNotify.missingInput(this, "Vui lòng cho biết giờ mở cửa!")
                } else if (rp.closeTime.isEmpty()) {
                    DialogNotify.missingInput(this, "Vui lòng cho biết giờ đóng cửa")
                } else if (logoUri == null) {
                    DialogNotify.missingInput(this, "Vui lòng chọn Logo cho nhà thuốc của bạn")
                } else if (featureImageUri == null) {
                    DialogNotify.missingInput(
                        this,
                        "Vui lòng chọn ảnh nền biểu trưng cho nhà thuốc của bạn"
                    )
                } else {
                    binding.requestCreatePharmacy?.users =
                        BodyCreatePharmacyRequest.User(Constants.user.id)
                    uploadFeatureImages()
                }
            }
        }
    }

    private fun timePickerValidate(): Boolean {
        if (tvPharmacyUpdaterCloseTime.text.isNotEmpty() && tvPharmacyUpdaterOpenTime.text.isNotEmpty() && tvPharmacyUpdaterCloseTime.text.toString() < tvPharmacyUpdaterOpenTime.text.toString()) {
            DialogNotify.error(
                this,
                "Giờ hoạt động không hợp lệ, giờ mở cửa phải bé hơn hoặc bằng giờ đóng cửa"
            )
            return false
        }
        return true
    }

    private fun uploadFeatureImages() {
        if (binding.requestCreatePharmacy?.featureImages != null && binding.requestCreatePharmacy?.featureImages!!.isNotEmpty()) {
            uploadLogo()
            return
        }
        UserUploadImageProcessing(this, featureImageUri!!).apply {
            execute()
            onFinished = { featureImageAddress ->
                if (featureImageAddress != null) {
                    binding.requestCreatePharmacy!!.featureImages = featureImageAddress
                    uploadLogo()
                } else {
                    DialogNotify.error(
                        this@PharmacyUpdaterActivity,
                        "Lỗi trong quá trình cập nhật ảnh bìa cho nhà thuốc!"
                    )
                }
            }
        }
    }

    fun uploadLogo() {
        if (binding.requestCreatePharmacy?.logo != null && binding.requestCreatePharmacy?.logo!!.isNotEmpty()) {
            createPharmacyProcessing()
            return
        }
        UserUploadImageProcessing(this@PharmacyUpdaterActivity, logoUri!!).apply {
            execute()
            onFinished = { logoImageAddress ->
                if (logoImageAddress != null) {
                    binding.requestCreatePharmacy!!.logo = logoImageAddress
                    createPharmacyProcessing()
                } else {
                    DialogNotify.error(
                        this@PharmacyUpdaterActivity,
                        "Lỗi trong quá trình cập nhật ảnh đại diện cho nhà thuốc!"
                    )
                }
            }
        }
    }

    private fun createPharmacyProcessing() {
        CreatePharmacyProcessing(
            this@PharmacyUpdaterActivity,
            binding.requestCreatePharmacy!!
        ).onFinished = {
            if (it != null) {
                DialogNotify.success(this, "Tạo nhà thuốc hoàn tất!")
            } else {
                DialogNotify.error(this, "Có lỗi trong quá trình ghi nhận thông tin nhà thuốc!")
            }
            finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Kết quả trả về từ màn hình chọn địa điểm
        if (requestCode == RequestCode.REQUEST_ADDRESS_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val userAddressJson =
                data.getStringExtra(AddressPickerActivity::class.java.simpleName) ?: ""
            if (userAddressJson.length > 10) {
                val userAddress =
                    Gson().fromJson(userAddressJson, UserAddress::class.java) ?: return
                binding.requestCreatePharmacy?.address = userAddress
                tvPharmacyUpdaterAddress.text = userAddress.toString()
            }
        }
        // Kết quả trả về chọn logo và ảnh biểu trưng
        if (requestCode == PICK_LOGO && resultCode == Activity.RESULT_OK && data != null) {
            logoUri = data.data!!
            GlideUtils.loadToImageViewWithPlaceHolder(
                ivPharmacyUpdaterLogo,
                data.data!!,
                R.drawable.no_logo
            )
        }
        if (requestCode == PICK_FEATURE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            featureImageUri = data.data!!
            GlideUtils.loadToImageViewWithPlaceHolder(
                ivPharmacyUpdaterFeature,
                data.data!!,
                R.drawable.pharmacy_background
            )
        }
    }
}