package com.semaphore.standardsupply.fragments.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.model.Address;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;

public class NewAddressFragment extends BaseFragment {

	TextView txtName, txtAddress, txtCity, txtState, txtZip;
	Button btnAdd;
	
	@Override
	protected String getName() {
		return "New Address";
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_new_address, container, false);
		txtName = (TextView) v.findViewById(R.id.txtName);
		txtAddress = (TextView) v.findViewById(R.id.txtAddress);
		txtCity = (TextView) v.findViewById(R.id.txtCity);
		txtState = (TextView) v.findViewById(R.id.txtState);
		txtZip = (TextView) v.findViewById(R.id.txtZip);
		btnAdd = (Button) v.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(addListener);
		
		return v;
	}
	
	public void onViewWillAppear() {
		getActivity().getActionBar().setTitle("Add a new address");
	}
	
	OnClickListener addListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Address addr = new Address();
			addr.name = txtName.getText().toString();
			addr.address = txtAddress.getText().toString();
			addr.city = txtCity.getText().toString();
			addr.state = txtState.getText().toString();
			addr.zip = txtZip.getText().toString();
			
			Model.getInstance().getAddresses().add(addr);
			Settings.addAddress(addr);
			pop();
		}
	};
}
