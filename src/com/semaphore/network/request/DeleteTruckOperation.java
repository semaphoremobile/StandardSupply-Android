package com.semaphore.network.request;


import com.semaphore.network.helper.ISSCallback;
import com.semaphore.standardsupply.model.User;

public class DeleteTruckOperation extends SSOperation {

	public DeleteTruckOperation(int truckId, ISSCallback callback) {
		super("/trucks/" + truckId + "?user_email="+User.currentUser().email + "&auth_token=" + User.currentUser().getAuthToken(), callback);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected String getHttpMethod() {
		return METHOD_DELETE;
	}
}
