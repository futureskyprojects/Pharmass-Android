package vn.vistark.pharmass.ui.update_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.components_toolbar.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.constants.Gender
import vn.vistark.pharmass.utils.GlideUtils

class UpdateProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        inits()
        initEvents()
    }

    private fun initEvents() {
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
        loadBigAvatar()
        loadUserBaseInfo()
    }

    private fun loadUserBaseInfo() {
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
    }
}