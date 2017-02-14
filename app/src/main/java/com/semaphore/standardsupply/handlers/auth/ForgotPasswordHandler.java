package com.semaphore.standardsupply.handlers.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.ForgotPasswordOperation;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.handlers.Handler;

public class ForgotPasswordHandler extends Handler {

	public ForgotPasswordHandler(SSActivity activity) {
		super(activity);
	}

	public String email;
	
	@Override
	public void request() {
		ForgotPasswordOperation operation = new ForgotPasswordOperation(this);
		operation.email = email;
	 	HttpAsync.makeRequest(operation);
	}
	
	@Override
	public void callback(String result) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Your password has been sent! Please check your inbox.")
				       .setTitle("");
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               activity.finish();
			           }
			       });

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
	}

}
