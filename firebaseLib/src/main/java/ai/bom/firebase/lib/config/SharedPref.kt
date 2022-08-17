package ai.bom.firebase.lib.config


import android.content.Context
import android.content.SharedPreferences

class SharedPref(private val context: Context) {

    private val strRemote = "remote"
    private val strPref = "pref"

    fun putRemoteString(value: String) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(strPref, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(strRemote, value)
        editor.apply()
    }

    fun getRemoteString(): String {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(strPref, Context.MODE_PRIVATE)
        return sharedPref.getString(strRemote, "{}") ?: "{}"
    }
}
