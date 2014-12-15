package com.semaphore.standardsupply.model;

import java.util.ArrayList;

public class Cart {
	
	public Cart(){
		items = new ArrayList<CartItem>();
	}
	public ArrayList<CartItem> items;
	public String PO;
	public String truckId;
	public IdNameObj location;
	public String deliveryMethod;
	public Address address;
	public String defaultJobNo;

}
