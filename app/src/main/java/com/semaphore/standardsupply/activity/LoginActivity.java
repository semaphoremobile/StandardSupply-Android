package com.semaphore.standardsupply.activity;


import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.home.profile.ProfileFragment;
import com.semaphore.standardsupply.handlers.auth.LoginHandler;
import com.semaphore.standardsupply.model.IdNameObj;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends SSActivity implements Observer {

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

    @Override
    public void update(Observable arg0, Object arg1) {
        Model.getInstance().getLocationsCache().deleteObserver(this);
        ArrayList<IdNameObj> items = Model.getInstance().getLocationsCache().items;
        if(items == null || items.size() == 0){
            // Do nothing here.. this technically should not happen
        }
        else{
            Settings.putLocation(this, items.get(0));
            navigateToHome();
        }
    }

    @Override
    public void OnHandlerCompleted(){
        // Find if the location is already set, otherwise request it
        IdNameObj location = Settings.getLocation(this);
        if(location != null && location.name != null && location.name.trim().length() > 0) {
            navigateToHome();
        } else {
            Model.getInstance().getLocationsCache().addObserver(this);
            Model.getInstance().getLocationsCache().request();
        }
    }

    private void navigateToHome(){
        SSActivity activity = this;
        Intent i = new Intent(activity,HomeActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

}
