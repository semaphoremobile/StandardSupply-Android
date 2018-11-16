package com.semaphore.standardsupply.fragments.home.addItems;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.SSApplication;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.home.GetJobFlagHandler;
import com.semaphore.standardsupply.model.Item;
import com.semaphore.standardsupply.model.ItemCache;
import com.semaphore.standardsupply.model.ItemCategory;
import com.semaphore.standardsupply.model.Model;

public class CategoriesFragment extends BaseFragment implements Observer , SearchView.OnQueryTextListener {

	ListView lstCategories;
	private SearchView mSearchView;
	public ArrayList<ItemCategory> categoriesList = new ArrayList<ItemCategory>();
	boolean checkSearch = false;
	RelativeLayout categorisesLinear;
	RelativeLayout progressLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		/*View v = inflater.inflate(R.layout.fragment_catagories_list_items, container,
				false);
*/
		categorisesLinear = (RelativeLayout) inflater.inflate(R.layout.fragment_catagories_list_items, container, false);

		lstCategories = (ListView) categorisesLinear.findViewById(R.id.catergories_list_items);
		mSearchView = (SearchView) categorisesLinear.findViewById(R.id.search_view);

		/*txtItemsSearch = (EditText) v.findViewById(R.id.txtItemsSearch);
		txtItemsSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				SSApplication.searchText = arg0.toString();
				if (adapter != null)
					adapter.getFilter().filter(arg0);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
*/
		progressLayout = (RelativeLayout) categorisesLinear.findViewById(R.id.progressBarLayout);
		showHideProgressLayout(false);

