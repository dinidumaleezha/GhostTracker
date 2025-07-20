package com.maleezha.ghosttracker.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.maleezha.ghosttracker.MainActivity;

public class SecretCodeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(
                context.getPackageName(),
                "com.maleezha.ghosttracker.MainActivity"
        );

        pm.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        );

        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}

