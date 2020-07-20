package vn.vistark.pharmass.component.bill_detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bill_detail.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.component.view_all_bill.ViewAllBillActivity

class BillDetailActivity : AppCompatActivity() {

    companion object {
        fun start(
            context: Context,
            billId: Int
        ) {
            val intent = Intent(context, BillDetailActivity::class.java)
            intent.putExtra(
                BillDetailActivity::class.java.simpleName,
                billId
            )
            (context as AppCompatActivity).overridePendingTransition(0, 300)
            context.startActivity(intent)
        }
    }

    var billId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_detail)
        setResult(Activity.RESULT_CANCELED)

        if (getData()) {
            showLoad()
            inits()
            initEvents()
        } else {
            Toast.makeText(
                this,
                "Lỗi trong quá trình truyền tải thông tin, vui lòng thử lại!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getData(): Boolean {
        billId = intent.getIntExtra(BillDetailActivity::class.java.simpleName, -1)
        return billId > 0
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