package com.semaphore.standardsupply.handlers.home;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.GetAdvertisementOperation;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.IAdFragment;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.Advertisement;
import com.semaphore.standardsupply.model.Model;

public class AdvertisementHandler extends Handler {

	private int width;
	public AdvertisementHandler(BaseFragment fragment, int width) {
		super(fragment);
		this.width = width;
	}

	@Override
	public void request() {
		GetAdvertisementOperation operation = new GetAdvertisementOperation(this, width);
		HttpAsync.makeRequest(operation);
	}

	@Override
	public void callback(String result) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(result);
		JsonObject obj = element.getAsJsonObject();
		Advertisement ad = gson.fromJson(obj.get("advertisement"), Advertisement.class);
		Model.getInstance().advertisement = ad;
		((IAdFragment)fragment).receivedAd();
	}
	@Override
	public void error(String error) {
		//Do nothing
	}

}
