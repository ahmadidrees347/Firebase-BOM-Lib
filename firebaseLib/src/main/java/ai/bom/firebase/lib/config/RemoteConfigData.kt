package ai.bom.firebase.lib.config

import ai.bom.firebase.lib.BuildConfig
import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.annotation.Keep
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Keep
class RemoteConfigData(private val remoteTopic: String) {

    private var remoteConfig: FirebaseRemoteConfig? = null
    private val timeInMillis: Long = if (BuildConfig.DEBUG) 0L else 3600L
    private var lastValueReceivedTime: Long = 0

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
            ?.addOnCompleteListener { task ->
                val status = SystemClock.elapsedRealtime() - lastValueReceivedTime < 1000
                Log.e("RemoteConfigNew*", "status $status: ${task.isSuccessful}")
                if (status) {
                    return@addOnCompleteListener
                }
                lastValueReceivedTime = SystemClock.elapsedRealtime()
                if (task.isSuccessful) {
                    val value = getRemoteConfig(context)
                    listener.invoke(value)
                } else {
                    listener.invoke(getRemoteConfig(context))
                }
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