package vn.vistark.pharmass.ui.update_profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.activity_update_profile.lnMenuContainer
import kotlinx.android.synthetic.main.components_toolbar.*
import kotlinx.android.synthetic.main.components_toolbar.civUserAvatar
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.request.BodyUpdateProfileRequest
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.constants.Gender
import vn.vistark.pharmass.core.constants.RequestCode.Companion.PICK_AVATAR
import vn.vistark.pharmass.core.constants.RequestCode.Companion.REQUEST_ADDRESS_CODE
import vn.vistark.pharmass.core.model.UserAddress
import vn.vistark.pharmass.databinding.ActivityUpdateProfileBinding
import vn.vistark.pharmass.processing.UpdateUserSelftProfileProcessing
import vn.vistark.pharmass.processing.UserUploadImageProcessing
import vn.vistark.pharmass.component.address_picker.AddressPickerActivity
import vn.vistark.pharmass.utils.DialogNotify
import vn.vistark.pharmass.utils.GlideUtils

class UpdateProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile)
        binding.profileUpdateRequest = BodyUpdateProfileRequest.copy(Constants.user)
//        setContentView(R.layout.activity_update_profile)
        inits()
        initEvents()
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }

        lnUpdateAddressButton.setOnClickListener {
            val intent = Intent(this, AddressPickerActivity::class.java)
            intent.putExtra(
                AddressPickerActivity::class.java.simpleName,
                Gson().toJson(Constants.user.address)
            )
            startActivityForResult(intent, REQUEST_ADDRESS_CODE)
            this.overridePendingTransition(0, 300);
        }
        tvChangeFullnameBtn.setOnClickListener {
            showUpdateDialog(1)
        }
        tvChangeGenderBtn.setOnClickListener {
            showUpdateDialog(2)
        }
        tvChangePhoneNumber.setOnClickListener {
            showUpdateDialog(3)
        }
        tvChangeIdentifyCardNumber.setOnClickListener {
            showUpdateDialog(4)
        }
        tvChangeBirthdayBtn.setOnClickListener {
            showUpdateDialog(5)
        }
        tvChangePasswordBtn.setOnClickListener {
            showUpdateDialog(6)
        }
        tvChangeIntroductionBtn.setOnClickListener {
            showUpdateDialog(7)
        }
        btnConfirm.setOnClickListener {
            updateProfile()
        }
        tvBirthdayPicker.setOnClickListener {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    tvBirthdayPicker.text =
                        String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                }, 1998, 3, 14
            ).show()
        }
        rbMale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                binding.profileUpdateRequest!!.gender = Gender.MALE
        }
        rbFemale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                binding.profileUpdateRequest!!.gender = Gender.FEMALE
        }
        rbSkip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                binding.profileUpdateRequest!!.gender = Gender.SKIP
        }
        tvChangeAvatarBtn.setOnClickListener {
            initEditDialogData()
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Chọn avatar"),
                PICK_AVATAR
            )
        }
    }

    private fun loadUserBaseInfo() {
        loadBigAvatar()
        if (Constants.user.fullName.isNotEmpty())
            tvUserFullName.text = Constants.user.fullName
        tvUserGender.text = Gender.find(Constants.user.gender)
        tvUserEmail.text = Constants.user.email
        if (Constants.user.phoneNumber.isNotEmpty())
            tvUserPhoneNumber.text = Constants.user.phoneNumber
        if (Constants.user.identifyCardNumber.isNotEmpty())
            tvUserIdentifyCardNumber.text = Constants.user.identifyCardNumber
        if (Constants.user.birthDay.isNotEmpty())
            tvUserBirthDay.text = Constants.user.birthDay
        if (Constants.user.introduction.isNotEmpty())
            tvUserIntroduction.text = Constants.user.introduction

        val userAddress = Constants.user.address.toString()
        if (userAddress.length > 10) {
            tvUserAddress.text = userAddress
        }
    }

    private fun loadBigAvatar() {
        GlideUtils.loadToImageViewWithPlaceHolder(
            civUserBigAvatar,
            Constants.user.getAvatarFullAddress(),
            R.drawable.no_avatar
        )
    }


    private fun inits() {
        ivExpandAuthorInfomation.visibility = View.GONE
        ivBackButton.visibility = View.VISIBLE
        tvToolbarLabel.text = "Cập nhật hồ sơ"
        tvToolbarLabel.setPadding(
            tvToolbarLabel.paddingLeft + 25,
            tvToolbarLabel.paddingTop,
            tvToolbarLabel.paddingRight,
            tvToolbarLabel.paddingBottom
        )
        civUserAvatar.visibility = View.INVISIBLE
        // Init data
        loadUserBaseInfo()
    }

    fun initEditDialogData() {
        binding.profileUpdateRequest = BodyUpdateProfileRequest.copy(Constants.user)
        when (Constants.user.gender) {
            1 -> rbMale.isChecked = true
            2 -> rbFemale.isChecked = true
            else -> rbSkip.isChecked = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADDRESS_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val userAddressJson =
                data.getStringExtra(AddressPickerActivity::class.java.simpleName) ?: ""
            if (userAddressJson.length > 10) {
                val userAddress =
                    Gson().fromJson(userAddressJson, UserAddress::class.java) ?: return
                initEditDialogData()
                binding.profileUpdateRequest?.address = userAddress
                updateProfile()
            }
        }
        if (requestCode == PICK_AVATAR && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val avatarUri = data.data!!
            UserUploadImageProcessing(this, avatarUri).apply {
                execute()
                onFinished = {
                    if (it != null) {
                        initEditDialogData()
                        binding.profileUpdateRequest!!.avatar = it
                        updateProfile()
                    } else {
                        DialogNotify.error(
                            this@UpdateProfileActivity,
                            "Cập nhật ảnh đại diện không thành công!"
                        )
                    }
                }
            }
        }
    }

    fun updateProfile() {
        hideUpdateDialog()
        UpdateUserSelftProfileProcessing(this, binding.profileUpdateRequest!!).onFinished = {
            if (it != null) {
                Constants.user = it
                loadUserBaseInfo()
                DialogNotify.success(this, "Cập nhật hồ sơ thành công")
            } else {
                DialogNotify.error(this, "Cập nhật hồ sơ không thành công")
            }
        }
    }

    fun hideAllUpdateFieldInDialog() {
        tvFullNameLabel.visibility = View.GONE
        edtFullName.visibility = View.GONE

        tvGenderLabel.visibility = View.GONE
        rgGenderPiker.visibility = View.GONE

        tvPhoneNumberLabel.visibility = View.GONE
        edtPhoneNumber.visibility = View.GONE

        tvIdentifyCardNumberLabel.visibility = View.GONE
        edtIdentifyCardNumber.visibility = View.GONE

        tvBirthdayLabel.visibility = View.GONE
        tvBirthdayPicker.visibility = View.GONE

        tvPasswordLabel.visibility = View.GONE
        edtPassword.visibility = View.GONE
        tvRePasswordLabel.visibility = View.GONE
        edtRePassword.visibility = View.GONE

        tvIntroductionLabel.visibility = View.GONE
        edtIntroduction.visibility = View.GONE
    }

    fun showUpdateDialog(case: Int = 1) {
        hideAllUpdateFieldInDialog()
        when (case) {
            1 -> {
                tvUpdateProfileTitle.text = "Cập nhật họ và tên"
                tvFullNameLabel.visibility = View.VISIBLE
                edtFullName.visibility = View.VISIBLE
            }
            2 -> {
                tvUpdateProfileTitle.text = "Cập nhật giới tính"
                tvGenderLabel.visibility = View.VISIBLE
                rgGenderPiker.visibility = View.VISIBLE
            }
            3 -> {
                tvUpdateProfileTitle.text = "Cập nhật số điện thoại"
                tvPhoneNumberLabel.visibility = View.VISIBLE
                edtPhoneNumber.visibility = View.VISIBLE
            }
            4 -> {
                tvUpdateProfileTitle.text = "Cập nhật số CMND"
                tvIdentifyCardNumberLabel.visibility = View.VISIBLE
                edtIdentifyCardNumber.visibility = View.VISIBLE
            }
            5 -> {
                tvUpdateProfileTitle.text = "Cập nhật ngày sinh"
                tvBirthdayLabel.visibility = View.VISIBLE
                tvBirthdayPicker.visibility = View.VISIBLE
            }
            6 -> {
                tvUpdateProfileTitle.text = "Thay đổi mật khẩu"
                tvPasswordLabel.visibility = View.VISIBLE
                edtPassword.visibility = View.VISIBLE
                tvRePasswordLabel.visibility = View.VISIBLE
                edtRePassword.visibility = View.VISIBLE
            }
            7 -> {
                tvUpdateProfileTitle.text = "Cập nhật giới thiệu"
                tvIntroductionLabel.visibility = View.VISIBLE
                edtIntroduction.visibility = View.VISIBLE
            }
        }
        rlHomeMenuRoot.visibility = View.VISIBLE
        rlHomeMenuRoot.setOnClickListener {
            hideUpdateDialog()
        }
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)

        initEditDialogData()
    }

    fun hideUpdateDialog() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        lnMenuContainer.startAnimation(slideUpAnimation)
        rlHomeMenuRoot.postDelayed({
            rlHomeMenuRoot.visibility = View.GONE
        }, 300)
    }
}