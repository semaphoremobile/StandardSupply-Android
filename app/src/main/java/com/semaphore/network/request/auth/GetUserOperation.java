package com.semaphore.network.request.auth;

import java.util.Map;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.network.request.SSOperation;
import com.semaphore.standardsupply.model.User;

public class GetUserOperation extends SSOperation {

	public GetUserOperation(ISSCallback callback) {
		super("/user.json?user_email="+User.currentUser().email + "&auth_token=" + User.currentUser().getAuthToken(), callback);
		// TODO Auto-generated constructor stub
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
