package com.semaphore.standardsupply.handlers.home.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.auth.ChangePasswordOperation;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.handlers.Handler;

public class ChangePasswordHandler extends Handler {

	public ChangePasswordHandler(SSActivity activity) {
		super(activity);
	}

	public String newPassword;
	public String currentPassword;
	
	@Override
	public void request() {
		ChangePasswordOperation operation = new ChangePasswordOperation(this);
		operation.currentPassword = currentPassword;
		operation.newPassword = newPassword;
		HttpAsync.makeRequest(operation);
	}
	
	@Override
	public void callback(String result) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Your password has been updated.")
				       .setTitle("Updated!");
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               activity.getFragmentManager().popBackStack();
			           }
			       });

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
	}

}
