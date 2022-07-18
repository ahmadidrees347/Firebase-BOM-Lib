package ai.bom.firebase.lib.analytics

import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import com.google.firebase.analytics.FirebaseAnalytics

@Keep
class FirebaseAnalytics(context: Context) {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    /**
     * Command to Enable Analytics in Debug Mode
     * Command : adb shell setprop debug.firebase.analytics.app packageName
     * */

    fun sendEventAnalytics(eventName: String, eventStatus: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.VALUE, eventStatus)
        firebaseAnalytics.logEvent(eventName, bundle)
    }

}
