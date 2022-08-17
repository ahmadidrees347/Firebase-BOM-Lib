## Using Firebase BOM Library in your Android application

### Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
### Add this in your build.gradle
```groovy
implementation 'com.github.ahmadidrees347:Firebase-BOM-Lib:firebase-bom-version'
```
Do not forget to add internet permission in manifest if already not present
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
Create Firebase project, add you application ID init, download your google-services.json file and place it in app folder.
### For FCM Notifications, initialize it in onCreate() Method of Application class :
```kotlin
val fcm = FirebaseFCM(this)
fcm.initializeFCM("/topics/$packageName")
```

### For FireBase Analytics, create an object FirebaseAnalytics, passing context init :
```kotlin
 val fbAnalytics = FirebaseAnalytics(this)
 ```
Send eventName & eventStatus init:

```kotlin
fbAnalytics.sendEventAnalytics(eventName, eventStatus)
```

### For FireBase Remote Config, create an object RemoteConfigDate, passing topicName init :
```kotlin
private val remoteConfig = RemoteConfigDate("topicName")
var remoteAdSettings = RemoteModel()
```
Make your Custom model the way you want to receive model, call getRemoteConfig() to get data:
```kotlin
remoteConfig.getRemoteConfig(context) {
    it?.let {
        val remoteJson = Gson().toJson(it)
        if (!remoteJson.isNullOrEmpty())
            remoteAdSettings = Gson().fromJson(remoteJson, RemoteModel::class.java)

        Log.e("RemoteConfigNew*", "$remoteAdSettings")

        if (remoteAdSettings.splashNative.value == "on") {
            //Load Splash Native AD
        }
    }
}
```
Data Class to receive the response:
```kotlin
@Keep
data class RemoteModel(
    @SerializedName("splashNative")
    val splashNative: RemoteDetailModel = RemoteDetailModel()
) {
    override fun toString(): String {
        return "splashNative : $splashNative"
    }
}
```

### License
```
   Copyright (C) 2022 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```