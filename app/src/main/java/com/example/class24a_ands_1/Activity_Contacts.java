package com.example.class24a_ands_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import android.content.ContentResolver;

public class Activity_Contacts extends AppCompatActivity {

    private static final int CONTACTS_PERMISSION_COUNT = 111;
    private static final int CONTACTS_PERMISSION_LIST = 222;

    private MaterialButton main_BTN_permission;
    private MaterialButton main_BTN_count;
    private MaterialButton main_BTN_read;
    private MaterialTextView main_LBL_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        main_BTN_permission = findViewById(R.id.main_BTN_permission);
        main_BTN_count = findViewById(R.id.main_BTN_count);
        main_BTN_read = findViewById(R.id.main_BTN_read);
        main_LBL_info = findViewById(R.id.main_LBL_info);


        main_BTN_permission.setOnClickListener(v -> permissionClicked());
        main_BTN_permission.setVisibility(View.GONE);
        main_BTN_count.setOnClickListener(v -> countContactsClicked());
        main_BTN_read.setOnClickListener(v -> readContactsClicked());
    }

    private void countContactsClicked() {
        boolean isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        if (isGranted) {
            countContacts();
        } else {
            ActivityCompat.requestPermissions(Activity_Contacts.this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION_COUNT);
        }
    }

    private void readContactsClicked() {
        boolean isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        if (isGranted) {
            readContacts();
        } else {
            ActivityCompat.requestPermissions(Activity_Contacts.this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION_LIST);
        }
    }

    private void showMessage() {
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setMessage("Hi")
                .setTitle("Bye")
                .setPositiveButton("Close", null)
                .show();

    }

    private void permissionClicked() {
        ActivityCompat.requestPermissions(Activity_Contacts.this,
                new String[]{Manifest.permission.READ_CONTACTS
//                        ,"android.permission.ACCESS_COARSE_LOCATION"
//                        ,"android.permission.ACCESS_FINE_LOCATION"
//                        ,"android.permission.BLUETOOTH"
//                        ,"android.permission.BODY_SENSORS"
                },
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CONTACTS_PERMISSION_COUNT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    countContacts();
                } else {
                    Log.d("pttt", "PERMISSION_DENIED 1");
                    //countContactsClicked();
//                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
            return;
            case CONTACTS_PERMISSION_LIST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Log.d("pttt", "PERMISSION_DENIED 2");
                    //readContactsClicked();
//                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("pttt", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("pttt", "onResume");
    }

    private void readContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        int size = 0;

        String data = "";
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d("pttt", phoneNo);
                        data += "\n" + name + " - " + phoneNo;
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }


        main_LBL_info.setText("Contacts:" + data);
    }

    private void countContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        int size = 0;

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d("pttt", phoneNo);
                        size++;
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }


        main_LBL_info.setText("Num of contacts: " + size);
    }

}