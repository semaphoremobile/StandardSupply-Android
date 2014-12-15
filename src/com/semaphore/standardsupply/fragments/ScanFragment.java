package com.semaphore.standardsupply.fragments;

import java.util.ArrayList;

import com.ebay.redlasersdk.BarcodeResult;
import com.ebay.redlasersdk.BarcodeScanActivity;
import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.activity.RLScanActivity;
import com.semaphore.standardsupply.handlers.cart.ScanHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


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
			Intent scanIntent = new Intent(HomeActivity.getActivity(),RLScanActivity.class);
			scanIntent.putExtra(RLScanActivity.INTENT_MULTI_SCAN, false);
			startActivityForResult(scanIntent,1);
		}
	};
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// We came from the scanning activity; the return intent contains a RESULT_EXTRA key
		// whose value is an ArrayList of BarcodeResult objects that we found while scanning.
		if (resultCode == Activity.RESULT_OK) 
		{			
			ArrayList<BarcodeResult> barcodes = data.getParcelableArrayListExtra(BarcodeScanActivity.RESULT_EXTRA);
			if (barcodes != null && barcodes.size() > 0)
			{
				String barcode = barcodes.get(0).barcodeString;
				ScanHandler handler = new ScanHandler(ScanFragment.this, barcode);
				handler.request();
			}
			else{
				
			}
		}
		else{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
