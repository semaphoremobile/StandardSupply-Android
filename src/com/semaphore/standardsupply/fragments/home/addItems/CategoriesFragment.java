package com.semaphore.standardsupply.fragments.home.addItems;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.home.GetJobFlagHandler;
import com.semaphore.standardsupply.model.Item;
import com.semaphore.standardsupply.model.ItemCache;
import com.semaphore.standardsupply.model.ItemCategory;
import com.semaphore.standardsupply.model.Model;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CategoriesFragment extends BaseFragment implements Observer {

	ListView lstCategories;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		lstCategories = (ListView) inflater.inflate(R.layout.fragment_list_items, container,false);
		return lstCategories;
	}
	
	@Override
	protected String getName() {
		return "Categories";
	}
	
	@Override
	public void onViewWillAppear() {
		GetJobFlagHandler handler = new GetJobFlagHandler(this);
		handler.request();
		Model.getInstance().getCategoriesCache().addObserver(this);
		Model.getInstance().getCategoriesCache().request();
		getActivity().getActionBar().setTitle("Categories");
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		Model.getInstance().getCategoriesCache().deleteObserver(this);

		favoritesObserver = new FavoritesObserver();
		Model.getInstance().getMyFavoritesCache().addObserver(favoritesObserver);
		Model.getInstance().getMyFavoritesCache().request();
	}

	private FavoritesObserver favoritesObserver;
	class FavoritesObserver implements Observer
	{
		@Override
		public void update(Observable arg0, Object arg1)
		{
			Model.getInstance().getMyFavoritesCache().deleteObserver(this);
			
			ItemCache<Item> favoritesCache = Model.getInstance().getMyFavoritesCache();
			ItemCache<ItemCategory> categoriesCache = Model.getInstance().getCategoriesCache();
			if(categoriesCache.items == null){
				return;
			}
			for(ItemCategory itemCategory : categoriesCache.items)
			{
				for(Item item : itemCategory.items)
				{
					for(Item favoriteItem : favoritesCache.items)
					{
						if(item.inv_mast_uid == favoriteItem.inv_mast_uid)
						{
							item.favorite = true;
						}
					}					
				}
			}
			if(getActivity() != null){
				CategoriesAdapter adapter = new CategoriesAdapter(getActivity(),R.id.lstCategories, Model.getInstance().getCategoriesCache().items, CategoriesFragment.this);
				lstCategories.setAdapter(adapter);
				lstCategories.setOnItemClickListener(adapter.listener);
			}
		}
	}
}
class CategoriesAdapter extends ArrayAdapter<ItemCategory>{

	private ArrayList<ItemCategory> categories;
	Context context;
	private CategoriesFragment cFragment;
	public CategoriesAdapter(Context context,
			int textViewResourceId, ArrayList<ItemCategory> objects, CategoriesFragment fragment) {
		super(context, textViewResourceId, objects);
		this.categories = objects;
		this.context = context;
		this.cFragment = fragment;

	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			CategoryDetailFragment fragment = new CategoryDetailFragment();
			final Bundle bundle = new Bundle();
			bundle.putInt("categoryIndex", arg2);
			fragment.setArguments(bundle);
			cFragment.add(((Activity)getContext()).getFragmentManager().beginTransaction(), fragment);
		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_category, parent, false);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.txtCategoryName);
		textView.setText(categories.get(position).category);
		return convertView;
	}
}