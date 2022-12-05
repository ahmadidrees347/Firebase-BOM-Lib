package ai.bom.firebase.lib.analytics

import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.annotation.Size
import com.google.firebase.analytics.FirebaseAnalytics

@Keep
class FirebaseAnalytics(context: Context) {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    /**
     * Command to Enable Analytics in Debug Mode
     * Command 1: adb shell setprop debug.firebase.analytics.app packageName
     * Command 2: adb shell setprop log.tag.FA VERBOSE
     * Command 3: adb shell setprop log.tag.FA-SVC VERBOSE
     * Command 4: adb logcat -v time -s FA FA-SVC
     * */

    fun sendEventAnalytics(@Size(min = 1L, max = 40L) eventName: String, eventStatus: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.VALUE, eventStatus)
        firebaseAnalytics.logEvent(eventName, bundle)
    }

}
