package ai.bom.firebase.lib.config

import ai.bom.firebase.lib.BuildConfig
import androidx.annotation.Keep
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Keep
class RemoteConfigDate(private val remoteTopic: String) {

    private var remoteConfig: FirebaseRemoteConfig? = null
    private val timeInMillis: Long = if (BuildConfig.DEBUG) 0L else 3600L

    companion object {
        @JvmStatic
        var remoteAdSettings: Any? = null


        fun RemoteDetailModel.isRemoteAdOn() = (value == "on")
    }

    fun setRemoteSetting(remoteAdSetting: Any) {
        remoteAdSettings = remoteAdSetting
    }

    fun getRemoteSetting() = remoteAdSettings

    inline fun <reified T> getRemoteData() = (remoteAdSettings as? T?)

    private fun getInstance(): FirebaseRemoteConfig? {
        remoteConfig?.let {
            return it
        }
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSetting = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(timeInMillis)
            .build()
        remoteConfig?.setConfigSettingsAsync(configSetting)
        remoteConfig?.setDefaultsAsync(
            mapOf(remoteTopic to Gson().toJson(Any()))
        )
        return remoteConfig
    }


    private fun getRemoteConfig(): Any {
        return Gson().fromJson(
            getInstance()?.getString(remoteTopic),
            Any::class.java
        )
    }

    fun getRemoteConfig(listener: ((Any?) -> Unit)) {
        getInstance()?.fetchAndActivate()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val value = getRemoteConfig()
                    remoteAdSettings = value
                    listener.invoke(value)
                } else {
                    listener.invoke(null)
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