package com.semaphore.standardsupply.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.semaphore.network.request.LocationsOperation;
import com.semaphore.network.request.SSOperation;
import com.semaphore.network.request.addItems.BarcodeSearchOperation;
import com.semaphore.standardsupply.activity.HomeActivity;

public class Model {

	private static Model instance = null;
	protected Model() {
		// Exists only to defeat instantiation.
	}
	public static Model getInstance(){
		if(instance == null){
			instance = new Model();
		}
		return instance;
	}

	private ItemCache<ItemCategory> categoriesCache;
	public ItemCache<ItemCategory> getCategoriesCache(){
		if(categoriesCache == null){
			categoriesCache = new ItemCache<ItemCategory>("item_categories", ItemCategory.class);
			SSOperation operation = new SSOperation("/get_inventory_items.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken(), categoriesCache);
			operation.httpMethod = SSOperation.METHOD_GET;
			categoriesCache.operation = operation;
		}
		return categoriesCache;
	}

	private ItemCache<IdNameObj> locationsCache;
	public ItemCache<IdNameObj> getLocationsCache(){
		if(locationsCache == null){
			locationsCache = new ItemCache<IdNameObj>("locations", IdNameObj.class);
			SSOperation operation = new LocationsOperation(locationsCache);
			operation.httpMethod = SSOperation.METHOD_GET;
			locationsCache.operation = operation;
		}
		return locationsCache;
	}

	private ItemCache<Item> myFavoritesCache;
	public ItemCache<Item> getMyFavoritesCache(){
		if(myFavoritesCache == null){
			myFavoritesCache = new ItemCache<Item>("favorites", Item.class);
			SSOperation operation = new SSOperation("/get_user_favorites.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken(), myFavoritesCache);
			operation.httpMethod = SSOperation.METHOD_GET;
			myFavoritesCache.operation = operation;
		}
		return myFavoritesCache;
	}

	private ItemCache<Item> companyFavoritesCache;
	public ItemCache<Item> getCompanyFavoritesCache(){
		if(companyFavoritesCache == null){
			companyFavoritesCache = new ItemCache<Item>("favorites", Item.class);
			SSOperation operation = new SSOperation("/get_company_favorites.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken(), companyFavoritesCache);
			operation.httpMethod = SSOperation.METHOD_GET;
			companyFavoritesCache.operation = operation;
		}
		return companyFavoritesCache;
	}

	private ItemCache<ShoppingList> shoppingListsCache;
	public ItemCache<ShoppingList> getShoppingListsCache(){
		if(shoppingListsCache == null){
			shoppingListsCache = new ItemCache<ShoppingList>("shopping_lists", ShoppingList.class);
			SSOperation operation = new SSOperation("/get_shopping_lists.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken(), shoppingListsCache);
			operation.httpMethod = SSOperation.METHOD_GET;
			shoppingListsCache.operation = operation;
		}
		return shoppingListsCache;
	}

	private ItemCache<MyOrder> myOrdersCache;
	public ItemCache<MyOrder> getMyOrdersCache(){
		if(myOrdersCache == null){
			myOrdersCache = new ItemCache<MyOrder>("orders", MyOrder.class);
			SSOperation operation = new SSOperation("/get_orders.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken(), myOrdersCache);
			operation.httpMethod = SSOperation.METHOD_GET;
			myOrdersCache.operation = operation;
		}
		return myOrdersCache;
	}


	private ItemCache<Notification> notificationsCache;
	public ItemCache<Notification> getNotificationsCache(){
		if(notificationsCache == null){
			notificationsCache = new ItemCache<Notification>("notifications", Notification.class);
			SSOperation operation = new SSOperation("/notifications.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken(), notificationsCache);
			operation.httpMethod = SSOperation.METHOD_GET;
			notificationsCache.operation = operation;
		}
		return notificationsCache;
	}


	private ItemCache<IdNameObj> trucksCache;
	public ItemCache<IdNameObj> getTrucksCache(){
		if(trucksCache == null){
			trucksCache = new ItemCache<IdNameObj>("trucks", IdNameObj.class);
			SSOperation operation = new SSOperation("/get_user_trucks.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken(), trucksCache);
			operation.httpMethod = SSOperation.METHOD_GET;
			trucksCache.operation = operation;
		}
		return trucksCache;
	}


	private SparseArray<ItemCache<CartItem>> myOrderDetailArray;
	public ItemCache<CartItem> getMyOrderDetailCache(MyOrder myOrder) {
		if(myOrderDetailArray == null){

			myOrderDetailArray = new SparseArray<ItemCache<CartItem>>();
		}
		if(myOrderDetailArray.get(myOrder.order_id) == null){
			ItemCache<CartItem> detailCache = new ItemCache<CartItem>("order_details", CartItem.class);
			MyDetailItemFactory itemFactory = new MyDetailItemFactory();
			detailCache.itemFactory = itemFactory;	
			SSOperation operation = new SSOperation("/get_order_details.json?user_email="+ User.currentUser().email +"&auth_token=" + User.currentUser().getAuthToken() + "&order_id=" + myOrder.order_id, detailCache);
			operation.httpMethod = SSOperation.METHOD_GET;
			detailCache.operation = operation;
			myOrderDetailArray.put(myOrder.order_id, detailCache);
		}
		return myOrderDetailArray.get(myOrder.order_id);
	}

	private Map<String, ItemCache<Item>> itemDetailArray;
	public ItemCache<Item> getItemDetailCache(String barcode) {
		if(itemDetailArray == null){

			itemDetailArray = new HashMap<String, ItemCache<Item>>();
		}
		if(itemDetailArray.get(barcode) == null){
			ItemCache<Item> detailCache = new ItemCache<Item>("item", Item.class);
			BarcodeDetailFactory itemFactory = new BarcodeDetailFactory();
			detailCache.itemFactory = itemFactory;	
			BarcodeSearchOperation operation = new BarcodeSearchOperation(detailCache, barcode);
			detailCache.operation = operation;
			itemDetailArray.put(barcode, detailCache);
		}
		return itemDetailArray.get(barcode);
	}


	private ArrayList<Address> addresses;
	public ArrayList<Address> getAddresses(){
		if(addresses == null){
			addresses = Settings.loadAddresses();
		}
		return addresses;
	}
	private Cart cart;
	public Cart getCart(){
		if(cart == null){
			restoreCart();
		}
		return cart;
	}
	public void resetCart() {
		cart = new Cart();
	}
	public void persistCart() {
		SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.getActivity().getApplicationContext());
		Editor prefsEditor = appSharedPrefs.edit();
    	Gson gson = new Gson();
    	String json = gson.toJson(getCart());
    	prefsEditor.putString("persist_cart", json);
    	prefsEditor.commit();
    }
	public void restoreCart() {
		SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.getActivity().getApplicationContext());
		Gson gson = new Gson();
		String json = appSharedPrefs.getString("persist_cart", "");
		if("".equals(json)) {
			cart = new Cart();
		} else {
			cart = (Cart) gson.fromJson(json, Cart.class);
		}
	}
    
	public Advertisement advertisement;
	public Bitmap profileImage;
	public String avatarUrl;
	public String defaultJobNo;
	public void resest() {
		Settings.deleteAddresses();
		addresses = null;
		myOrdersCache = null;
		myOrderDetailArray = null;
		trucksCache = null;
		notificationsCache = null;
		categoriesCache = null;
		myFavoritesCache = null;
		companyFavoritesCache = null;
	}
}
