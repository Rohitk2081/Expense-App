package com.kehgye.expensetrackingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "ExpenseAppPrefs";
    private static final String AUTH_TOKEN = "authToken";
    private static final String USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Get the SharedPreferences to check login status
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_TOKEN, null);
        String userId = sharedPreferences.getString(USER_ID, null);

        // Log for debugging
        Log.d("SplashActivity", "Auth Token: " + authToken + ", User ID: " + userId);

        // Delay to show splash screen for 2 seconds
        new Handler().postDelayed(() -> {
            Intent intent;
            if (authToken != null && userId != null) {
                // If both authToken and userId are present, go to MainActivity
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // If not logged in, go to LoginActivity
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish(); // Close SplashActivity
        }, 2000); // 2-second delay
    }
}