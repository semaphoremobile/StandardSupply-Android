package com.semaphore.standardsupply.activity;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.handlers.auth.ForgotPasswordHandler;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ForgotPasswordActivity extends SSActivity {

	EditText txtForgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        txtForgotPassword = (EditText) findViewById(R.id.forgotpassword_email_field);
    }
    
    public void onResetClick(View v) {
        ForgotPasswordHandler handler = new ForgotPasswordHandler(this);
        handler.email = txtForgotPassword.getText().toString();
        handler.request();
    }
}
