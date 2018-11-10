package com.semaphore.standardsupply.fragments.home.addItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.SSApplication;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.cart.SubmitOrderHandler;
import com.semaphore.standardsupply.model.Cart;
import com.semaphore.standardsupply.model.CartItem;
import com.semaphore.standardsupply.model.Item;
import com.semaphore.standardsupply.model.Job;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;
import com.squareup.picasso.Picasso;

public class PickUpItemDetailFragment extends BaseFragment implements Observer {

	private Item item;

	EditText txtJobNo;
	EditText txtPONo;
	ListView lstAddItems;

	Button btnAddItem;
	Button btnSubmit;
	ArrayList<Item> arrayList = new ArrayList<Item>();
	PickupDetailAdapter adapter;
	RelativeLayout progressLayout;

	@Override
	protected String getName() {
		return "Pickup Item Detail";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.pickup_item_detail, container, false);
		progressLayout = (RelativeLayout) v.findViewById(R.id.progressBarLayout);
		showHideProgressLayout(false);
		if (getArguments().containsKey("item")) {
			item = (Item) getArguments().getSerializable("item");
			arrayList.add(item);
			// REQ: Allow any qty for pick up now orders
			// SSApplication.itemList.add(item);
			addOrUpdateItemQuantity(item);
		}

		txtJobNo = (EditText) v.findViewById(R.id.txtJobNo);
		txtPONo = (EditText) v.findViewById(R.id.txtPONo);
		lstAddItems = (ListView) v.findViewById(R.id.lstAddItems);

		ArrayList<Item> itemLists=SSApplication.itemList;
		adapter = new PickupDetailAdapter(getActivity(), R.id.lstAddItems,
				itemLists, this);
		lstAddItems.setAdapter(adapter);
		// lstAddItems.setOnItemClickListener(adapter.listener);

		btnAddItem = (Button) v.findViewById(R.id.btnAddItem);
		btnAddItem.setOnClickListener(addItemListener);

		// REQ: Allow any qty for pick up now orders
		/*
		if(itemLists.size()>2){
			btnAddItem.setVisibility(View.GONE);
		}*/

		btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(submitListener);

