package com.semaphore.standardsupply.fragments;

import com.semaphore.standardsupply.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelpFragment extends BaseFragment {

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_help, container, false);
		return v;
	}
	
	@Override
	public void onViewWillAppear() {
		getActivity().getActionBar().setTitle("Product Info");
	}
	
	@Override
	protected String getName() {
		return "Technical";
	}
}
