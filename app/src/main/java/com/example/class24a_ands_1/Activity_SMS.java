package com.example.class24a_ands_1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

public class Activity_SMS extends AppCompatActivity {

    private MaterialButton main_BTN_permission;
    private MaterialButton main_BTN_read;
    private MaterialTextView main_LBL_info;


    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    readSms();
                } else {
                    boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS);
                    if (shouldShow) {
                        openPermissionInfo();
                        // last time before don't ask me again
                    } else {
                        // can't show any request 3+++
                        openSettingsInfo();
                    }
                }
            });

    private ActivityResultLauncher<Intent> manuallyPermissionResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    readSms();
                }
            });

    private void openSettingsInfo() {
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("SMS")
                .setMessage("Settings -> Permissions -> SMS -> Allow all the time")
                .setPositiveButton("Got It", (dialog, which) -> openSettings())
                .setNegativeButton("No", null)
                .show();
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        manuallyPermissionResultLauncher.launch(intent);
    }

    private void openPermissionInfo() {
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("SMS")
                .setMessage("We need it for...")
                .setPositiveButton("Got It", (dialog, which) -> requestPermissionLauncher.launch(Manifest.permission.READ_SMS))
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        main_BTN_permission = findViewById(R.id.main_BTN_permission);
        main_BTN_read = findViewById(R.id.main_BTN_read);
        main_LBL_info = findViewById(R.id.main_LBL_info);

        main_BTN_permission.setOnClickListener(v -> permissionStatus());
        main_BTN_read.setOnClickListener(v -> readSms());

    }

    private void readSms() {
        boolean isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
        if (isGranted) {
            main_LBL_info.setText("MY SMSs List");
        } else {
            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS);
            if (shouldShow) {
                // last time before don't ask me again
                openPermissionInfo();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_SMS);
            }
        }
    }


    private void permissionStatus() {
        boolean isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS);
        main_LBL_info.setText("Granted = " + isGranted + "\nshouldShow = " + shouldShow);
    }
}