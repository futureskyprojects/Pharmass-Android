package vn.vistark.pharmass.component.user_basic_info

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_user_basic_info.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.core.constants.Gender
import vn.vistark.pharmass.databinding.ActivityUserBasicInfoBinding
import vn.vistark.pharmass.processing.GetUserByIdProcessing
import vn.vistark.pharmass.utils.GlideUtils

class UserBasicInfoActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, userId: Int, isShowFullInfo: Boolean = false) {
            val intent = Intent(context, UserBasicInfoActivity::class.java)
            val baseOfUserDataTransfer = BaseOfUserDataTransfer(userId, isShowFullInfo)
            intent.putExtra(
                BaseOfUserDataTransfer::class.java.simpleName,
                Gson().toJson(baseOfUserDataTransfer)
            )
            (context as AppCompatActivity).overridePendingTransition(0, 300)
            context.startActivity(intent)
        }

        data class BaseOfUserDataTransfer(
            val userId: Int,
            val isShowFullInfo: Boolean = false
        )
    }

    var baseOfUserDataTransfer: BaseOfUserDataTransfer? = null

    lateinit var binding: ActivityUserBasicInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_user_basic_info)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_basic_info)
        setResult(Activity.RESULT_CANCELED)

        if (getData()) {
            showLoad()
            inits()
            initEvents()
            hideImportantData()
            loadUserInfo()
        } else {
            Toast.makeText(
                this,
                "Không thể xác dịnh thông tin của người này, vui lòng thử lại!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun loadUserInfo() {
        GetUserByIdProcessing(this, baseOfUserDataTransfer!!.userId)
            .onFinished = {
            if (it != null && it.isNotEmpty()) {
                binding.user = it.first()
                showSpecialMediaAndInfo()
                showDetail()
            } else {
                Toast.makeText(
                    this,
                    "Không thể lấy thông tin của tài khoản hiện tại, vui lòng thử lại",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun showSpecialMediaAndInfo() {
        GlideUtils.loadToImageViewWithPlaceHolder(
            civUserBigAvatar,
            binding.user!!.getAvatarFullAddress(),
            R.drawable.no_avatar
        )
        tvUserGender.text = Gender.find(Constants.user.gender)
        tvUserEmail.isSelected = true
    }

    private fun hideImportantData() {
        if (!baseOfUserDataTransfer!!.isShowFullInfo) {
            // Nếu không cho hiển thị full thông tin
            tvUserPhoneNumber.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            tvUserIdentifyCardNumber.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            tvUserAddress.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun getData(): Boolean {
        val baseOfUserDataTransferJson =
            intent.getStringExtra(BaseOfUserDataTransfer::class.java.simpleName) ?: ""
        baseOfUserDataTransfer =
            Gson().fromJson(baseOfUserDataTransferJson, BaseOfUserDataTransfer::class.java)

        return (baseOfUserDataTransferJson.isNotEmpty() && baseOfUserDataTransfer != null)
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }

        ivCloseButton.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
    }

    fun showLoad() {
        rvInfoDisplayArea.visibility = View.GONE
        skvLoadingIcon.visibility = View.VISIBLE
    }

    fun showDetail() {
        rvInfoDisplayArea.visibility = View.VISIBLE
        skvLoadingIcon.visibility = View.GONE
    }
}