package com.semaphore.standardsupply.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ItemFactory<T> {

	protected String listKey;
	private Type type;
	public ItemFactory(String listKey, Type type){
		this.listKey = listKey;
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<T> getItems(String result) {
		ArrayList<T> items = new ArrayList<T>();
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(result);
		JsonObject obj = element.getAsJsonObject();
		JsonArray arr = obj.getAsJsonArray(listKey);
		if(arr == null){
			return items;
		}

		Gson  gson = new Gson();
		for (Iterator<JsonElement> iterator = arr.iterator(); iterator.hasNext();) {
			JsonObject t = (JsonObject) iterator.next();
			items.add((T) gson.fromJson(t, type));
		}
		return items;
	}

}
