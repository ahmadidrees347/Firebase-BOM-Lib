package ai.bom.firebase

import ai.bom.firebase.lib.analytics.FirebaseAnalytics
import ai.bom.firebase.lib.config.RemoteConfigData
import ai.bom.firebase.lib.fcm.FirebaseFCM
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private val fbAnalytics by lazy { FirebaseAnalytics(this) }
    private val remoteConfig = RemoteConfigData("demo")

    private var remoteAdSettings = RemoteModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Subscribe FCM Notifications
        val fcm = FirebaseFCM(this)
        fcm.initializeFCM("/topics/$packageName")

        //Send FireBase Analytic Event
        sendFbEvent("MainActivity", "Index Screen Open")

        remoteConfig.getRemoteConfig(this) {
            it?.let {
                val remoteJson = Gson().toJson(it)
                if (!remoteJson.isNullOrEmpty())
                    remoteAdSettings = Gson().fromJson(remoteJson, RemoteModel::class.java)

                Log.e("RemoteConfigNew*", "$remoteAdSettings")

                if (remoteAdSettings.splashNative.value == "on") {
                    //Load Splash Native AD
                }
                if (remoteAdSettings.splashInter.value == "on") {
                    //Load Splash Interstitial AD
                }
                sendFbEvent("RemoteConfig", "Config Fetch")
            }
        }
    }

    private fun sendFbEvent(eventName: String, eventStatus: String) {
        fbAnalytics.sendEventAnalytics(eventName, eventStatus)
    }

}