		return categorisesLinear;
	}

	private void showHideProgressLayout (boolean show) {
		if (show){
			progressLayout.setVisibility(View.VISIBLE);
		}
		else{
			progressLayout.setVisibility(View.GONE);
		}
	}

	@Override
	protected String getName() {
		return "Categories";
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("resume", "resume");
	}

	@Override
	public void onViewWillAppear() {
		// if (txtItemsSearch == null
		// || txtItemsSearch.getText().toString().trim().equals("")) {
		// Log.d("catfrg", "txtItemsSearch is null");
		// } else {
		// // txtItemsSearch.setText(txtItemsSearch.getText());
		// // FragmentTransaction tr = getFragmentManager().beginTransaction();
		// // // tr.replace(R.id.fragment_container, this);
		// // tr.detach(this);
		// // tr.commit();
		// // tr.attach(this);
		//
		// // Model.getInstance().getCategoriesCache().request();
		// }
		Model.getInstance().getCategoriesCache().deleteObservers();

		GetJobFlagHandler handler = new GetJobFlagHandler(this);
		handler.request();
		Model.getInstance().getCategoriesCache().addObserver(this);
		Log.d("catfrg", "calling 1");
		//mSearchView.setQuery("",true);
		showHideProgressLayout(true);
		Model.getInstance().getCategoriesCache().request();
		getActivity().getActionBar().setTitle("Categories");
	}

	// @Override
	// public void onViewStateRestored(Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// super.onViewStateRestored(savedInstanceState);
	// if (Model.getInstance().getCategoriesCache().items != null) {
	// Log.d("catfrg", "onViewStateRestored..."
	// + Model.getInstance().getCategoriesCache().items.size());
	// Log.d("catfrg", "onViewStateRestored1 ..."
	// + SSApplication.itemCategoryList.size());
	// // Model.getInstance().getCategoriesCache().request();
	// }
	//
	// }

	@Override
	public void update(Observable arg0, Object arg1) {
		// if (Model.getInstance().getCategoriesCache().items.size() > 0) {
		// Model.getInstance().getCategoriesCache().items.clear();
		//
		// Model.getInstance().getCategoriesCache().items
		// .addAll(SSApplication.itemCategoryList);
		// Log.d("CatFrag", "SSApplication.deleteObserver 69..final.."
		// + Model.getInstance().getCategoriesCache().items.size());
		// }
		// SSApplication.itemCategoryList.addAll(Model.getInstance()
		// .getCategoriesCache().items);
		showHideProgressLayout(false);
		Model.getInstance().getCategoriesCache().deleteObserver(this);
		Log.d("CatFrag", "SSApplication.deleteObserver...."
				+ Model.getInstance().getCategoriesCache().items.size());
		favoritesObserver = new FavoritesObserver();
		Model.getInstance().getMyFavoritesCache()
				.addObserver(favoritesObserver);
		Model.getInstance().getMyFavoritesCache().request();
		Log.d("CatFrag", "SSApplication.deleteObserver 1...."
				+ Model.getInstance().getCategoriesCache().items.size());
		//mSearchView.setQuery("",true);
	}


	private void reloadSearchList(String searchedTxt) {
		if (categoriesList.size() > 0) {
			if (searchedTxt.equals("")) {
				//  lstCategories.clearTextFilter();
				CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.id.lstCategories, categoriesList, CategoriesFragment.this);
				lstCategories.setAdapter(adapter);
				lstCategories.setOnItemClickListener(adapter.listener);
				lstCategories.setTextFilterEnabled(true);

			} else {
				// Log.v("TAG", "Searched Text " + newText);
				// lstCategories.setFilterText(newText);
				ArrayList<ItemCategory> categoriesSearchList = new ArrayList<ItemCategory>();
               /* for (int i = 0; i < categoriesList.size(); i++) {
                    Log.v("TAG", "Searched Text 2 " + categoriesList.get(i).category);
                    ItemCategory itemCategory = categoriesList.get(i);
                    if (itemCategory.category != null && !itemCategory.category.equalsIgnoreCase("null") && itemCategory.category.contains(newText.toUpperCase())) {
                        categoriesSearchList.add(categoriesList.get(i));
                    }
                }*/
				if (categoriesSearchList.size() > 0) {
					CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.id.lstCategories, categoriesSearchList, CategoriesFragment.this);
					lstCategories.setAdapter(adapter);
					checkSearch = true;
					lstCategories.setOnItemClickListener(adapter.listener);
					lstCategories.setTextFilterEnabled(true);
				} else {
					ArrayList<ItemCategory> categoriesSearchList_item = new ArrayList<ItemCategory>();
					for (int i = 0; i < categoriesList.size(); i++) {
						Log.v("TAG", "Searched Text 3  " + categoriesList.get(i).category);
						ArrayList<Item> item_ar = categoriesList.get(i).items;

						for (int j = 0; j < item_ar.size(); j++) {

							Item itemCategory = item_ar.get(j);

							if (itemCategory.getItemId() != null && !itemCategory.getItemId().equalsIgnoreCase("null") && itemCategory.getItemId().toUpperCase().contains(searchedTxt.toUpperCase())
									|| itemCategory.item_desc != null && !itemCategory.item_desc.equalsIgnoreCase("null") && itemCategory.item_desc.toUpperCase().contains(searchedTxt.toUpperCase())) {
								Log.v("TAG", "itemCategory.item_desc :- " + itemCategory.item_desc);
								if (!categoriesSearchList_item.contains(categoriesList.get(i))) {
									categoriesSearchList_item.add(categoriesList.get(i));
								}
							} /*else if (itemCategory.item_desc != null && !itemCategory.item_desc.equalsIgnoreCase("null") && itemCategory.item_desc.toUpperCase().contains(newText.toUpperCase())) {
                                if (!categoriesSearchList_item.contains(categoriesList.get(i))) {
                                    categoriesSearchList_item.add(categoriesList.get(i));
                                }
                            }*/
						}
					}


					if (categoriesSearchList_item.size() > 0) {

						checkSearch = false;

					}
					//categoriesList = Model.getInstance().getCategoriesCache().items;

					CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.id.lstCategories, categoriesSearchList_item, CategoriesFragment.this);
					lstCategories.setAdapter(adapter);
					lstCategories.setOnItemClickListener(adapter.listener);
					lstCategories.setTextFilterEnabled(true);
				//	setupSearchView();

				}

			}
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		if (TextUtils.isEmpty(query)) {
			// lstCategories.clearTextFilter();
		} else {
			Log.v("TAG", "Searched Text " + query);
			//   lstCategories.setFilterText(query);
		}

		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (categoriesList.size() > 0) {

			if (TextUtils.isEmpty(newText)) {
				//  lstCategories.clearTextFilter();
				CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.id.lstCategories, categoriesList, CategoriesFragment.this);
				lstCategories.setAdapter(adapter);
				lstCategories.setOnItemClickListener(adapter.listener);
				lstCategories.setTextFilterEnabled(true);

			} else {
				Log.v("TAG", "Searched Text " + newText);
				// lstCategories.setFilterText(newText);
				ArrayList<ItemCategory> categoriesSearchList = new ArrayList<ItemCategory>();
               /* for (int i = 0; i < categoriesList.size(); i++) {
                    Log.v("TAG", "Searched Text 2 " + categoriesList.get(i).category);
                    ItemCategory itemCategory = categoriesList.get(i);
                    if (itemCategory.category != null && !itemCategory.category.equalsIgnoreCase("null") && itemCategory.category.contains(newText.toUpperCase())) {
                        categoriesSearchList.add(categoriesList.get(i));
                    }
                }*/
				if (categoriesSearchList.size() > 0) {
					CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.id.lstCategories, categoriesSearchList, CategoriesFragment.this);
					lstCategories.setAdapter(adapter);
					checkSearch = true;
					lstCategories.setOnItemClickListener(adapter.listener);
					lstCategories.setTextFilterEnabled(true);
				} else {
					ArrayList<ItemCategory> categoriesSearchList_item = new ArrayList<ItemCategory>();
					for (int i = 0; i < categoriesList.size(); i++) {
						Log.v("TAG", "Searched Text 3  " + categoriesList.get(i).category);
						ArrayList<Item> item_ar = categoriesList.get(i).items;

						for (int j = 0; j < item_ar.size(); j++) {

							Item itemCategory = item_ar.get(j);

							if (itemCategory.getItemId() != null && !itemCategory.getItemId().equalsIgnoreCase("null") && itemCategory.getItemId().toUpperCase().contains(newText.toUpperCase())
									|| itemCategory.item_desc != null && !itemCategory.item_desc.equalsIgnoreCase("null") && itemCategory.item_desc.toUpperCase().contains(newText.toUpperCase())) {
								Log.v("TAG", "itemCategory.item_desc :- " + itemCategory.item_desc);
								if (!categoriesSearchList_item.contains(categoriesList.get(i))) {
									categoriesSearchList_item.add(categoriesList.get(i));
								}
							} /*else if (itemCategory.item_desc != null && !itemCategory.item_desc.equalsIgnoreCase("null") && itemCategory.item_desc.toUpperCase().contains(newText.toUpperCase())) {
                                if (!categoriesSearchList_item.contains(categoriesList.get(i))) {
                                    categoriesSearchList_item.add(categoriesList.get(i));
                                }
                            }*/
						}
					}


					if (categoriesSearchList_item.size() > 0) {

						checkSearch = false;

					}

					CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.id.lstCategories, categoriesSearchList_item, CategoriesFragment.this);
					lstCategories.setAdapter(adapter);
					lstCategories.setOnItemClickListener(adapter.listener);
					lstCategories.setTextFilterEnabled(true);

				}

			}
		}
		return true;
	}

	public void addlist(ArrayList<ItemCategory> categoriesLists) {

		categoriesList.clear();
		categoriesList = categoriesLists;
	}

	private void setupSearchView() {
		mSearchView.setIconifiedByDefault(false);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setSubmitButtonEnabled(false);
		mSearchView.setQueryHint("Search Here");
	}

	private FavoritesObserver favoritesObserver;

	class FavoritesObserver implements Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			Log.d("CatFrag", "SSApplication.deleteObserver 2...."
					+ Model.getInstance().getCategoriesCache().items.size());
			Model.getInstance().getMyFavoritesCache().deleteObserver(this);
			Log.d("CatFrag", "SSApplication.deleteObserver 3...."
					+ Model.getInstance().getCategoriesCache().items.size());
			ItemCache<Item> favoritesCache = Model.getInstance()
					.getMyFavoritesCache();
			ItemCache<ItemCategory> categoriesCache = Model.getInstance()
					.getCategoriesCache();
			if (categoriesCache.items == null) {
				return;
			}
			for (ItemCategory itemCategory : categoriesCache.items) {
				for (Item item : itemCategory.items) {
					for (Item favoriteItem : favoritesCache.items) {
						if (item.inv_mast_uid == favoriteItem.inv_mast_uid) {
							item.favorite = true;
						}
					}
				}
			}
			Log.d("CatFrag", "SSApplication.deleteObserver 4...."
					+ Model.getInstance().getCategoriesCache().items.size());
			if (getActivity() != null) {

				categoriesList = Model.getInstance().getCategoriesCache().items;

				CategoriesAdapter adapter = new CategoriesAdapter(getActivity(),
						R.id.lstCategories, Model.getInstance()
						.getCategoriesCache().items,
						CategoriesFragment.this);
				Log.d("CatFrag", "SSApplication.itemCategoryList 5...."
						+ Model.getInstance().getCategoriesCache().items.size());
				if (SSApplication.itemCategoryList != null) {
					SSApplication.itemCategoryList.clear();
				}
				Log.d("CatFrag", "SSApplication.deleteObserver 6...."
						+ Model.getInstance().getCategoriesCache().items.size());
				SSApplication.itemCategoryList.addAll(Model.getInstance()
						.getCategoriesCache().items);
				Log.d("CatFrag", "SSApplication.deleteObserver 7...."
						+ Model.getInstance().getCategoriesCache().items.size());
				lstCategories.setAdapter(adapter);
				lstCategories.setOnItemClickListener(adapter.listener);
				setupSearchView();
			}
		}
	}


	class CategoriesAdapter extends ArrayAdapter<ItemCategory> {

		private ArrayList<ItemCategory> categories;
		Context context;
		private ArrayList<ItemCategory> filteredCategories;
		private CategoryFilter filter;
		private CategoriesFragment cFragment;

		public CategoriesAdapter(Context context, int textViewResourceId,
								 ArrayList<ItemCategory> objects, CategoriesFragment fragment) {
			super(context, textViewResourceId, objects);
			this.categories = objects;
			// this.categories.addAll(objects);
			this.context = context;
			this.filteredCategories = new ArrayList<ItemCategory>();
			this.filteredCategories.addAll(objects);
			this.cFragment = fragment;

		}

		OnItemClickListener listener = new OnItemClickListener() {

		/*	@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				Log.d("CatFrag", "SSApplication.deleteObserver 69...."
						+ Model.getInstance().getCategoriesCache().items.size());
				CategoryDetailFragment fragment = new CategoryDetailFragment();
				final Bundle bundle = new Bundle();
				bundle.putInt("categoryIndex", arg2);
				fragment.setArguments(bundle);
				cFragment.add(((Activity) getContext()).getFragmentManager()
						.beginTransaction(), fragment);

			}
		};
*/
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
			com.semaphore.standardsupply.fragments.home.addItems.CategoryDetailFragment fragment = new com.semaphore.standardsupply.fragments.home.addItems.CategoryDetailFragment();
			final Bundle bundle = new Bundle();
			bundle.putInt("categoryIndex", arg2);
			if (!checkSearch) {
				bundle.putString("categoryIndexSearch", mSearchView.getQuery().toString());


				for (int i = 0; i < categoriesList.size(); i++) {

					ItemCategory itemCategory = categoriesList.get(i);

					if (itemCategory.category != null && !itemCategory.category.equalsIgnoreCase("null") && itemCategory.category.equalsIgnoreCase(categories.get(arg2).category.toUpperCase())) {
						bundle.putInt("categoryIndex", i);

					}


				}


			} else {
				bundle.putString("categoryIndexSearch", "");

			}
			// mSearchView.setQuery("", true);
			fragment.setArguments(bundle);

			checkSearch = true;
			cFragment.add(((Activity) getContext()).getFragmentManager().beginTransaction(), fragment);
			//  notifyDataSetChanged();
		}
		};

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater
						.inflate(R.layout.row_category, parent, false);
			}
			TextView textView = (TextView) convertView
					.findViewById(R.id.txtCategoryName);
			textView.setText(categories.get(position).category);
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
				Log.d("IF 0 ::: ", SSApplication.itemCategoryList.size() + "");
				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				if (constraint != null && constraint.toString().length() > 0) {
					Log.d("IF 1 ::: ", SSApplication.itemCategoryList.size() + "");
					ArrayList<ItemCategory> filteredItems = new ArrayList<ItemCategory>();

					for (int i = 0, l = SSApplication.itemCategoryList.size(); i < l; i++) {

						ItemCategory m = SSApplication.itemCategoryList.get(i);
						if (filteredItems.contains(m)) {
							continue;
						}
						if (m.category.toLowerCase().contains(constraint)) {
							Log.d("CatFrg", m.category.toLowerCase());
							filteredItems.add(m);
						} else {

							for (int j = 0, n = m.items.size(); j < n; j++) {
								if (m.items.get(j).getItemId().toLowerCase()
										.contains(constraint)) {
									Log.d("CatFrg inner", m.category.toLowerCase());
									filteredItems.add(m);
									break;
								}
							}
						}
					}
					result.count = filteredItems.size();
					result.values = filteredItems;
				} else {
					synchronized (this) {
						Log.d("ELSE ::: ", SSApplication.itemCategoryList.size()
								+ "");
						result.values = SSApplication.itemCategoryList;
						result.count = SSApplication.itemCategoryList.size();
						Log.d("IF 2 ::: ", SSApplication.itemCategoryList.size()
								+ "");
					}
				}
				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
										  FilterResults results) {
				Log.d("CatFrag", "SSApplication.deleteObserver 69 1...."
						+ Model.getInstance().getCategoriesCache().items.size());
				filteredCategories = (ArrayList<ItemCategory>) results.values;
				notifyDataSetChanged();
				Log.d("CatFrag", "SSApplication.deleteObserver 69....2..."
						+ Model.getInstance().getCategoriesCache().items.size());
				clear();
				// addAll(filteredCategories);
				for (int i = 0, l = filteredCategories.size(); i < l; i++) {
					add(filteredCategories.get(i));
				}
				Log.d("CatFrag", "SSApplication.deleteObserver 69..3.."
						+ Model.getInstance().getCategoriesCache().items.size());
				Log.d("IF 3 ::: ", SSApplication.itemCategoryList.size() + "");
				notifyDataSetInvalidated();
				Log.d("IF 4 ::: ", SSApplication.itemCategoryList.size() + "");
				Log.d("CatFrag", "SSApplication.deleteObserver 1 2...."
						+ Model.getInstance().getCategoriesCache().items.size());
			}
		}
	}
}