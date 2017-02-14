package com.semaphore.standardsupply.fragments.home.addItems;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.model.Item;
import com.semaphore.standardsupply.model.Model;
import com.squareup.picasso.Picasso;

public class CompanyFavoritesFragment extends BaseFragment implements Observer{
	ListView lstItems;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		lstItems = (ListView) inflater.inflate(R.layout.fragment_list_items, container, false);
		return lstItems;
	}
	
	@Override
	protected String getName() {
		return "Company Favorites";
	}
	
	@Override
	public void onViewWillAppear() {
		Model.getInstance().getCompanyFavoritesCache().addObserver(this);
		Model.getInstance().getCompanyFavoritesCache().request();
		getActivity().getActionBar().setTitle("Company Favorites");
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Model.getInstance().getCompanyFavoritesCache().deleteObserver(this);
		if(getActivity() != null){
			CompanyFavoritesAdapter adapter = new CompanyFavoritesAdapter(getActivity(),R.id.lstItems, Model.getInstance().getCompanyFavoritesCache().items, this);
			lstItems.setAdapter(adapter);
			lstItems.setOnItemClickListener(adapter.listener);
		}
	}
}


class CompanyFavoritesAdapter extends ArrayAdapter<Item>{

	private ArrayList<Item> favorites;
	Context context;
	CompanyFavoritesFragment cfFragment;
	public CompanyFavoritesAdapter(Context context,
			int textViewResourceId, ArrayList<Item> objects, CompanyFavoritesFragment fragment) {
		super(context, textViewResourceId, objects);
		this.favorites = objects;
		this.context = context;
		cfFragment = fragment;
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ItemDetailFragment fragment = new ItemDetailFragment();
			final Bundle bundle = new Bundle();
			bundle.putSerializable("item", favorites.get(arg2));
			fragment.setArguments(bundle); 
			cfFragment.add(((Activity)getContext()).getFragmentManager().beginTransaction(), fragment);

		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_item, parent, false);
		}
		TextView txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
		txtItemName.setText(favorites.get(position).getItemId());
		TextView txtItemDetails = (TextView) convertView.findViewById(R.id.txtItemDetails);
		txtItemDetails.setText(favorites.get(position).item_desc);
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imgItem);
		Picasso.with(context)
	    .load(favorites.get(position).image)
	    .placeholder(R.drawable.no_item_image)
	    .error(R.drawable.no_item_image)
	    .into(imageView);
		return convertView;
	}
}
