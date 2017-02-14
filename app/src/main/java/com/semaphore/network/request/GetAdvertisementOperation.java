package com.semaphore.network.request;

import java.util.Map;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.standardsupply.model.User;

public class GetAdvertisementOperation extends SSOperation {

	public GetAdvertisementOperation(ISSCallback callback, int width) {
		super("/get_advertisement.json?user_email="+User.currentUser().email + "&auth_token=" + User.currentUser().getAuthToken() + "&width="+ width + "&device_type=a", callback);
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
