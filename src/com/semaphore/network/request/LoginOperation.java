package com.semaphore.network.request;

import java.util.HashMap;
import java.util.Map;

import com.semaphore.network.helper.ISSCallback;

public class LoginOperation extends SSOperation {

	public String email;
	public String password;
	
	public LoginOperation(ISSCallback callback) {
		super("/remote_signin.json", callback);
	}
	
	@Override
	protected Map<String, Object> getRequest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_email", this.email);
		map.put("password", this.password);
		return map;
	}

}
