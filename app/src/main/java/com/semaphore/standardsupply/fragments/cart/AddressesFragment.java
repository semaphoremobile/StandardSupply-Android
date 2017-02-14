package com.semaphore.standardsupply.fragments.cart;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.cart.AddressesHandler;
import com.semaphore.standardsupply.model.Address;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;

public class AddressesFragment extends BaseFragment{

	LinearLayout btnAddAddress;
	ListView lstAddresses;
	Address address;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_address_selector, container, false);
		btnAddAddress = (LinearLayout) v.findViewById(R.id.btnAddAddress);
		btnAddAddress.setOnClickListener(addListener);
		lstAddresses = (ListView) v.findViewById(R.id.lstAddresses);
		
		if(Model.getInstance().getAddresses().size() == Settings.loadAddresses().size()){
			AddressesHandler handler = new AddressesHandler(this);
			handler.request();
		}
		return v;
	}

	public void onViewWillAppear() {
		
		if(Model.getInstance().getAddresses().size() != Settings.loadAddresses().size()){
			setList();
		}
		getActivity().getActionBar().setTitle("Select Address");
	}
	
	OnClickListener addListener = new OnClickListener() {
		@SuppressLint("CommitTransaction")
		@Override
		public void onClick(View v) {
			add(getActivity().getFragmentManager().beginTransaction(), new NewAddressFragment());	
		}
	};

	private void setList(){
		AddressesAdapter adapter = new AddressesAdapter(getActivity(), R.id.lstAddresses, Model.getInstance().getAddresses());
		lstAddresses.setAdapter(adapter);
		lstAddresses.setOnItemClickListener(adapter.itemClickListener);
		lstAddresses.refreshDrawableState();
	}

	public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_next, menu);
	}


	@SuppressLint("CommitTransaction")
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if(item.getItemId() == R.id.action_next){
			if(Model.getInstance().getCart().address == null){
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

				builder.setMessage("Please select an address").setNeutralButton("OK", null);

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			else{
				add(getActivity().getFragmentManager().beginTransaction(), new SubmitCartFragment());
			}
			return false;
		}
		return super.onOptionsItemSelected(item);
	}

	public void update() {
		setList();
	}

	@Override
	protected String getName() {
		return "Addressess";
	}
}
	

class AddressesAdapter extends ArrayAdapter<Address>{

	private ArrayList<Address> addresses;
	Context context;
	public AddressesAdapter(Context context,
			int textViewResourceId, ArrayList<Address> objects ) {
		super(context, textViewResourceId, objects);
		this.addresses = new ArrayList<Address>();
		this.addresses.addAll(objects);
		this.context = context;

	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Model.getInstance().getCart().address = addresses.get(arg2);
			notifyDataSetChanged();
			notifyDataSetInvalidated();
			invalidated = true;
		}
	};
	private boolean invalidated = false;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null || invalidated  == true){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(Model.getInstance().getCart().address == addresses.get(position)){
				convertView = inflater.inflate(R.layout.row_address_selected, parent, false);
			}
			else{
				convertView = inflater.inflate(R.layout.row_list_address, parent, false);
			}
		}
		TextView txtItemName = (TextView) convertView.findViewById(R.id.txtAddr1);
		txtItemName.setText(addresses.get(position).name);

		TextView txtItemDesc = (TextView) convertView.findViewById(R.id.txtAddr2);
		txtItemDesc.setText(addresses.get(position).address);


		TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtAddr3);
		txtQuantity.setText(addresses.get(position).city + ", " + addresses.get(position).state + ", " + addresses.get(position).getZipCode());


		return convertView;
	}
}
