package ai.bom.firebase.lib.config

import ai.bom.firebase.lib.BuildConfig
import android.content.Context
import androidx.annotation.Keep
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Keep
class RemoteConfigDate(private val remoteTopic: String) {

    private var remoteConfig: FirebaseRemoteConfig? = null
    private val timeInMillis: Long = if (BuildConfig.DEBUG) 0L else 3600L

    private fun getInstance(): FirebaseRemoteConfig? {
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSetting = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(timeInMillis)
            .build()
        remoteConfig?.setConfigSettingsAsync(configSetting)
//        remoteConfig?.fetch(timeInMillis)
        remoteConfig?.setDefaultsAsync(
            mapOf(remoteTopic to Gson().toJson(Any()))
        )
        return remoteConfig
    }

    private fun getRemoteConfig(context: Context): Any? {
        val pref = SharedPref(context)
        var json = getInstance()?.getString(remoteTopic)

        if (json.isNullOrEmpty() || json == "{}") {
            json = pref.getRemoteString()
        }
        pref.putRemoteString(json)

        return Gson().fromJson(json, Any::class.java)
    }

    fun getRemoteConfig(context: Context, listener: ((Any?) -> Unit)) {
        getInstance()?.reset()
        getInstance()?.fetchAndActivate()
            ?.addOnCompleteListener { _ ->
                val value = getRemoteConfig(context)
                listener.invoke(value)
            }
    }
}

@Keep
data class RemoteDetailModel(
    @SerializedName("value")
    val value: String = "off"
) {
    override fun toString(): String {
        return "(value : $value)"
    }
}