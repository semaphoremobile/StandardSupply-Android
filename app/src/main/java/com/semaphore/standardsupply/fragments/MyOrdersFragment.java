package com.semaphore.standardsupply.fragments;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.model.CartItem;
import com.semaphore.standardsupply.model.ItemCache;
import com.semaphore.standardsupply.model.Job;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.MyOrder;
import com.squareup.picasso.Picasso;

public class MyOrdersFragment extends BaseFragment implements Observer {

	ExpandableListView lstMyOrders;
	EditText txtSearch;
	MyOrdersAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_my_orders, container, false);
		lstMyOrders = (ExpandableListView) v.findViewById(R.id.lstMyOrders);
		txtSearch = (EditText) v.findViewById(R.id.txtSearch);
		txtSearch.addTextChangedListener(filterTextWatcher);
		return v;
	}

	@Override
	public void onViewWillAppear() {
		Model.getInstance().getMyOrdersCache().invalidate();
		Model.getInstance().getMyOrdersCache().addObserver(this);
		Model.getInstance().getMyOrdersCache().request();
		getActivity().getActionBar().setTitle("My Orders");
	}

	@Override
	protected String getName() {
		return "My Orders";
	}

	private void setList(){
		adapter = new MyOrdersAdapter(getActivity(), Model.getInstance().getMyOrdersCache().items);
		lstMyOrders.setAdapter(adapter);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Model.getInstance().getMyOrdersCache().deleteObserver(this);
		setList();
	}


	private TextWatcher filterTextWatcher =new TextWatcher()
	{

		public void beforeTextChanged(CharSequence s, int start, int count,int after) 
		{  

		}  
		public void onTextChanged(CharSequence s, int start, int before,int count) 
		{  

		}

		public void afterTextChanged(Editable s) 
		{
			adapter.getFilter().filter(txtSearch.getText().toString());
		}  
	};
}

class MyOrdersAdapter extends BaseExpandableListAdapter implements Observer, Filterable{

	private ArrayList<MyOrder> myOrders;
	private ArrayList<MyOrder> filteredMyOrders;
	private Context context;
	public MyOrdersAdapter(Context context, ArrayList<MyOrder> myOrders) {
		this.context = context;
		this.myOrders = new ArrayList<MyOrder>();
		filteredMyOrders = new ArrayList<MyOrder>();
		if(myOrders != null) {
			this.myOrders.addAll(myOrders);
			filteredMyOrders.addAll(myOrders);
		}
	}


	MyOrderFilter filter;
	public Filter getFilter()
	{
		if(filter == null)
			filter = new MyOrderFilter();
		return filter;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return Model.getInstance().getMyOrderDetailCache(filteredMyOrders.get(groupPosition)).items.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		MyOrder myOrder = filteredMyOrders.get(groupPosition);
		ArrayList<CartItem> items = Model.getInstance().getMyOrderDetailCache(myOrder).items;

		if(childPosition == 0){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_change_truck, parent, false);
			TextView txtTruckNo = (TextView) convertView.findViewById(R.id.txtTruckNo);
			txtTruckNo.setText("" + myOrder.truck_name);
		}

		else if(childPosition == 1){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_po, parent, false);

