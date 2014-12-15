package com.semaphore.standardsupply.activity;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.handlers.auth.SignupHandler;
import com.semaphore.standardsupply.model.IdNameObj;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.User;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class SignUpActivity extends SSActivity implements Observer {

	EditText txtFirstName;
	EditText txtLastName;
	EditText txtEmail;
	EditText txtTruckId;
	EditText txtCustomerId;
	EditText txtBranchId;
	EditText txtPassword;
	EditText txtPasswordConfirm;
	IdNameObj location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		txtFirstName = (EditText) findViewById(R.id.txtFirstName);
		txtLastName = (EditText) findViewById(R.id.txtLastName);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtTruckId = (EditText) findViewById(R.id.txtTruckId);
		txtCustomerId = (EditText) findViewById(R.id.txtCustomerId);
		txtBranchId = (EditText) findViewById(R.id.txtPickupLocation);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtPasswordConfirm = (EditText) findViewById(R.id.txtReconfirmPassword);
		txtBranchId.setOnClickListener(branchListener);
		txtBranchId.setFocusable(false);
		ArrayList<IdNameObj> items = Model.getInstance().getLocationsCache().items;
		if(items == null || items.size() == 0){
			Model.getInstance().getLocationsCache().addObserver(this);
			Model.getInstance().getLocationsCache().request();	
		}
	}
	
	OnClickListener branchListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);


			final CharSequence[] items = new String[Model.getInstance().getLocationsCache().items.size()];

			int i = 0;
			for (IdNameObj obj : Model.getInstance().getLocationsCache().items) {
				items[i] = obj.name;
				i++;
			}

			builder.setItems(items, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					location = Model.getInstance().getLocationsCache().items.get(arg1);	
					txtBranchId.setText(location.name);
				}
			});
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@SuppressLint("CommitTransaction")
				public void onClick(DialogInterface dialog, int id) {

				}
			});

			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}; 

	public void onSignUpClick(View v) {
		if(validate()){
			User user = new User();
			user.first_name = txtFirstName.getText().toString();
			user.last_name = txtLastName.getText().toString();
			user.email = txtEmail.getText().toString();
			user.truck_name = txtTruckId.getText().toString();
			user.customer_id = Integer.parseInt(txtCustomerId.getText().toString());
			user.default_branch_id = location.id;
			user.password = txtPassword.getText().toString();
			user.password_confirmation = txtPasswordConfirm.getText().toString();
			user.device_uid = "uniqie_android_id";
			SignupHandler handler = new SignupHandler(this);
			handler.user = user;
			handler.request();
		}
	}

	private boolean validate(){
		boolean validated = true;
		if(location == null){
			validated = false;
			AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
			
			builder.setMessage("Please choose a default pickup location");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@SuppressLint("CommitTransaction")
				public void onClick(DialogInterface dialog, int id) {

				}
			});

			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return validated;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Model.getInstance().getLocationsCache().deleteObserver(this);
		ArrayList<IdNameObj> items = Model.getInstance().getLocationsCache().items;
		if(items == null || items.size() == 0){
			// 1. Instantiate an AlertDialog.Builder with its constructor
						AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
						
						builder.setMessage("Sorry the list of locations cannot be fetched");
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@SuppressLint("CommitTransaction")
							public void onClick(DialogInterface dialog, int id) {

							}
						});

						// 3. Get the AlertDialog from create()
						AlertDialog dialog = builder.create();
						dialog.show();
		}
	}
}
