package vn.vistark.pharmass.core.constants

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import vn.vistark.pharmass.core.model.User

class Constants(context: Context) {
    companion object {
        val TAG = Constants::class.java.simpleName
        var sharedPreferences: SharedPreferences? = null
        var user: User
            @SuppressLint("DefaultLocale")
            get() {
                return Gson().fromJson(
                    sharedPreferences?.getString(
                        User::class.java.simpleName.toUpperCase(),
                        ""
                    ), User::class.java
                ) ?: User()
            }
            @SuppressLint("DefaultLocale", "ApplySharedPref")
            set(user) {
                sharedPreferences?.edit()
                    ?.putString(User::class.java.simpleName.toUpperCase(), Gson().toJson(user))
                    ?.commit()
            }
        var token: String
            get() {
                return sharedPreferences?.getString("TOKEN", "") ?: ""
            }
            @SuppressLint("ApplySharedPref")
            set(token) {
                sharedPreferences?.edit()?.putString("TOKEN", token)?.commit()
            }
    }

    init {
        sharedPreferences = context.getSharedPreferences(
            "VISTARK_PHARMASS_NGUYEN_TRONG_NGHIA",
            Context.MODE_PRIVATE
        )
    }
}