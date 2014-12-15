package com.semaphore.network.request;

import java.util.HashMap;
import java.util.Map;

import com.semaphore.network.helper.ISSCallback;

public class ForgotPasswordOperation extends SSOperation {

	public ForgotPasswordOperation(ISSCallback callback) {
		super("/forgotten_password.json", callback);
	}
	
	public String email;
	
	@Override
	protected Map<String, Object> getRequest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_email", email);
		return map;
	}

}