		return v;
	}

	private void showHideProgressLayout (boolean show) {
		if (show){
			progressLayout.setVisibility(View.VISIBLE);
		}
		else{
			progressLayout.setVisibility(View.GONE);
		}
	}

	// REQ: Allow any qty for pick up now orders
	// Find if the item is already present in the cache. If it is increment the quantity by 1
	private void addOrUpdateItemQuantity(Item item){
		for(Item citem : SSApplication.itemList){
			if(citem.id == item.id) {
				// Item already exists in the cache
				// Increment the count
				citem.shoppingQuantity ++;
				return;
			}
		}

		// If we have reached this part, this means the item is not already in the cache, so add the item
        item.shoppingQuantity = 1;
		SSApplication.itemList.add(item);
	}

	// REQ: Allow any qty for pick up now orders
	// If the user updates the item quantity, call this method
	private void updateItemQuantity(int position, int quantity){
		Iterator<Item> iter = SSApplication.itemList.iterator();

		Item cachedItem = SSApplication.itemList.get(position);
		while (iter.hasNext()) {
			Item itemVal = iter.next();
			if (itemVal.getItemId().equalsIgnoreCase(
					cachedItem.getItemId())) {
				cachedItem.shoppingQuantity =  quantity;
				return;
			}
		}
	}


	public void removeItem(int position) {

		Iterator<Item> iter = SSApplication.itemList.iterator();

		while (iter.hasNext()) {
			Item itemVal = iter.next();
			Log.e("Position Val::; ", position + "");
			Log.e("itemVal::; ", itemVal.getItemId() + "");
			Log.e("itemVal111::; ", SSApplication.itemList.get(position)
					.getItemId() + "");
			if (itemVal.getItemId().equalsIgnoreCase(
					SSApplication.itemList.get(position).getItemId())) {
				Log.e("Result::; ", "Yes");
				iter.remove();
				adapter = new PickupDetailAdapter(getActivity(),
						R.id.lstAddItems, SSApplication.itemList, this);
				lstAddItems.setAdapter(adapter);
				return;
			}

		}

	}

	@Override
	public void onViewWillAppear() {
		if (SSApplication.jobNo != null) {
			txtJobNo.setText(SSApplication.jobNo);
		}
		if (SSApplication.poNo != null) {
			txtPONo.setText(SSApplication.poNo);
		}
		getActivity().getActionBar().setTitle("Items List");
		// getActivity().getActionBar().setTitle(
		// Model.getInstance().getCategoriesCache().items
		// .get(categoryIndex).category);
	}

	OnClickListener submitListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String jNo = txtJobNo.getText().toString();
			if (jNo.equals("")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Job number required");
				builder.setMessage("The job number cannot be blank.");
				builder.setPositiveButton("OK", null);
				builder.create().show();
				return;
			}
			String poNo = txtPONo.getText().toString();
			if (poNo.equals("")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("PO number required");
				builder.setMessage("The PO number cannot be blank.");
				builder.setPositiveButton("OK", null);
				builder.create().show();
				return;
			}

			Cart cart = new Cart();
			cart.deliveryMethod = getString(R.string.pick_up_now);
			cart.location = SSApplication.location == null ? Settings
					.getLocation(getActivity()) : SSApplication.location;
			cart.truckId = "" + Settings.getTruck(getActivity()).id;
			cart.PO = jNo;

			for (int i = 0; i < SSApplication.itemList.size(); i++) {
				Log.e("LIST SIZE:: ", SSApplication.itemList.size() + "");
				Item item = SSApplication.itemList.get(i);
				CartItem cartItem = CartItem
						.cartItemForItem(item);
				Job job = new Job();
				job.number = jNo;
                // REQ: Allow any qty for pick up now orders
				// job.quantity = 1;
                job.quantity = item.shoppingQuantity;
				Model.getInstance().defaultJobNo = "";
				cartItem.job_numbers = new ArrayList<Job>();
				cartItem.job_numbers.add(job);
				cart.items.add(cartItem);
				Log.e("cart LIST SIZE:: ", cart.items.size() + "");
			}

			SubmitOrderHandler handler = new SubmitOrderHandler(
					(SSActivity) getActivity());
			handler.cart = cart;
			handler.request();
			showHideProgressLayout(true);

			SSApplication.itemList.clear();
			SSApplication.jobNo = "";
			SSApplication.poNo = "";
			SSApplication.pickUp=false;

		}
	};

	OnClickListener addItemListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SSApplication.jobNo = txtJobNo.getText().toString();
			SSApplication.poNo = txtPONo.getText().toString();
			FragmentTransaction fmt = getFragmentManager().beginTransaction();
			add(fmt, new AddToOrderFragment());

		}
	};

	class PickupDetailAdapter extends ArrayAdapter<Item> {

		private ArrayList<Item> categories;
		private ArrayList<Item> filteredCategories;
		Context context;
		private CategoryFilter filter;
		private PickUpItemDetailFragment cdFragment;

		public PickupDetailAdapter(Context context, int textViewResourceId,
				ArrayList<Item> objects, PickUpItemDetailFragment fragment) {
			super(context, textViewResourceId, objects);
			this.categories = new ArrayList<Item>();
			this.categories.addAll(objects);
			this.context = context;
			this.filteredCategories = new ArrayList<Item>();
			this.filteredCategories.addAll(objects);
			this.cdFragment = fragment;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.pickup_item_lst,
						parent, false);
			}
			TextView txtItemName = (TextView) convertView
					.findViewById(R.id.txtItemName);
			txtItemName.setText(filteredCategories.get(position).getItemId());
			Button btnDelete = (Button) convertView.findViewById(R.id.button2);
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.imgItem);
			Picasso.with(context).load(filteredCategories.get(position).image)
					.placeholder(R.drawable.no_item_image)
					.error(R.drawable.no_item_image).into(imageView);

			btnDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					cdFragment.removeItem(position);

				}
			});


			// REQ: Allow any qty for pick up now orders
			View etView = convertView
					.findViewById(R.id.et_quantity);
            Item currentItem = filteredCategories.get(position);
			if(etView != null) {
				final EditText et_quantity = (EditText) etView;
				et_quantity.setText(Integer.toString(currentItem.shoppingQuantity));
				et_quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					public void onFocusChange(View v, boolean hasFocus) {
						// Assuming we have lost the focues, update the model
						if(!hasFocus) {
						    if(et_quantity.getText() != null && !et_quantity.getText().toString().equals("")) {
                                int changedQuantity = Integer.parseInt(et_quantity.getText().toString());
                                cdFragment.updateItemQuantity(position, changedQuantity);
                            }
						}
					}
				});
			}


			return convertView;
		}

		@Override
		public Filter getFilter() {
			if (filter == null) {
				filter = new CategoryFilter();
			}
			return filter;
		}

		private class CategoryFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				if (constraint != null && constraint.toString().length() > 0) {
					ArrayList<Item> filteredItems = new ArrayList<Item>();

					for (int i = 0, l = categories.size(); i < l; i++) {
						Item m = categories.get(i);
						if (m.getItemId().toLowerCase().contains(constraint)
								|| m.item_desc.toLowerCase().contains(
										constraint))
							filteredItems.add(m);
					}
					result.count = filteredItems.size();
					result.values = filteredItems;
				} else {
					synchronized (this) {
						result.values = categories;
						result.count = categories.size();
					}
				}
				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				filteredCategories = (ArrayList<Item>) results.values;
				notifyDataSetChanged();
				clear();
				for (int i = 0, l = filteredCategories.size(); i < l; i++)
					add(filteredCategories.get(i));
				notifyDataSetInvalidated();
			}

		}

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}
}
