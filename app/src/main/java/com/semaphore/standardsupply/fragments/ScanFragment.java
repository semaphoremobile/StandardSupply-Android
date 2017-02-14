package com.semaphore.standardsupply.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.activity.CScanActivity;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.handlers.cart.ScanHandler;
import com.semaphore.standardsupply.utils.AppConstants;
import com.semaphore.standardsupply.utils.CommonUtils;


public class ScanFragment extends BaseFragment {

    ListView lstScannedItems;
    Button btnScan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scan_history, container, false);
        lstScannedItems = (ListView) v.findViewById(R.id.lstScanHistory);
        btnScan = (Button) v.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(scanListener);
        return v;
    }

    public void onViewWillAppear() {
        getActivity().getActionBar().setTitle("Scan");
    }

    @Override
    protected String getName() {
        return "Scan";
    }

    OnClickListener scanListener = new OnClickListener() {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // We came from the scanning activity; the return intent contains a RESULT_EXTRA key
        // whose value is an ArrayList of BarcodeResult objects that we found while scanning.
        if (resultCode == Activity.RESULT_OK) {
            Log.v("scan ", "AddToOrderFragment Java scan entered reslut ok");
            String scanvalue = data.getExtras().getString(AppConstants.KEY_SCAN_ID).replace(" ", "%20");

            if (!scanvalue.equals("")) {
                //String barcode = barcodes.get(0).barcodeString;
                if (CommonUtils.isNetworkAvailable(getActivity())) {
                    ScanHandler handler = new ScanHandler(ScanFragment.this, scanvalue);
                    handler.request();
                } else {
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage("Please Check your Network Connectivity.").setNeutralButton("OK", null);

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            } else {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("Sorry the item cannot be found!").setNeutralButton("OK", null);

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
