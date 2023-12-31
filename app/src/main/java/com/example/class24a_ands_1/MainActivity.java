package com.example.class24a_ands_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import android.content.ContentResolver;

public class MainActivity extends AppCompatActivity {

    private MaterialButton main_BTN_permission;
    private MaterialButton main_BTN_read;
    private MaterialTextView main_LBL_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_BTN_permission = findViewById(R.id.main_BTN_permission);
        main_BTN_read = findViewById(R.id.main_BTN_read);
        main_LBL_info = findViewById(R.id.main_LBL_info);


        main_BTN_permission.setOnClickListener(v -> permissionClicked());
        main_BTN_read.setOnClickListener(v -> readContacts());
    }

    private void readContacts() {
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

    private void permissionClicked() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{"android.permission.READ_CONTACTS"},
                1);
    }
}