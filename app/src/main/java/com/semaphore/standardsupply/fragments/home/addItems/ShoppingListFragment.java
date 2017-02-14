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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.ShoppingList;

public class ShoppingListFragment extends BaseFragment implements Observer {

	ListView lstCategories;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		lstCategories = (ListView) inflater.inflate(R.layout.fragment_list_items, container,false);
		return lstCategories;
	}
	
	@Override
	protected String getName() {
		return "Shopping List";
	}
	
	@Override
	public void onViewWillAppear() {
		Model.getInstance().getShoppingListsCache().addObserver(this);
		Model.getInstance().getShoppingListsCache().request();
		getActivity().getActionBar().setTitle("Shopping List");
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		Model.getInstance().getShoppingListsCache().deleteObserver(this);
		ShoppingListAdapter adapter = new ShoppingListAdapter(getActivity(),R.id.lstCategories, Model.getInstance().getShoppingListsCache().items, ShoppingListFragment.this);
		lstCategories.setAdapter(adapter);
		lstCategories.setOnItemClickListener(adapter.listener);
	}

}

class ShoppingListAdapter extends ArrayAdapter<ShoppingList>{

	private ArrayList<ShoppingList> shoppingLists;
	Context context;
	private ShoppingListFragment cFragment;
	public ShoppingListAdapter(Context context,
			int textViewResourceId, ArrayList<ShoppingList> objects, ShoppingListFragment fragment) {
		super(context, textViewResourceId, objects);
		this.shoppingLists = objects;
		this.context = context;
		this.cFragment = fragment;

	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ShoppingDetailFragment fragment = new ShoppingDetailFragment();
			final Bundle bundle = new Bundle();
			bundle.putInt("shoppingListIndex", arg2);
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
		textView.setText(shoppingLists.get(position).name);
		return convertView;
	}
}
