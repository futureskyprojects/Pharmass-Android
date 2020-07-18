package vn.vistark.pharmass.component.goods_picker

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_goods_picker.*
import vn.vistark.pharmass.R
import vn.vistark.pharmass.core.model.Goods
import vn.vistark.pharmass.core.model.Pharmacy
import vn.vistark.pharmass.processing.GetPharmacyGoodsByNameProcessing
import vn.vistark.pharmass.ui.pharmacy_ware_house.GoodsAdapter

class GoodsPickerActivity : AppCompatActivity() {

    var pharmacyJson: String = ""
    lateinit var pharmacy: Pharmacy

    val listOfGoods = ArrayList<Goods>()

    lateinit var adapter: GoodsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_picker)
        setResult(Activity.RESULT_CANCELED)
        if (getPassingData()) {
            inits()
            initEvents()
            onTextChange()
            loadMedicineCategoryByName("")
        } else {
            Toast.makeText(
                this,
                "Không nhận diện được nhà thuốc hiện đang thao tác",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun getPassingData(): Boolean {
        // Tiến hành lấy thông tin nhà thuốc
        pharmacyJson = intent.getStringExtra(Pharmacy::class.java.simpleName) ?: ""
        pharmacy = Gson().fromJson(pharmacyJson, Pharmacy::class.java)
        if (pharmacyJson.isEmpty()) {
            Toast.makeText(
                this,
                "Không lấy được thông tin nhà thuốc truyền vào",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return false
        }
        return true
    }

    private fun onTextChange() {
        edtGoodsName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    val name = s.toString()
                    if (name.length > 2) {
                        loadMedicineCategoryByName(name)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun loadMedicineCategoryByName(name: String) {
        loadingIcon.visibility = View.VISIBLE
        GetPharmacyGoodsByNameProcessing(this, pharmacy.id, name).onFinished = {
            loadingIcon.visibility = View.GONE
            if (it != null) {
                listOfGoods.clear()
                listOfGoods.addAll(it)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    this,
                    "Không lấy được kết quả nào",
                    Toast.LENGTH_SHORT
                ).show()
            }
            selectMedicineCategoryHintControl()
        }
    }

    private fun initEvents() {
        rlHomeMenuRoot.setOnClickListener {
            rlHomeMenuRoot.setBackgroundColor(Color.TRANSPARENT)
            onBackPressed()
        }
    }

    private fun inits() {
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        lnMenuContainer.startAnimation(slideUpAnimation)

        rvGoods.setHasFixedSize(true)
        rvGoods.layoutManager = LinearLayoutManager(this)

        selectMedicineCategoryHintControl()

        adapter =
            GoodsAdapter(
                listOfGoods
            )
        rvGoods.adapter = adapter

        adapter.onClicked = {
            val intent = Intent()
            intent.putExtra(Goods::class.java.simpleName, Gson().toJson(it))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun selectMedicineCategoryHintControl() {
        if (listOfGoods.size > 0) {
            tvHintForSelect.visibility = View.VISIBLE
            rvGoods.visibility = View.VISIBLE
        } else {
            tvHintForSelect.visibility = View.GONE
            rvGoods.visibility = View.GONE
        }
    }
}