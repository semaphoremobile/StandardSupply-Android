package com.semaphore.standardsupply.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyOrder {

	public int id;
	public int  order_id;
	public int  user_id;
	public int  truck_id;
	public String  truck_name;
	public int  num_items;
	public int  num_line_items;

	public String  customer;
	public String  location;
	private String  po_number;
	public String  order_type;
	public String order_date;

	public Address delivery_address;

	
	public String getPONumber(){
		return po_number == null ? "" : po_number;
	}
	public String getOrderDate(){
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	      try {
			Date stringDate = simpledateformat.parse(order_date);
			SimpleDateFormat fmt = new SimpleDateFormat("dd MMM, yyyy", Locale.US);
			return fmt.format(stringDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return "";
	}

}
