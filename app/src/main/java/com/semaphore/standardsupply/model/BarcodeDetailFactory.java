package com.semaphore.standardsupply.model;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BarcodeDetailFactory extends ItemFactory<Item> {

	public BarcodeDetailFactory() {
		super("item",Item.class);
	}
	
	@Override
	public ArrayList<Item> getItems(String result) {
		ArrayList<Item> items = new ArrayList<Item>();
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(result);
		JsonObject obj = element.getAsJsonObject();
		JsonElement object =  obj.get(listKey);

		if((object instanceof JsonNull) == false){
			Gson  gson = new Gson();
			Item item = gson.fromJson(object, Item.class);
			items.add(item);
		}
		return items;
	}

}
