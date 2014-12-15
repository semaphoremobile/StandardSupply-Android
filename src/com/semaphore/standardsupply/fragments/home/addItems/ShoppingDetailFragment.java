package com.semaphore.standardsupply.fragments.home.addItems;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.cart.CartFragment;
import com.semaphore.standardsupply.model.CartItem;
import com.semaphore.standardsupply.model.Job;
import com.semaphore.standardsupply.model.Model;
import com.squareup.picasso.Picasso;

public class ShoppingDetailFragment extends BaseFragment {
	
	EditText txtItemsSearch;
	ListView lstCategorieItems;
	private int shoppingListIndex;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_category_detail, container,false);
		lstCategorieItems = (ListView) v.findViewById(R.id.lstCategoryItems);
		shoppingListIndex = getArguments().getInt("shoppingListIndex");
		ArrayList<CartItem> cartItems = Model.getInstance().getShoppingListsCache().items.get(shoppingListIndex).items;
		for (CartItem item : cartItems) {
	        Job job = new Job();
	        job.quantity = item.shoppingQuantity;
	        job.number = "";
	        
	        if(item.job_numbers == null){
	        	item.job_numbers = new ArrayList<Job>();
	        }
	        item.job_numbers.clear();
	        item.job_numbers.add(job);
	    }
		
		final ShoppingDetailAdapter adapter = new ShoppingDetailAdapter(getActivity(),R.id.lstCategories, Model.getInstance().getShoppingListsCache().items.get(shoppingListIndex).items, this);
		lstCategorieItems.setAdapter(adapter);
		lstCategorieItems.setOnItemClickListener(adapter.listener);
		txtItemsSearch = (EditText) v.findViewById(R.id.txtItemsSearch);
		txtItemsSearch.setVisibility(View.GONE);
		return v;
	}

	@Override
	protected String getName() {
		return "Shopping Detail";
	}
	@Override
	public void onViewWillAppear() {
		getActivity().getActionBar().setTitle(Model.getInstance().getShoppingListsCache().items.get(shoppingListIndex).name);
	}

	@Override
	public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_cart, menu);
	}

	@SuppressLint("CommitTransaction")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_cart){
			showAddToCartDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showAddToCartDialog(){
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

		final android.widget.EditText jobNumber = new android.widget.EditText(this.getActivity());
		builder.setView(jobNumber);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage("Please enter the main job number:")
		.setTitle("");

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@SuppressLint("CommitTransaction")
			public void onClick(DialogInterface dialog, int id) {

				String jNo = jobNumber.getText().toString();
				if(jNo.equals("")){
					AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.getActivity());
					builder.setTitle("Invalid Job Number");
					builder.setMessage("The job number cannot be blank");
					builder.setPositiveButton("OK", null);
					builder.create().show();
					return;
				}
				ArrayList<CartItem> cartItems = Model.getInstance().getShoppingListsCache().items.get(shoppingListIndex).items;
				for (CartItem item : cartItems) {
			        Job job = item.job_numbers.get(0);
			        job.number = jNo;
			        
			        item.job_numbers.remove(0);
			        item.job_numbers.add(job);
			        Model.getInstance().getCart().items.add(item);
			    }
//				Settings.putObject(getActivity(), Model.getInstance().getCart(), SK)
//			    [Settings setItem:[Model shared].cart forKey:SKCart];
				add(getActivity().getFragmentManager().beginTransaction(), new CartFragment());
			}
		});

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}

class ShoppingDetailAdapter extends ArrayAdapter<CartItem>{

	private ArrayList<CartItem> items;
	Context context;
	private ShoppingDetailFragment cdFragment;
	public ShoppingDetailAdapter(Context context,
			int textViewResourceId, ArrayList<CartItem> objects, ShoppingDetailFragment fragment) {
		super(context, textViewResourceId, objects);
		this.items = new ArrayList<CartItem>();
		this.items.addAll(objects);
		this.context = context;
		this.cdFragment = fragment;
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ItemDetailFragment fragment = new ItemDetailFragment();
			final Bundle bundle = new Bundle();
			bundle.putSerializable("item", items.get(arg2));
			fragment.setArguments(bundle); 
			cdFragment.add(((Activity)getContext()).getFragmentManager().beginTransaction(), fragment);

		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_shopping_item, parent, false);
		}
		TextView txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
		txtItemName.setText(items.get(position).getItemId());
		TextView txtItemDetails = (TextView) convertView.findViewById(R.id.txtItemDetails);
		txtItemDetails.setText(items.get(position).item_desc);
		
		final TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
		txtQuantity.setText("" + items.get(position).getQuantity());
		txtQuantity.setTag(items.get(position));
		txtQuantity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(cdFragment.getActivity());

				final android.widget.EditText jobNumber = new android.widget.EditText(cdFragment.getActivity());
				builder.setView(jobNumber);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Enter quantity:")
				.setTitle("");

				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						String jNo = jobNumber.getText().toString();
						if(jNo.equals("") || jNo.equals("0")){
							AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.getActivity());
							builder.setTitle("Invalid Quantity");
							builder.setMessage("The quantity cannot be blank or 0.");
							builder.setPositiveButton("OK", null);
							builder.create().show();
							return;
						}
						CartItem cartItem = (CartItem)txtQuantity.getTag();
						cartItem.job_numbers.clear();
						Job job = new Job();
				        job.number = "";
						job.quantity = Integer.parseInt(jNo);
						cartItem.job_numbers.add(job);
						notifyDataSetChanged();
						notifyDataSetInvalidated();
					}
				});

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imgItem);
		Picasso.with(context)
	    .load(items.get(position).image)
	    .placeholder(R.drawable.no_item_image)
	    .error(R.drawable.no_item_image)
	    .into(imageView);
		return convertView;
	}
}
