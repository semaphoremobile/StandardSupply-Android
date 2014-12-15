package com.semaphore.standardsupply.fragments.cart;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.model.IdNameObj;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;

public class PrepCartFragment extends BaseFragment implements Observer {

	EditText txtPO;
	EditText txtBranch;
	LinearLayout layoutBranch;
	IdNameObj location;
	String deliveryMethod;
	LinearLayout btnWillCall;
	LinearLayout btnDelivery;
	TextView txtChooseBranch;

	@Override
	protected String getName() {
		return "PO/Delivery method";
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_prep_cart, container, false);
		txtPO = (EditText) v.findViewById(R.id.txtPO);
		txtBranch = (EditText) v.findViewById(R.id.txtBranchId);
		layoutBranch = (LinearLayout) v.findViewById(R.id.layoutBranch);
		layoutBranch.setOnClickListener(branchListener);
		location = Settings.getLocation(getActivity());
        
        ArrayList<IdNameObj> items = Model.getInstance().getLocationsCache().items;
		if(items == null || items.size() == 0){
			Model.getInstance().getLocationsCache().addObserver(this);
			Model.getInstance().getLocationsCache().request();
		}
		if(location != null){
			txtBranch.setText(location.name);
		}
		else if(items != null && items.size() > 0){
			location = items.get(0);
		}
		txtChooseBranch = (TextView) v.findViewById(R.id.txtChooseBranch);
		btnWillCall = (LinearLayout) v.findViewById(R.id.btnWillCall);
		btnDelivery = (LinearLayout) v.findViewById(R.id.btnDelivery);
		btnWillCall.setOnClickListener(willCallMethodListener);
		btnDelivery.setOnClickListener(deliveryMethodListener);
		return v;
	}
	
	@Override
	public void onViewWillAppear() {
		txtBranch.setText(location == null ? "" : location.name);
		getActivity().getActionBar().setTitle("Order Details");
		
	}

	public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_next, menu);
	}

	@SuppressLint("CommitTransaction")
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if(item.getItemId() == R.id.action_next){
			if(deliveryMethod == null){
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

				builder.setMessage("Please select a delivery method").setNeutralButton("OK", null);

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
				return false;
			}
			if(txtPO.getText().toString().equals("")){
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

				builder.setMessage("Please enter PO number").setNeutralButton("OK", null);

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
				return false;
			}
			Model.getInstance().getCart().PO = txtPO.getText().toString();
			Model.getInstance().getCart().location = location;
			Model.getInstance().getCart().deliveryMethod = deliveryMethod;
			Model.getInstance().persistCart();
			if(deliveryMethod.equals(getString(R.string.will_call))){
				add(getActivity().getFragmentManager().beginTransaction(), new SubmitCartFragment());
			}
			else{
				add(getActivity().getFragmentManager().beginTransaction(), new AddressesFragment(), "AddressesFragment");
			}
			return false;
		}
		return super.onOptionsItemSelected(item);
	}


	OnClickListener branchListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


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
					txtBranch.setText(location.name);
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

	OnClickListener deliveryMethodListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			deliveryMethod = getString(R.string.delivery);
			layoutBranch.setVisibility(View.GONE);
			txtChooseBranch.setVisibility(View.GONE);
			//Set delivery cell
			v.setBackgroundColor(getResources().getColor(R.color.medium_gray));
			v.findViewById(R.id.imgDelivery).setVisibility(View.VISIBLE);
			((TextView)v.findViewById(R.id.txtDelivery)).setTextColor(getResources().getColor(R.color.ss_green));

			//Set Will call cell
			btnWillCall.setBackgroundColor(getResources().getColor(R.color.light_gray));
			btnWillCall.findViewById(R.id.imgWillCall).setVisibility(View.INVISIBLE);
			((TextView)btnWillCall.findViewById(R.id.txtWillCall)).setTextColor(getResources().getColor(R.color.gray));

		}
	};

	OnClickListener willCallMethodListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setMessage("Will call orders placed before 3pm will be ready for pickup tomorrow after 8am. Orders placed after 3pm will be ready after 10am tomorrow.");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@SuppressLint("CommitTransaction")
				public void onClick(DialogInterface dialog, int id) {

				}
			});

			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();


			deliveryMethod = getString(R.string.will_call);
			layoutBranch.setVisibility(View.VISIBLE);
			txtChooseBranch.setVisibility(View.VISIBLE);
			//Set delivery cell
			v.setBackgroundColor(getResources().getColor(R.color.medium_gray));
			v.findViewById(R.id.imgWillCall).setVisibility(View.VISIBLE);
			((TextView)v.findViewById(R.id.txtWillCall)).setTextColor(getResources().getColor(R.color.ss_green));

			//Set Will call cell
			btnDelivery.setBackgroundColor(getResources().getColor(R.color.light_gray));
			btnDelivery.findViewById(R.id.imgDelivery).setVisibility(View.INVISIBLE);
			((TextView)btnDelivery.findViewById(R.id.txtDelivery)).setTextColor(getResources().getColor(R.color.gray));

		}
	};
	

	@Override
	public void update(Observable arg0, Object arg1) {
		Model.getInstance().getLocationsCache().deleteObserver(this);
		ArrayList<IdNameObj> items = Model.getInstance().getLocationsCache().items;
		if(items == null || items.size() == 0){
			// 1. Instantiate an AlertDialog.Builder with its constructor
						AlertDialog.Builder builder = new AlertDialog.Builder(PrepCartFragment.this.getActivity());
						
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
		else{
			if(location == null){
				location = items.get(0);
				Settings.putLocation(getActivity(), location);
				txtBranch.setText(location.name);
			}
			else if(location.name == null){
				for (IdNameObj loc : items) {
					if(loc.id == location.id){
						location = loc;
						Settings.putLocation(getActivity(), location);
						txtBranch.setText(location.name);
						break;
					}
				}
			}
		}
	}

}
