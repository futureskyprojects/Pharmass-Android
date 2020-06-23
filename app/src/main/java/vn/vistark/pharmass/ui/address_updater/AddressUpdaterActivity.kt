package vn.vistark.pharmass.ui.address_updater

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_address_updater.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.constants.vietnam.*
import vn.vistark.pharmass.core.model.User
import vn.vistark.pharmass.core.model.UserAddress
import vn.vistark.pharmass.utils.DialogNotify
import vn.vistark.pharmass.utils.GlideUtils
import java.io.InputStreamReader

class AddressUpdaterActivity : AppCompatActivity() {
    lateinit var vietnamAdministrativeUnits: VietnamAdministrativeUnits

    var selectedProvince: Province? = null
    var selectedDistrict: District? = null
    var selectedWards: Wards? = null

    var userAddress: UserAddress? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_updater)
        inits()
        initEvents()
        setResult(Activity.RESULT_CANCELED)
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
        tvProvince.setOnClickListener {
            AlertDialog.Builder(this).apply {
                title = "Chọn tỉnh thành"
                var items: Array<String> = emptyArray()
                vietnamAdministrativeUnits.forEach {
                    items += it.nameWithType
                }
                setItems(items) { d, pos ->
                    selectedProvince = vietnamAdministrativeUnits.get(pos)
                    selectedDistrict = null
                    selectedWards = null
                    tvProvince.text = selectedProvince?.nameWithType
                    tvDistrict.text = ""
                    tvWards.text = ""
                }
            }.create().show()
        }

        tvDistrict.setOnClickListener {
            if (selectedProvince == null) {
                tvProvince.performClick()
            } else {
                AlertDialog.Builder(this).apply {
                    title = "Chọn quận/huyện"
                    var items: Array<String> = emptyArray()
                    selectedProvince?.quanHuyen?.forEach {
                        items += it.nameWithType
                    }
                    setItems(items) { d, pos ->
                        selectedDistrict = selectedProvince?.quanHuyen?.get(pos)
                        selectedWards = null
                        tvDistrict.text = selectedDistrict?.nameWithType
                        tvWards.text = ""
                    }
                }.create().show()
            }
        }

        tvWards.setOnClickListener {
            if (selectedDistrict == null) {
                tvDistrict.performClick()
            } else {
                AlertDialog.Builder(this).apply {
                    title = "Chọn xã phường"
                    var items: Array<String> = emptyArray()
                    selectedDistrict?.xaPhuong?.forEach {
                        items += it.nameWithType
                    }
                    setItems(items) { d, pos ->
                        selectedWards = selectedDistrict?.xaPhuong?.get(pos)
                        tvWards.text = selectedWards?.nameWithType
                    }
                }.create().show()
            }
        }
        btnConfirm.setOnClickListener {
            if (edtAddress.text.isEmpty()) {
                DialogNotify.missingInput(this, "Vui lòng cho biết số nhà/tên đường")
            } else if (selectedProvince == null) {
                DialogNotify.missingInput(this, "Vui lòng chọn tỉnh thành")
            } else if (selectedDistrict == null) {
                DialogNotify.missingInput(this, "Vui lòng chọn quận/huyện")
            } else if (selectedWards == null) {
                DialogNotify.missingInput(this, "Vui lòng chọn xã phường")
            } else {
                val userAddress = UserAddress()
                userAddress.address = edtAddress.text.toString()
                userAddress.wards =
                    BaseVietnamUnit(selectedWards!!.code, selectedWards!!.nameWithType)
                userAddress.district =
                    BaseVietnamUnit(selectedDistrict!!.code, selectedDistrict!!.nameWithType)
                userAddress.province =
                    BaseVietnamUnit(selectedProvince!!.code, selectedProvince!!.nameWithType)

                val textResult = Gson().toJson(userAddress)
                val intent = Intent()
                intent.putExtra(AddressUpdaterActivity::class.java.simpleName, textResult)

                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun readData() {
        val inp = resources.openRawResource(R.raw.tinh_thanh_vn)
        vietnamAdministrativeUnits =
            Gson().fromJson(InputStreamReader(inp), VietnamAdministrativeUnits::class.java)
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)

        readData()

        val userAddressJson =
            intent.getStringExtra(AddressUpdaterActivity::class.java.simpleName) ?: ""
        if (userAddressJson.length > 10) {
            userAddress = Gson().fromJson(userAddressJson, UserAddress::class.java)
            if (userAddress != null) {
                edtAddress.setText(userAddress!!.address)
                tvProvince.text = userAddress!!.province.name
                vietnamAdministrativeUnits.forEach {
                    if (it.code == userAddress!!.province.code) {
                        selectedProvince = it
                    }
                }
                tvDistrict.text = userAddress!!.district.name
                selectedProvince?.quanHuyen?.forEach {
                    if (it.code == userAddress!!.district.code) {
                        selectedDistrict = it
                    }
                }
                tvWards.text = userAddress!!.wards.name
                selectedDistrict?.xaPhuong?.forEach {
                    if (it.code == userAddress!!.wards.code) {
                        selectedWards = it
                    }
                }
            }
        }
    }
}