package com.semaphore.standardsupply.handlers;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.fragments.BaseFragment;

/**
 * A base class for handling the different network requests to be made by SSActivities.
 * @author arjavdave
 *
 */
public abstract class Handler implements ISSCallback {

	/**
	 * The activity that makes the request
	 */
	protected SSActivity activity;
	
	public Handler(SSActivity activity){
		this.activity = activity;
	}
	
	
	/**
	 * The activity that makes the request
	 */
	protected BaseFragment fragment;
	
	public Handler(BaseFragment fragment){
		this.fragment = fragment;
	}
	
	/**
	 * SSActivity should call this method to make a network request. 
	 * Before calling this method the activity should set the required params for making a network call.
	 */
	public abstract void request();
	
	
	@Override
	public void callback(String result) {
		// Do nothing
	}

	
	@Override
	public void error(String error) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(activity == null ? fragment.getActivity() : activity);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(error)
		       .setTitle("Error");
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	           }
	       });

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
