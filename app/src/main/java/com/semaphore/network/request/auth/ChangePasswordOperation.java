package com.semaphore.network.request.auth;

import java.util.HashMap;
import java.util.Map;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.network.request.SSOperation;
import com.semaphore.standardsupply.model.User;

public class ChangePasswordOperation extends SSOperation {

	public String newPassword;
	public String currentPassword;
	
	public ChangePasswordOperation(ISSCallback callback) {
		super("/change_password.json", callback);
	}
	
	@Override
	protected Map<String, Object> getRequest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("current_password", currentPassword);
		map.put("new_password", newPassword);
		map.put("user_email", User.currentUser().email);
		map.put("auth_token", User.currentUser().getAuthToken());
		return map;
	}

}
