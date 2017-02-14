package com.semaphore.network.request;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.standardsupply.model.User;

public class SignupOperation extends SSOperation {

	public User user;
	public SignupOperation(ISSCallback callback) {
		super("", callback);
	}

	
	@Override
	protected String getActionUrl() {
		return "http://stockmytruck.com/admin/users.json"; //Staging: 162.242.148.122
//		return "http://206.72.198.49:3000/api/admin/users.json"; //Staging: 162.242.148.122
	//	return "http://206.72.198.49:3000/admin/users.json"; //Staging: 162.242.148.122
	}
	
	@Override
	public String getRequestString() {
	return "user[email]="+ user.email  +"&user[last_name]="+  user.last_name +"&user[first_name]="+ user.first_name  +"&user[password]="+  user.password +"&user[password_confirmation]="+ user.password_confirmation  +"&user[customer_id]="+ user.customer_id +"&user[default_branch_id]="+ user.default_branch_id  +"&user[device_uid]="+ user.device_uid +"&user[truck_name]="+ user.truck_name;  
	}
	
}
