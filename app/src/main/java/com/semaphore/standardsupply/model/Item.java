package com.semaphore.standardsupply.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5076995238209720609L;
	
	public int id;
	public int inv_mast_uid; //Both are same but server returns different names for different service calls :/
	public int inv_mast_id;
	protected String item_id;
	public String item_desc;
	public String unit;
	public String image;
	public String item_image;
	public boolean favorite;
	public String notes;
	@SerializedName("quantity") 
	public int shoppingQuantity;
	
	public String getItemId(){
		return item_id == null ? "" : item_id;
	}
	
	public int getInvId() {
		return inv_mast_uid == 0 ? inv_mast_id : inv_mast_uid;
	}
	
}
