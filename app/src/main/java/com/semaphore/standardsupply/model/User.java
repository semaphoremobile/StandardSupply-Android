package com.semaphore.standardsupply.model;

import java.io.Serializable;

import com.semaphore.standardsupply.activity.HomeActivity;

public class User implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 2992690404008520549L;
	
	public String name;
	public String email;
	public String first_name;
	public String last_name;
	public int customer_id;
	public int default_branch_id;
	public String location_name;
	public String device_uid;
	public String password;
	public int truck_id;
	public String truck_name;
	public String employee_id;
	private String avatar_url;
	
	public int account_id;
	public int id;
	private String auth_token;
	public String password_confirmation;

	public String imageUrl;
	

	public String getAvatarUrl(){
		if(avatar_url != null && this.avatar_url.indexOf("http://") < 0){
			return "http://162.242.148.122"  + avatar_url;
		}
		return avatar_url;
	}
	public String getAuthToken(){
		return auth_token;
	}
	public static User currentUser(){
		return Settings.getUser(HomeActivity.getActivity());
	}
}
