package com.semaphore.standardsupply.handlers.cart;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.cart.SubmitOperation;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.Cart;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;

public class SubmitOrderHandler extends Handler {

	public boolean isInProgress;
	public SubmitOrderHandler(SSActivity activity) {
		super(activity);
	}
	
	public Cart cart;

	@Override
	public void request() {
		isInProgress = true;
		SubmitOperation operation = new SubmitOperation(this);
		operation.cart = cart;
		HttpAsync.makeRequest(operation);
	}
	
	@Override
	public void callback(String result) {
		isInProgress = false;
		activity.invalidateOptionsMenu();
		Model.getInstance().defaultJobNo = null;
		if(cart == null){
			cart = Model.getInstance().getCart();
		}
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		String str = activity.getResources().getString(com.semaphore.standardsupply.R.string.pick_up_now);
		if(cart.deliveryMethod != null &&  str.equals(cart.deliveryMethod) == false){
			//2. Chain together various setter methods to set the dialog characteristics
			builder.setMessage("Thank you for your order! Orders placed before 3pm will be ready by 8am. Orders placed after hours will be ready by 10am. Delivery orders will be coordinated directly by the delivering branch.")
		       .setTitle("Order Received");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Model.getInstance().resetCart();
		        	   Model.getInstance().persistCart();
		        		   while(activity.getFragmentManager().popBackStackImmediate()){
		            	   
		        		   }
		        		   if(activity instanceof HomeActivity) {
		        			   ((HomeActivity)activity).goHome();
		        		   }
		           }
		       });
		}
		else{
			//2. Chain together various setter methods to set the dialog characteristics
			builder.setMessage("Thank you!  Your order has been submitted and the " + (this.cart.location.id == Settings.getLocation(activity).id ? "default" : this.cart.location.name.toUpperCase(Locale.US)) + " branch has been notified. Please allow 15 minutes for your will call order to be ready.")
		       .setTitle("Order Received");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        		   while(activity.getFragmentManager().popBackStackImmediate()){
		            	   
		        		   }
		        		   if(activity instanceof HomeActivity) {
		        			   ((HomeActivity)activity).goHome();
		        		   }
		           }
		       });
		}
		

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	@Override
	public void error(String error){
		super.error(error);
		isInProgress = false;
		activity.invalidateOptionsMenu();
		
	}
}
