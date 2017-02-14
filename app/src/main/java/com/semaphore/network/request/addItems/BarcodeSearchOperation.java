package com.semaphore.network.request.addItems;

import java.util.Map;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.network.request.SSOperation;
import com.semaphore.standardsupply.model.User;

public class BarcodeSearchOperation extends SSOperation {

	public BarcodeSearchOperation(ISSCallback callback, String barcode) {
		super("/barcode_search.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken() + "&laser_scan=" + barcode, callback);
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
