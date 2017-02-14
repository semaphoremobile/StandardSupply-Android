package com.semaphore.standardsupply.handlers.helplinks;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.HelpLinkOperation;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.Help;
import com.semaphore.standardsupply.model.Model;

public class HelpLinksHandler extends Handler {

	public HelpLinksHandler(BaseFragment fragment) {
		super(fragment);
	}

	@Override
	public void request() {
		HelpLinkOperation operation = new HelpLinkOperation(this);
		HttpAsync.makeRequest(operation);
	}

	@Override
	public void callback(String result) {
		Log.i("HELP LINK URL RESPONSE::", result);
		// Gson gson = new Gson();
		// JsonParser parser = new JsonParser();
		// JsonElement element = parser.parse(result);
		//
		// JsonObject obj = element.getAsJsonObject();
		// // Type type = new TypeToken<List<Help>>() {
		// // }.getType();
		// List<Helplinks> helpList = (List<Helplinks>) gson.fromJson(
		// obj.get("helplinks"), Helplinks.class);
		//
		// // Gson gson = new Gson();
		// // List<CustomerInfo> customers =
		// // (List<CustomerInfo>)gson.fromJson(result,CustomerInfo.class);
		JsonParser jsonParser = new JsonParser();
		JsonObject jo = (JsonObject) jsonParser.parse(result);
		JsonArray jsonArr = jo.getAsJsonArray("helplink");
		// jsonArr.
		Gson googleJson = new Gson();
		Type listType = new TypeToken<List<Help>>() {
		}.getType();
		ArrayList<Help> jsonObjList = (ArrayList<Help>) googleJson.fromJson(
				jsonArr, listType);

		// // Gson gson = new Gson();
		// // JsonParser parser = new JsonParser();
		// // JsonElement element = parser.parse(result);
		// //
		// // JsonObject obj = element.getAsJsonObject();
		// // // Help help = gson.fromJson(obj.get("url"), Help.class);
		// // ArrayList<Help> helpList =new ArrayList<Help>();
		// // Type listType = new TypeToken<List<Help>>() {
		// // }.getType();
		// // ArrayList<Help> helpList = (ArrayList<Help>) gson.fromJson(
		// // obj.get("helplinks"), listType);
		// // Log.d("HELP LINK URL RESPONSE::", helpList.size() + "");
		//
		Log.d("HELP LINK URL RESPONSE::", jsonObjList.size() + "");
		Model.getInstance().persistHelpLinkInfo(jsonObjList);
		// HelpInfoFragment fragment = (HelpInfoFragment) this.fragment;
		// fragment.update();
		// ((IAdFragment) fragment).receivedAd();
	}

}
