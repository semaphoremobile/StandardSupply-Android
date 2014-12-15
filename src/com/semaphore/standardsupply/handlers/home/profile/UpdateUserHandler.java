package com.semaphore.standardsupply.handlers.home.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.auth.UpdateUserOperation;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.Settings;
import com.semaphore.standardsupply.model.User;

public class UpdateUserHandler extends Handler {

	public UpdateUserHandler(SSActivity activity) {
		super(activity);
	}
	
	public User user;

	@Override
	public void request() {
		UpdateUserOperation operation = new UpdateUserOperation(this);
		operation.user = user;
		HttpAsync.makeRequest(operation);
	}
	

	@Override
	public void callback(String result) {
		
		Settings.putUser(activity, user);
		// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Your profile has been successfully updated")
				       .setTitle("Updated!");
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               
			           }
			       });

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
	}

}
