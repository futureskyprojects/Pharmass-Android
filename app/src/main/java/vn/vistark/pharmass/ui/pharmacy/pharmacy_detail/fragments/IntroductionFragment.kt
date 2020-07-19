package vn.vistark.pharmass.ui.pharmacy.pharmacy_detail.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import vn.vistark.pharmass.R

class IntroductionFragment : Fragment() {


    private var param1: String? = null
    lateinit var tvIntroduction: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_introduction, container, false)
        tvIntroduction = v.findViewById(
            R.id.tvIntroduction
        )
        if (param1 != null && param1!!.isNotEmpty()) {
            tvIntroduction.text = param1
        }
        return v
    }

    companion object {
        const val ARG_PARAM1 = "param1"


        @JvmStatic
        fun newInstance(param1: String) =
            IntroductionFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}