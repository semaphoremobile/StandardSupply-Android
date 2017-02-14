package com.semaphore.standardsupply.handlers.home.profile;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.auth.GetUserOperation;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.home.profile.ProfileFragment;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.User;

public class GetUserHandler extends Handler {

	public GetUserHandler(BaseFragment fragment) {
		super(fragment);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void request() {
		GetUserOperation operation = new GetUserOperation(this);
		HttpAsync.makeRequest(operation);
	}
	
	@Override
	public void callback(String result) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(result);
		JsonObject obj = element.getAsJsonObject();
		User user = gson.fromJson(obj.get("user"), User.class);
		if(user.getAvatarUrl() != null){
			Model.getInstance().avatarUrl = user.getAvatarUrl();
		}
		else{
			Model.getInstance().avatarUrl = null;
		}
		
		((ProfileFragment)this.fragment).receivedImage();
	}

}
