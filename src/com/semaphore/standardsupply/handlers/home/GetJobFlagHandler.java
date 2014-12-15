package com.semaphore.standardsupply.handlers.home;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.GetJobFlagOperation;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.Settings;

public class GetJobFlagHandler extends Handler {

	public GetJobFlagHandler(BaseFragment fragment) {
		super(fragment);
	}

	@Override
	public void request() {
		GetJobFlagOperation operation = new GetJobFlagOperation(this);
		HttpAsync.makeRequest(operation);
	}
	
	@Override
	public void callback(String result) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(result);
		JsonObject obj = element.getAsJsonObject();
		Object jobFlag = gson.fromJson(obj.get("settings"), Object.class);
		if(jobFlag == null){
			return;
		}
		if(jobFlag.toString().contains("true")){
			Settings.putJobFlag(HomeActivity.getActivity(), "Y");
		}
		else{
			Settings.putJobFlag(HomeActivity.getActivity(), "N");
		}
	}

}
