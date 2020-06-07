package vn.vistark.pharmass.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.request.BodyLoginRequest
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.databinding.ActivityLoginBinding
import vn.vistark.pharmass.processing.GetUserSelftProcessing
import vn.vistark.pharmass.processing.LoginProcessing
import vn.vistark.pharmass.ui.home.HomeActivity
import vn.vistark.pharmass.ui.pharmacy.PharmacyActivity
import vn.vistark.pharmass.ui.register.RegisterActivity
import vn.vistark.pharmass.utils.DialogNotify
import vn.vistark.pharmass.utils.SimpfyLocationUtils
import java.util.*

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
//        setContentView(R.layout.activity_login)
        binding.loginRequest = BodyLoginRequest()
        inits()
        initEvents()
//        startActivity(Intent(this, PharmacyActivity::class.java))
        autoLogin()
        // Lấy vị trí lần đầu tiên
        SimpfyLocationUtils.getLastLocation(FusedLocationProviderClient(this))
    }

    private fun inits() {
        Constants(this) // Khởi tạo bộ lưu dữ liệu cho lần đầu
        if (Constants.user.username.isNotEmpty()) {
            binding.loginRequest!!.identifier = Constants.user.username
        }
    }

    private fun autoLogin() {
        if (Constants.token.length > 30) {
            GetUserSelftProcessing(this).onFinished = {
                if (it != null) {
                    Constants.user = it
                    Toast.makeText(this, "Chào mừng quay trở lại", Toast.LENGTH_SHORT).show()
                    goHome()
                }
            }
        }
    }

    private fun initEvents() {
        btnLogin.setOnClickListener {
            if (binding.loginRequest == null) {
                DialogNotify.error(this, "Lỗi liên kết dữ liệu trong ứng dụng")
                return@setOnClickListener
            }
            val validateMessage = binding.loginRequest!!.validate()
            if (validateMessage.isNotEmpty()) {
                DialogNotify.warning(this, validateMessage)
            } else {
                LoginProcessing(this, binding.loginRequest!!).onFinished = {
                    println("Đăng nhập JSON: " + Gson().toJson(it))
                    if (it != null) {
                        Constants.token = it.jwt
                        Constants.user = it.user
                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                        goHome()
                    } else {
                        DialogNotify.error(
                            this,
                            "Gặp lỗi trong quá trình lấy thông tin tài khoản, vui lòng thử lại!"
                        )
                    }
                }
            }
        }
        tvRegisterLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun goHome() {
        println("USER sau lưu: " + Gson().toJson(Constants.user))
        if (!Constants.user.confirmed) {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).apply {
                titleText =
                    "Tài khoản của bạn chưa được xác nhận, vui lòng kiểm tra Email để tiến hành xác nhận tài khoản!"
                contentText = "STOP"
                showCancelButton(false)
                setCancelable(false)
                show()
            }
        } else if (Constants.user.blocked) {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).apply {
                titleText =
                    "Tài khoản của bạn đã bị khóa, vui lòng liên hệ quản trị viên để biết thêm chi tiết!"
                contentText = "BLOCKED"
                showCancelButton(false)
                setCancelable(false)
                show()
            }
        } else {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}