			TextView txtPO = (TextView) convertView.findViewById(R.id.txtPO);
			txtPO.setText(myOrder.getPONumber());
		}

		else if(childPosition == 2){
			if(myOrder.order_type.equals("W") == false){
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_address, parent, false);

				TextView txtItemName = (TextView) convertView.findViewById(R.id.txtAddr1);
				txtItemName.setText(myOrder.delivery_address.name);

				TextView txtItemDesc = (TextView) convertView.findViewById(R.id.txtAddr2);
				txtItemDesc.setText(myOrder.delivery_address.address);


				TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtAddr3);
				String tmpTxtAddr3 = myOrder.delivery_address.city;
				if(tmpTxtAddr3 != null && tmpTxtAddr3.length() > 0 && myOrder.delivery_address.state != null && myOrder.delivery_address.state.length() > 0) {
					tmpTxtAddr3 += ", ";
				}
				tmpTxtAddr3 += myOrder.delivery_address.state;
				if(tmpTxtAddr3 != null && tmpTxtAddr3.length() > 0 && myOrder.delivery_address.getZipCode() != null && myOrder.delivery_address.getZipCode().length() > 0) {
					tmpTxtAddr3 += ", ";
				}
				tmpTxtAddr3 += myOrder.delivery_address.getZipCode();
				txtQuantity.setText(tmpTxtAddr3);	
			}
			else{

				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_location, parent, false);
				TextView txtLocation = (TextView) convertView.findViewById(R.id.txtLocation);
				txtLocation.setText(myOrder.location);
			}

		}
		else{
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_cart_item, parent, false);

			childPosition = childPosition - 3;
			TextView txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
			txtItemName.setText(items.get(childPosition).getItemId());

			TextView txtItemDesc = (TextView) convertView.findViewById(R.id.txtItemDetail);
			txtItemDesc.setText(items.get(childPosition).item_desc);


			TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
			txtQuantity.setText("" + items.get(childPosition).getQuantity());

			ImageView imgCartItem = (ImageView) convertView.findViewById(R.id.imgCartItem);
			Picasso.with(context)
			.load(items.get(childPosition).item_image)
			.placeholder(R.drawable.no_item_image)
			.error(R.drawable.no_item_image)
			.into(imgCartItem);


			final TextView txtJobNos = (TextView) convertView.findViewById(R.id.txtJobNos);
			String jobNos = "Job Nos. ";
			for (Job job : items.get(childPosition).job_numbers) {
				jobNos += job.number + "(" + job.quantity + "), ";
			}
			jobNos = jobNos.substring(0, jobNos.length()-2);
			txtJobNos.setText(jobNos);
		}
		return convertView;
	}


	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<CartItem> items = Model.getInstance().getMyOrderDetailCache(filteredMyOrders.get(groupPosition)).items;
		if(items == null){
			Model.getInstance().getMyOrderDetailCache(filteredMyOrders.get(groupPosition)).addObserver(this);
			Model.getInstance().getMyOrderDetailCache(filteredMyOrders.get(groupPosition)).request();
		}
		return 3 + (items == null ? 0 : items.size());
	}

	@Override
	public Object getGroup(int groupPosition) {
		return filteredMyOrders.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return filteredMyOrders.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return filteredMyOrders.get(groupPosition).order_id;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
			ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_order_no, parent, false);
		}

		TextView txtOrderNo = (TextView) convertView.findViewById(R.id.txtOrderNo);
		txtOrderNo.setText("Order No.: " + filteredMyOrders.get(groupPosition).order_id + "    " + filteredMyOrders.get(groupPosition).getOrderDate());

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void update(Observable observable, Object data) {
		@SuppressWarnings("unchecked")
		ItemCache<CartItem> cache = (ItemCache<CartItem>) data;
		cache.deleteObserver(this);
		notifyDataSetChanged();
	}


	private class MyOrderFilter extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if(constraint != null && constraint.toString().length() > 0)
			{
				ArrayList<MyOrder> filteredItems = new ArrayList<MyOrder>();

				for(int i = 0, l = myOrders.size(); i < l; i++)
				{	
					MyOrder m = myOrders.get(i);
					if(m.getPONumber().toLowerCase().contains(constraint)){
						filteredItems.add(m);
					}
					else if(("" + m.order_id).contains(constraint)){
						filteredItems.add(m);
					}
					else if(m.delivery_address != null && m.delivery_address.search(constraint.toString())){
						filteredItems.add(m);
					}

					else if(m.location != null && m.location.contains(constraint)){
						filteredItems.add(m);
					}
				}
				result.count = filteredItems.size();
				result.values = filteredItems;
			}
			else
			{
				synchronized(this)
				{
					result.values = myOrders;
					result.count = myOrders.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {

			filteredMyOrders.clear();
			filteredMyOrders.addAll((ArrayList<MyOrder>)results.values);
			notifyDataSetChanged();
			notifyDataSetInvalidated();
		}
	}

}
