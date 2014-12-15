package com.semaphore.network.request;

import java.util.Map;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.standardsupply.model.User;

public class GetJobFlagOperation extends SSOperation {

	public GetJobFlagOperation(ISSCallback callback) {
		super("/customers/"+ User.currentUser().customer_id +".json?user_email="+User.currentUser().email + "&auth_token=" + User.currentUser().getAuthToken() , callback);
	}

	@Override
	protected String getHttpMethod() {
		return METHOD_GET;
	}
	
	@Override
	protected Map<String, Object> getRequest() {
		return null;
	}


	@Override
	public boolean isAuthenticationRequired() {
		return false;
	}
}
