package com.semaphore.standardsupply.handlers.auth;

import android.content.Intent;

import com.google.gson.Gson;
import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.LoginOperation;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.IdNameObj;
import com.semaphore.standardsupply.model.Settings;
import com.semaphore.standardsupply.model.User;


public class LoginHandler extends Handler {
	
	public LoginHandler(SSActivity activity){
		super(activity);
	}
	public String email;
	public String password;
	
	public void request(){
		LoginOperation operation = new LoginOperation(this);
		operation.email = email;
		operation.password = password;
		HttpAsync.makeRequest(operation);
	}

	@Override
	public void callback(String result) {
		Gson gson = new Gson();
		User user = gson.fromJson(result, User.class);
		Settings.putUser(activity, user);
		IdNameObj truck = new IdNameObj();
		truck.id = user.truck_id;
		truck.name = user.truck_name;
		Settings.putTruck(activity, truck);
		IdNameObj location = new IdNameObj();
		location.id = user.default_branch_id;
		location.name = user.location_name;
		Settings.putLocation(activity, location);
		
		/*
		Intent i = new Intent(activity,HomeActivity.class);
        activity.startActivity(i);
        activity.finish();*/
		activity.OnHandlerCompleted();
	}

}
