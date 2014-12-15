package com.semaphore.standardsupply.handlers.cart;

import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.home.addItems.ItemDetailFragment;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.Model;

public class ScanHandler extends Handler implements Observer {

	private String barcode;

	public ScanHandler(BaseFragment fragment, String barcode) {
		super(fragment);
		if(barcode.startsWith("0")){
			barcode = barcode.substring(1);
		}
		this.barcode = barcode;
	}

	@Override
	public void request() {
		Model.getInstance().getItemDetailCache(barcode).addObserver(this);
		Model.getInstance().getItemDetailCache(barcode).request();
	}

	@SuppressLint("CommitTransaction")
	@Override
	public void update(Observable arg0, Object arg1) {
		Model.getInstance().getItemDetailCache(barcode).deleteObserver(this);

		if(Model.getInstance().getItemDetailCache(barcode).items.size() > 0){
			ItemDetailFragment idFragment = (ItemDetailFragment) fragment;
			idFragment.receivedBarcode(barcode);
		}
		else{
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

			// 2. Chain together various setter methods to set the dialog characteristics
			builder.setMessage("Sorry the Item was not found!!!")
			       .setTitle("");
			
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   ItemDetailFragment idFragment = (ItemDetailFragment) fragment;
		   				idFragment.barcodeNotFound();
		           }
		       });

			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
}
