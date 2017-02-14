package com.semaphore.network.request.cart;

import java.util.Map;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.network.request.SSOperation;
import com.semaphore.standardsupply.model.User;

public class GetAddressOperation extends SSOperation {

	public GetAddressOperation(ISSCallback callback) {
		super("/get_customer_address.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken() , callback);
	}
	
	@Override
	protected String getHttpMethod() {
		return METHOD_GET;
	}
	
	@Override
	protected Map<String, Object> getRequest() {
		return null;
	}

}
