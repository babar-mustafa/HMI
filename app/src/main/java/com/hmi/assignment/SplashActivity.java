package com.hmi.assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    Integer splashscreenTime = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds

                    sleep(splashscreenTime * 1000);
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);

                    startActivity(i);
//                    if (SharedPrefManager.getInstance(getBaseContext()).isLoggedIn()) {
//
//                            startActivity(new Intent(getBaseContext(), LoginActivity.class));
//
//                        } else {
//                            startActivity(new Intent(getBaseContext(), MainActivity.class));
//
//                        }

                    finish();


                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }
}
