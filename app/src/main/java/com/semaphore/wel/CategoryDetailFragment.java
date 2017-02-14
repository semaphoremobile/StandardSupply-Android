package com.semaphore.wel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.home.addItems.ItemDetailFragment;
import com.semaphore.standardsupply.model.Item;
import com.semaphore.standardsupply.model.Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.semaphore.standardsupply.R.id.lstCategories;

public class CategoryDetailFragment extends BaseFragment {

    EditText txtItemsSearch;

    ListView lstCategorieItems;
    private int categoryIndex;
    ArrayList<Item> categoriesList;
    CategoryDetailFragment categoryDetailFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_detail, container, false);
        lstCategorieItems = (ListView) v.findViewById(R.id.lstCategoryItems);
        categoryIndex = getArguments().getInt("categoryIndex");
        categoryDetailFragment = this;
        Log.v("TAG", "Searched Text veera ");

        categoriesList = Model.getInstance().getCategoriesCache().items.get(categoryIndex).items;


        final CategoryDetailAdapter adapter = new CategoryDetailAdapter(getActivity(), lstCategories, Model.getInstance().getCategoriesCache().items.get(categoryIndex).items, this);
        lstCategorieItems.setAdapter(adapter);
        lstCategorieItems.setOnItemClickListener(adapter.listener);
        txtItemsSearch = (EditText) v.findViewById(R.id.txtItemsSearch);


        txtItemsSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                /*if (!arg0.equals("")) {
                    adapter.getFilter().filter(arg0);
                }*/
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub


                ArrayList<Item> categoriesSearchLists = new ArrayList<Item>();
                for (int i = 0; i < categoriesList.size(); i++) {
                    Item itemCategory = categoriesList.get(i);
                    Log.v("ITEM ID-------------", "ITEMID _________" + itemCategory.getItemId());
                    Log.v("TAG", "ITEM DESC _________" + itemCategory.item_desc);
                    if (itemCategory.getItemId() != null && !itemCategory.getItemId().equalsIgnoreCase("null") && itemCategory.getItemId().toUpperCase().contains(arg0.toString().toUpperCase())
                            || itemCategory.item_desc != null && !itemCategory.item_desc.equalsIgnoreCase("null") && itemCategory.item_desc.toUpperCase().contains(arg0.toString().toUpperCase())) {
                        categoriesSearchLists.add(categoriesList.get(i));
                    }
                }

                CategoryDetailAdapter adapter = new CategoryDetailAdapter(getActivity(), lstCategories, categoriesSearchLists, categoryDetailFragment);
                lstCategorieItems.setAdapter(adapter);
                lstCategorieItems.setOnItemClickListener(adapter.listener);


            }
        });
        if (getArguments().getString("categoryIndexSearch") != null) {
            txtItemsSearch.setText(getArguments().getString("categoryIndexSearch"));

            txtItemsSearch.requestFocus();
        }
        return v;
    }

    @Override
    protected String getName() {
        return "Category Detail";
    }

    @Override
    public void onViewWillAppear() {
        getActivity().getActionBar().setTitle(Model.getInstance().getCategoriesCache().items.get(categoryIndex).category);
    }
}

class CategoryDetailAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> categories;
    private ArrayList<Item> filteredCategories;
    Context context;
    private CategoryFilter filter;
    private CategoryDetailFragment cdFragment;

    public CategoryDetailAdapter(Context context,
                                 int textViewResourceId, ArrayList<Item> objects, CategoryDetailFragment fragment) {
        super(context, textViewResourceId, objects);
        this.categories = new ArrayList<Item>();
        this.categories.addAll(objects);
        this.context = context;
        this.filteredCategories = new ArrayList<Item>();
        this.filteredCategories.addAll(objects);
        this.cdFragment = fragment;
    }

    OnItemClickListener listener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            ItemDetailFragment fragment = new ItemDetailFragment();
            final Bundle bundle = new Bundle();
            bundle.putSerializable("item", filteredCategories.get(arg2));
            fragment.setArguments(bundle);
            cdFragment.add(((Activity) getContext()).getFragmentManager().beginTransaction(), fragment);

        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item, parent, false);
        }
        TextView txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
        txtItemName.setText(filteredCategories.get(position).getItemId());
        TextView txtItemDetails = (TextView) convertView.findViewById(R.id.txtItemDetails);
        txtItemDetails.setText(filteredCategories.get(position).item_desc);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgItem);
        Picasso.with(context)
                .load(filteredCategories.get(position).image)
                .placeholder(R.drawable.no_item_image)
                .error(R.drawable.no_item_image)
                .into(imageView);
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
                    if (m.getItemId().toLowerCase().contains(constraint) || m.item_desc.toLowerCase().contains(constraint))
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
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredCategories = (ArrayList<Item>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredCategories.size(); i < l; i++)
                add(filteredCategories.get(i));
            notifyDataSetInvalidated();
        }
    }
}