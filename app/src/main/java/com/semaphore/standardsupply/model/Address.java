package com.semaphore.standardsupply.model;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.Locale;

public class Address implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1319441749398738982L;
	
	public String name;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String zip_code;
	
	//Sometimes the server returns zip and sometimes zip_code :/
	public String getZipCode(){
		return zip == null ? (zip_code == null ? "" : zip_code) : zip; 
	}
	@SuppressLint("DefaultLocale")
	public boolean search(String constraint) {
//		return name.contains(constraint) || address.contains(constraint) || 
//				city.contains(constraint) || state.contains(constraint) || 
//				zip.contains(constraint);
		return name.toLowerCase(Locale.US).indexOf(constraint.toLowerCase()) > -1 ||
				address.toLowerCase(Locale.US).indexOf(constraint.toLowerCase()) > -1 ||
				city.toLowerCase(Locale.US).indexOf(constraint.toLowerCase()) > -1 ||
				state.toLowerCase(Locale.US).indexOf(constraint.toLowerCase()) > -1 ||
				(zip!=null && zip.toLowerCase(Locale.US).indexOf(constraint.toLowerCase()) > -1);
	}
	

}
