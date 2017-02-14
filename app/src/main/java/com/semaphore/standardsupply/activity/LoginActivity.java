package com.semaphore.standardsupply.activity;


import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.handlers.auth.LoginHandler;
import com.semaphore.standardsupply.model.Settings;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends SSActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        if(Settings.getUser(this) != null){
        	Intent i = new Intent(this, HomeActivity.class);
        	this.startActivity(i);
        	finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // No menu
        return false;
    }
    
    public void onLoginClick(View v) {
        LoginHandler handler = new LoginHandler(this);
        if(this.validate()){
        	TextView emailField = (TextView) findViewById(R.id.login_email_field);
        	TextView passwordField = (TextView) findViewById(R.id.login_password_field);
        	handler.email = emailField.getText().toString();
        	handler.password = passwordField.getText().toString();
        	handler.request();
        }
    }
    
    private boolean validate(){
    	boolean validated = true;
    	TextView emailField = (TextView) findViewById(R.id.login_email_field);
    	TextView passwordField = (TextView) findViewById(R.id.login_password_field);
    	if(emailField.getText().toString().trim().equals("")){
    		validated = false;
    	}
    	
    	if(passwordField.getText().toString().trim().equals("")){
    		validated = false;
    	}
    	
    	return validated;
    }
    
    public void onForgotPasswordClick(View v) {
        Intent i = new Intent(this,ForgotPasswordActivity.class);
        startActivityForResult(i, -1);        
    }
    
    public void onSignUpClick(View v) {
        Intent i = new Intent(this,SignUpActivity.class);
        startActivityForResult(i, -1);
    }
}
