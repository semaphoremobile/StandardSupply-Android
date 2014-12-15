package com.semaphore.standardsupply.handlers.cart;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.cart.GetAddressOperation;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.cart.AddressesFragment;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.Address;
import com.semaphore.standardsupply.model.Model;

public class AddressesHandler extends Handler{

	
	public AddressesHandler(BaseFragment fragment){
		super(fragment);
	}

	@Override
	public void request() {
		GetAddressOperation operation = new GetAddressOperation(this);
		HttpAsync.makeRequest(operation);
	}
	
	@Override
	public void callback(String result) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(result);
		JsonElement obj = element.getAsJsonObject().get("address");
		Gson  gson = new Gson();
		Address address = gson.fromJson(obj, Address.class);
		Model.getInstance().getAddresses().add(address);
		AddressesFragment fragment = (AddressesFragment) this.fragment;
		fragment.update();
		
	}
}


