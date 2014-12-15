package com.semaphore.network.request.addItems;

import java.util.Map;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.network.request.SSOperation;
import com.semaphore.standardsupply.model.User;

public class DeleteFavoriteOperation extends SSOperation {

	public DeleteFavoriteOperation(ISSCallback callback, int inv_mast_uid) {
		super("/delete_user_favorite.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken() + "&id=" + inv_mast_uid, callback);
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
	
	@Override
	public boolean isAuthenticationRequired() {
		return false;
	}
	
}
