package ai.bom.firebase

import ai.bom.firebase.lib.config.RemoteDetailModel
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RemoteModel(
    @SerializedName("splashNative")
    val splashNative: RemoteDetailModel = RemoteDetailModel(),
    @SerializedName("splashInter")
    val splashInter: RemoteDetailModel = RemoteDetailModel(),
    @SerializedName("indexInter")
    val indexInter: RemoteDetailModel = RemoteDetailModel()
) {
    override fun toString(): String {
        return "splashNative : $splashNative, splashInter : $splashInter, indexInter : $indexInter"
    }
}