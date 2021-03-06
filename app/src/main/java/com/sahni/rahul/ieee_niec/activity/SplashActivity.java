package com.sahni.rahul.ieee_niec.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.helpers.NotificationHelper;

public class SplashActivity extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "initial_setup";
    private static final String NOTIFICATION_CHANNEL_STATUS_KEY = "status";
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int CREATED = 1;
        int NOT_CREATED = 0;

        Intent intent = getIntent();
        String dataPayloadType = null;
        if(intent != null){
            dataPayloadType = intent.getStringExtra(ContentUtils.NOTIFICATION_DATA_PAYLOAD_KEY);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        int status = sharedPreferences.getInt(NOTIFICATION_CHANNEL_STATUS_KEY, NOT_CREATED);
        if(status == NOT_CREATED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationHelper.createNotificationChannel(this, getString(R.string.default_notification_channel_id));
            }
            SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
            editor.putInt(NOTIFICATION_CHANNEL_STATUS_KEY, CREATED);
            editor.apply();
        }
        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.putExtra(ContentUtils.NOTIFICATION_DATA_PAYLOAD_KEY, dataPayloadType);
        startActivity(intent1);
    }
}
