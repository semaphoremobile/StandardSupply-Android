package com.semaphore.standardsupply.model;

import java.util.ArrayList;

public class CartItem extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8887987181721326739L;

	public static CartItem cartItemForItem(Item item){
		if(item == null){
			return new CartItem();
		}
		CartItem  cartItem = new CartItem();
		cartItem.id = item.id;
		cartItem.image = item.image;
		cartItem.inv_mast_uid = item.inv_mast_uid;
		cartItem.inv_mast_id = item.inv_mast_id;
		cartItem.item_id = item.getItemId();
		cartItem.item_desc = item.item_desc;
		cartItem.unit = item.unit;
		cartItem.job_numbers = new ArrayList<Job>();
		return cartItem;

	}

	public ArrayList<Job> job_numbers;
	public int getQuantity(){
		int quantity = 0;
		for (Job job : job_numbers) {
			quantity += job.quantity; 
		}
		return quantity;
	}
}
