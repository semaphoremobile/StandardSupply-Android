package com.semaphore.standardsupply.fragments.cart;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.cart.SubmitOrderHandler;
import com.semaphore.standardsupply.model.Address;
import com.semaphore.standardsupply.model.CartItem;
import com.semaphore.standardsupply.model.Job;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;
import com.squareup.picasso.Picasso;

public class SubmitCartFragment extends BaseFragment {

	SubmitOrderHandler handler;
	RelativeLayout progressLayout;
	ListView lstCartItems;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_submit_cart, container, false);
		progressLayout = (RelativeLayout) v.findViewById(R.id.progressBarLayout);
		lstCartItems = (ListView) v.findViewById(R.id.lstCartItems);
		return v;
	}
	
	@Override
	protected String getName() {
		return "Submit cart";
	}
	
	@Override
	public void onViewWillAppear() {
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(new CartItem());
		items.add(new CartItem());
		items.add(new CartItem());
		items.addAll(Model.getInstance().getCart().items);
		lstCartItems.setAdapter(new SubmitCartItemsAdapter(getActivity(), R.id.lstCartItems, items));
        getActivity().getActionBar().setTitle("Submit order");
	}
	public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_order, menu);
	}
	
	@Override
	public void onPrepareOptionsMenu (Menu menu) {
	    if (handler != null && handler.isInProgress){
	        menu.getItem(0).setEnabled(false);
	        
	        progressLayout.setVisibility(View.VISIBLE);
	    }
	    else{
	    	menu.getItem(0).setEnabled(true);
	    	progressLayout.setVisibility(View.GONE);
	    }
	}

	@SuppressLint("CommitTransaction")
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if(item.getItemId() == R.id.action_order){
			if(handler != null && handler.isInProgress){
				return false;
			}
			handler = new SubmitOrderHandler((SSActivity) getActivity());
			handler.request();
			getActivity().invalidateOptionsMenu();
			return false;
		}
		else if(item.getItemId() == R.id.action_cancel){
			getActivity().onBackPressed();
			return false;
		}
		return super.onOptionsItemSelected(item);
	}
}


class SubmitCartItemsAdapter extends ArrayAdapter<CartItem>{

	private ArrayList<CartItem> cartItems;
	Context context;
	public SubmitCartItemsAdapter(Context context,
			int textViewResourceId, ArrayList<CartItem> objects ) {
		super(context, textViewResourceId, objects);
		this.cartItems = new ArrayList<CartItem>();
		this.cartItems.addAll(objects);
		this.context = context;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position == 0){
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_change_truck, parent, false);
			}
			TextView txtTruckNo = (TextView) convertView.findViewById(R.id.txtTruckNo);
			txtTruckNo.setText("" + Settings.getTruck((Activity) context).name);
			return convertView;
		}
		else if(position == 1){
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_po, parent, false);
			}
			TextView txtPO = (TextView) convertView.findViewById(R.id.txtPO);
			txtPO.setText(Model.getInstance().getCart().PO);
			return convertView;
		}
		else if(position == 2){
			if(Model.getInstance().getCart().deliveryMethod.equals(context.getString(R.string.delivery))){
				if(convertView == null){
					LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.row_address, parent, false);
				}
				
				TextView txtItemName = (TextView) convertView.findViewById(R.id.txtAddr1);
				Address address = Model.getInstance().getCart().address;
				txtItemName.setText(address.name);

				TextView txtItemDesc = (TextView) convertView.findViewById(R.id.txtAddr2);
				txtItemDesc.setText(address.address);


				TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtAddr3);
				String tmpTxtAddr3 = address.city;
				if(tmpTxtAddr3 != null && tmpTxtAddr3.length() > 0 && address.state != null && address.state.length() > 0) {
					tmpTxtAddr3 += ", ";
				}
				tmpTxtAddr3 += address.state;
				if(tmpTxtAddr3 != null && tmpTxtAddr3.length() > 0 && address.getZipCode() != null && address.getZipCode().length() > 0) {
					tmpTxtAddr3 += ", ";
				}
				tmpTxtAddr3 += address.getZipCode();
				txtQuantity.setText(tmpTxtAddr3);

			}
			else{
				if(convertView == null){
					LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.row_location, parent, false);
				}
				TextView txtLocation = (TextView) convertView.findViewById(R.id.txtLocation);
				txtLocation.setText(Model.getInstance().getCart().location.name);
			}
			return convertView;
		}
		if(convertView == null){	
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_cart_item, parent, false);
		}
		TextView txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
		txtItemName.setText(cartItems.get(position).getItemId());
		
		TextView txtItemDesc = (TextView) convertView.findViewById(R.id.txtItemDetail);
		txtItemDesc.setText(cartItems.get(position).item_desc);
		
		
		TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
		txtQuantity.setText("" + cartItems.get(position).getQuantity());
		
		
		final TextView txtJobNos = (TextView) convertView.findViewById(R.id.txtJobNos);
		String jobNos = "Job Nos. ";
		for (Job job : cartItems.get(position).job_numbers) {
			jobNos += job.number + "(" + job.quantity + "), ";
		}
		jobNos = jobNos.substring(0, jobNos.length()-2);
		txtJobNos.setText(jobNos);

		ImageView imgCartItem = (ImageView) convertView.findViewById(R.id.imgCartItem);
		Picasso.with(context)
	    .load(cartItems.get(position).image)
	    .placeholder(R.drawable.no_item_image)
	    .error(R.drawable.no_item_image)
	    .into(imgCartItem);
		
		return convertView;
	}
	
	@Override
	public int getItemViewType(int position) {
		switch (position) {
		case 0:
			return 0;
		case 1: 
			return 1;
		case 2: 
			return 2;

		default:
			return 3;
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return 4;
	}
}
