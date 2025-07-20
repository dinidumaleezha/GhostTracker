package com.maleezha.ghosttracker;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
    }
}

