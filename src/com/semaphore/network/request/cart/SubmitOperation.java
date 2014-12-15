package com.semaphore.network.request.cart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.network.request.SSOperation;
import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.model.Address;
import com.semaphore.standardsupply.model.Cart;
import com.semaphore.standardsupply.model.CartItem;
import com.semaphore.standardsupply.model.Job;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;
import com.semaphore.standardsupply.model.User;

public class SubmitOperation extends SSOperation {

	public SubmitOperation(ISSCallback callback) {
		super("/create_order.json", callback);
	}

	public Cart cart;

	@Override
	protected Map<String, Object> getRequest() {
		if(cart == null){
			cart = Model.getInstance().getCart();
		}
		Map<String, String> addressHash = null;
		String orderType = "";
		if(cart.deliveryMethod.equals(HomeActivity.getActivity().getString(R.string.will_call))){
			orderType = "W";
		}
		else if(cart.deliveryMethod.equals(HomeActivity.getActivity().getString(R.string.delivery))){
			orderType = "D";
			Address address = Model.getInstance().getCart().address;
			addressHash = new HashMap<String, String>();
			addressHash.put("name", address.name);
			addressHash.put("address", address.address);
			addressHash.put("city", address.city);
			addressHash.put("state", address.state);
			addressHash.put("zip", address.getZipCode());
		}
		else if(cart.deliveryMethod.equals(HomeActivity.getActivity().getString(R.string.pick_up_now))){
			orderType = "O";
		}
		ArrayList<Map<String, Object>> order_items = new  ArrayList<Map<String,Object>>();
		for (CartItem item : cart.items) {
			for(Job job : item.job_numbers){
				Map<String, Object> mapItem = new HashMap<String, Object>();
				mapItem.put("inv_mast_uid", item.getInvId());
				mapItem.put("quantity", item.getQuantity());
				mapItem.put("job_number", job.number);
				order_items.add(mapItem);
			}	
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String dateString = sdf.format(new Date()) + "+0000";
		Map<String,Object> order = new HashMap<String,Object>();
		order.put("order_date", dateString);
		order.put("customer_id", User.currentUser().customer_id);
		order.put("truck_id", Settings.getTruck(HomeActivity.getActivity()).id);
		order.put("location_id", cart.location.id);
		order.put("po_number", cart.PO);
		order.put("order_type", orderType);
		if(addressHash != null){
			order.put("custom_address", addressHash);
		}
		
		Map<String,Object> mainOrder = new HashMap<String, Object>();
		mainOrder.put("order", order);
		mainOrder.put("order_items", order_items);
		mainOrder.put("user_email", User.currentUser().email);
		mainOrder.put("auth_token", User.currentUser().getAuthToken());
		
		
		
		return mainOrder;
	}
	
	@Override
	public String getRequestString() {
		// TODO Auto-generated method stub
		return "order=" + super.getRequestString();
	}

}
