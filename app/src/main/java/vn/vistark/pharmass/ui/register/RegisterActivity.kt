package vn.vistark.pharmass.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_register.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.api.request.BodyRegisterRequest
import vn.vistark.pharmass.core.constants.Constants
import vn.vistark.pharmass.databinding.ActivityRegisterBinding
import vn.vistark.pharmass.processing.RegisterProcessing
import vn.vistark.pharmass.ui.login.LoginActivity
import vn.vistark.pharmass.utils.DialogNotify

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
//        setContentView(R.layout.activity_register)
        binding.registerRequest = BodyRegisterRequest()
        initEvents()
    }

    private fun initEvents() {
        tvLoginLink.setOnClickListener {
            goLogin()
        }
        btnRegister.setOnClickListener {
            if (binding.registerRequest == null) {
                DialogNotify.error(this, "Lỗi liên kết dữ liệu trong ứng dụng")
                return@setOnClickListener
            }
            val validateMessage = binding.registerRequest!!.validate()
            if (validateMessage.isNotEmpty()) {
                // Nếu validate có trả về thông báo, hiển thị nó lên
                DialogNotify.warning(this, validateMessage)
            } else {
                RegisterProcessing(this, binding.registerRequest!!).onFinished = {
                    if (it != null) {
                        Constants.token = it.jwt
                        Constants.user = it.user
                        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).apply {
                            titleText =
                                "Đăng ký tài khoản thành công, hãy đăng nhập và cùng khám phá Pharmass nhé!"
                            contentText = "THÀNH CÔNG"
                            showCancelButton(false)
                            setConfirmButton("Đăng nhập") {
                                registerSuccess()
                            }
                            show()
                            setCancelable(false)
                        }
                    } else {
                        DialogNotify.error(
                            this,
                            "Xác nhận tài khoản đăng ký thông thành công, vui lòng thử lại!"
                        )
                    }
                }
            }
        }
    }

    fun registerSuccess() {
        goLogin()
    }

    fun goLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}