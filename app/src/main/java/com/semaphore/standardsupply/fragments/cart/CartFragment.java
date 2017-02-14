package com.semaphore.standardsupply.fragments.cart;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.home.addItems.AddToOrderFragment;
import com.semaphore.standardsupply.fragments.home.profile.MyTrucksFragment;
import com.semaphore.standardsupply.model.CartItem;
import com.semaphore.standardsupply.model.Job;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;
import com.squareup.picasso.Picasso;

public class CartFragment extends BaseFragment {
	ListView lstCartItems;
	TextView txtTruckNo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_cart, container, false);

		v.findViewById(R.id.btnChangeTruck).setOnClickListener(
				changeTruckListener);
		lstCartItems = (ListView) v.findViewById(R.id.lstCartItems);
		txtTruckNo = (TextView) v.findViewById(R.id.txtTruckNo);
		registerForContextMenu(lstCartItems);
		return v;
	}

	@Override
	protected String getName() {
		return "Cart";
	}

	private void setList() {
		CartItemsAdapter adapter = new CartItemsAdapter(getActivity(),
				R.id.lstJobNos, Model.getInstance().getCart().items, this);
		lstCartItems.setOnItemClickListener(adapter.listener);
		lstCartItems.setAdapter(adapter);

	}

	public void onViewWillAppear() {
		txtTruckNo.setText("" + Settings.getTruck(getActivity()).name);
		setList();
		getActivity().getActionBar().setTitle("Cart");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle().equals("Delete")) {
			AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Model.getInstance().getCart().items.remove(acmi.position);
			Model.getInstance().persistCart();
			setList();
			return false;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add("Delete");
	}

	OnClickListener changeTruckListener = new OnClickListener() {

		@SuppressLint("CommitTransaction")
		@Override
		public void onClick(View v) {
			add(getActivity().getFragmentManager().beginTransaction(),
					new MyTrucksFragment());
		}
	};

	@Override
	public void onCreateOptionsMenu(android.view.Menu menu,
			android.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_submit, menu);
	}

	@SuppressLint("CommitTransaction")
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == R.id.action_add_items) {

			while (getActivity().getFragmentManager().popBackStackImmediate()) {

			}
			add(getActivity().getFragmentManager().beginTransaction(),
					new AddToOrderFragment());
			return false;
		} else if (item.getItemId() == R.id.action_submit) {
			if (Model.getInstance().getCart().items.size() == 0) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setTitle("Standard Supply");
				alertDialogBuilder
						.setMessage(
								"No order to submit. Please add items in your Cart !")
						.setCancelable(false)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			} else {
				// Toast.makeText(getActivity(),
				// "I ma here"+Model.getInstance().getCart().items.size(),
				// Toast.LENGTH_LONG).show();
				add(getActivity().getFragmentManager().beginTransaction(),
						new PrepCartFragment());
				return false;
			}
		}
		return super.onOptionsItemSelected(item);
	}
}

class CartItemsAdapter extends ArrayAdapter<CartItem> {

	private ArrayList<CartItem> cartItems;
	Context context;
	CartFragment cFragment;

	public CartItemsAdapter(Context context, int textViewResourceId,
			ArrayList<CartItem> objects, CartFragment fragment) {
		super(context, textViewResourceId, objects);
		this.cartItems = new ArrayList<CartItem>();
		this.cartItems.addAll(objects);
		this.context = context;
		cFragment = fragment;

	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			EnterQuantityFragment fragment = new EnterQuantityFragment();
			final Bundle bundle = new Bundle();
			bundle.putSerializable("cart_item", cartItems.get(arg2));
			fragment.setArguments(bundle);
			cFragment.add(((Activity) context).getFragmentManager()
					.beginTransaction(), fragment);
		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		cartItems = Model.getInstance().getCart().items;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_cart_item, parent,
					false);
		}
		TextView txtItemName = (TextView) convertView
				.findViewById(R.id.txtItemName);
		txtItemName.setText(cartItems.get(position).getItemId());

		TextView txtItemDesc = (TextView) convertView
				.findViewById(R.id.txtItemDetail);
		txtItemDesc.setText(cartItems.get(position).item_desc);

		TextView txtQuantity = (TextView) convertView
				.findViewById(R.id.txtQuantity);
		txtQuantity.setText("" + cartItems.get(position).getQuantity());

		final TextView txtJobNos = (TextView) convertView
				.findViewById(R.id.txtJobNos);
		String jobNos = "Job Nos. ";
		for (Job job : cartItems.get(position).job_numbers) {
			jobNos += job.number + " (" + job.quantity + "), ";
		}
		jobNos = jobNos.substring(0, jobNos.length() - 2);
		txtJobNos.setText(jobNos);

		ImageView imgCartItem = (ImageView) convertView
				.findViewById(R.id.imgCartItem);
		Picasso.with(context).load(cartItems.get(position).image)
				.placeholder(R.drawable.no_item_image)
				.error(R.drawable.no_item_image).into(imgCartItem);

		return convertView;
	}
}