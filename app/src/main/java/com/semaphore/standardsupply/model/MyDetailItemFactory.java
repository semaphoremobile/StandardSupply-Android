package com.semaphore.standardsupply.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MyDetailItemFactory extends ItemFactory<CartItem> {

	public MyDetailItemFactory() {
		super("order_details", MyOrder.class);
	}

	@Override
	public ArrayList<CartItem> getItems(String result) {
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(result);
		JsonObject obj = element.getAsJsonObject();
		JsonArray arr =  obj.getAsJsonArray(listKey);
		Gson  gson = new Gson();
		for (Iterator<JsonElement> iterator = arr.iterator(); iterator.hasNext();) {
			JsonObject t = (JsonObject) iterator.next();
			CartItem cartItem = null;
			for(CartItem item : items){
				if(item.item_id.equals(t.get("item_id").toString())){
					cartItem = item;
					break;
				}
			}
			if(cartItem == null){
				cartItem = gson.fromJson(t, CartItem.class);
				cartItem.job_numbers = new ArrayList<Job>();
				items.add(cartItem);
			}
			
			Job job = new Job();
			job.number = t.get("job_number").toString();
			job.quantity = Integer.parseInt(t.get("num_items").toString());
			
			cartItem.job_numbers.add(job);
		}
		return items;
	}
}
