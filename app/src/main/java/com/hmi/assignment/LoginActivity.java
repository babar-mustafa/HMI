package com.hmi.assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hmi.assignment.fragments.LoginFragment;
import com.hmi.assignment.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {


            Utils.FragemntTransactionSimple(getSupportFragmentManager(),
                    R.id.containerLoginSignup, new LoginFragment(), false, "LoginFragment");
        } catch (Exception e) {
            Utils.showToastMessage(LoginActivity.this.getResources().getString(R.string.error_String), LoginActivity.this);

        }
    }
}
