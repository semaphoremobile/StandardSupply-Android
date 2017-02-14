package com.semaphore.standardsupply.fragments.home.addItems;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.activity.CScanActivity;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.FavoritesFragment;
import com.semaphore.standardsupply.utils.AppConstants;

public class AddToOrderFragment extends BaseFragment {

    ImageView btnSearch, btnScan, btnFavorites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_to_order, container, false);
        btnSearch = (ImageView) v.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(searchListener);

        btnScan = (ImageView) v.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(scanListener);

        btnFavorites = (ImageView) v.findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(favoritesListener);
        return v;
    }

    @Override
    protected String getName() {
        return "Add Items";
    }

    public void onViewWillAppear() {
        getActivity().getActionBar().setTitle("Add Items");
    }

    OnClickListener searchListener = new OnClickListener() {

        @SuppressLint("CommitTransaction")
        @Override
        public void onClick(View v) {
            add(getFragmentManager().beginTransaction(), new CategoriesFragment());
        }
    };

    OnClickListener scanListener = new OnClickListener() {

        @SuppressLint("CommitTransaction")
        @Override
        public void onClick(View v) {
            /*Intent scanIntent = new Intent(HomeActivity.getActivity(),RLScanActivity.class);
            scanIntent.putExtra(RLScanActivity.INTENT_MULTI_SCAN, false);
			startActivityForResult(scanIntent,1);*/
            Intent scanIntent = new Intent(HomeActivity.getActivity(), CScanActivity.class);
            scanIntent.putExtra(CScanActivity.INTENT_MULTI_SCAN, false);
            startActivityForResult(scanIntent, 1);
        }
    };

    OnClickListener favoritesListener = new OnClickListener() {

        @SuppressLint("CommitTransaction")
        @Override
        public void onClick(View v) {
            add(getFragmentManager().beginTransaction(), new FavoritesFragment());
        }
    };

    @SuppressLint("CommitTransaction")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // We came from the scanning activity; the return intent contains a RESULT_EXTRA key
        // whose value is an ArrayList of BarcodeResult objects that we found while scanning.
        if (resultCode == Activity.RESULT_OK) {
            Log.v("scan ", "AddToOrderFragment Java scan entered reslut ok");
            String scanvalue = data.getExtras().getString(AppConstants.KEY_SCAN_ID).replace(" ", "%20");
            //ArrayList<BarcodeResult> barcodes = data.getParcelableArrayListExtra(BarcodeScanActivity.RESULT_EXTRA);
            Log.v("scan ", "Scanned Value " + scanvalue);
            if (!scanvalue.equals("")) {
                ItemDetailFragment fragment = new ItemDetailFragment();
                final Bundle bundle = new Bundle();
                bundle.putSerializable("barcode", scanvalue);
                fragment.setArguments(bundle);
                add(getActivity().getFragmentManager().beginTransaction(), fragment);
            } else {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("Sorry search the item cannot be found!").setNeutralButton("OK", null);

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
