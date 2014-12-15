package com.semaphore.network.request.addItems;

import java.util.HashMap;
import java.util.Map;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.network.request.SSOperation;
import com.semaphore.standardsupply.model.User;

public class CreateFavoriteOperation extends SSOperation {

	public int id;
	public CreateFavoriteOperation(ISSCallback callback) {
		super("/create_user_favorite.json", callback);
	}
	
	@Override
	protected Map<String, Object> getRequest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("user_email", User.currentUser().email);
		map.put("auth_token", User.currentUser().getAuthToken());
		
		return map;
	}

}
