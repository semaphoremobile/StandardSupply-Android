package com.semaphore.standardsupply.handlers.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;

import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.SignupOperation;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.User;

public class SignupHandler extends Handler {

	public SignupHandler(SSActivity activity) {
		super(activity);
	}
	
	public User user;

	@Override
	public void request() {
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		user.device_uid = "unique_" + tm.getDeviceId();
		SignupOperation operation = new SignupOperation(this);
		operation.user = user;
		HttpAsync.makeRequest(operation);
	}
	
	@Override
	public void callback(String result) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Thanks for signing up! Your request is being processed and you will receive an email when your account is ready.")
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
