package com.semaphore.network.request.auth;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.network.request.SSOperation;
import com.semaphore.standardsupply.model.User;

public class UpdateUserOperation extends SSOperation {

	public User user;
	
	public UpdateUserOperation(ISSCallback callback) {
		super("/update_user.json", callback);
	}
	
	@Override
	protected String getHttpMethod() {
		return METHOD_PUT;
	}
	
	@Override
	public String getRequestString() {
		String str = "user_email="+ User.currentUser().email +"&auth_token="+ User.currentUser().getAuthToken() +"&user[last_name]="+ user.last_name +"&user[first_name]="+ user.first_name +"&user[customer_id]="+ user.customer_id  +"&user[default_branch_id]="+ user.default_branch_id +"&user[truck_name]="+ user.truck_name;
		if(user.imageUrl != null){
			str += "&user[image_type]=png&user[image]=" + user.imageUrl;
		}
		return str;
	}

}
