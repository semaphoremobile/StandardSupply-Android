package com.semaphore.standardsupply.fragments.home.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.home.profile.AddTruckHandler;

public class AddTruckFragment extends BaseFragment {

	EditText txtTruckName;
	Button btnAddTruck;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_add_truck, container, false);
		txtTruckName = (EditText) v.findViewById(R.id.txtTruckName);
		btnAddTruck = (Button) v.findViewById(R.id.btnAddTruck);
		btnAddTruck.setOnClickListener(addTruckListener);
		return v;
	}
	@Override
	protected String getName() {
		return "Add Truck";
	}
	
	public void onViewWillAppear() {
		getActivity().getActionBar().setTitle("Add a truck");
	}
	
	OnClickListener addTruckListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AddTruckHandler handler = new AddTruckHandler(AddTruckFragment.this, txtTruckName.getText().toString());
			handler.request();
		}
	};
}
