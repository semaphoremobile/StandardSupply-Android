package com.semaphore.standardsupply.fragments;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.SSApplication;
import com.semaphore.standardsupply.SSApplication.TrackerName;
import com.semaphore.standardsupply.utils.SSTabListner;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

public abstract class BaseFragment extends Fragment{
	
	public SSTabListner tabListener;
	
	public void onTabReselected(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		// Get tracker.
        Tracker t = ((SSApplication) getActivity().getApplication()).getTracker(
            TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(getName());

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
        
		super.onCreate(savedInstanceState);
	}
	abstract protected String getName();
	
	public void add(FragmentTransaction fmt, BaseFragment fragment, String tag){
		tabListener.getFragments().add(fragment);
		fragment.tabListener = tabListener;
		fmt.add(R.id.fragment_container,fragment, tag);
        fmt.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fmt.addToBackStack(tag);
        fmt.commit();	
	}
	public void add(FragmentTransaction fmt, BaseFragment fragment){
		add(fmt, fragment, fragment.getTag());
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		onViewWillAppear();
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
		menu.clear();
	}
	
	public void pop(){
		tabListener.getFragments().remove(this);
		getActivity().getFragmentManager().popBackStackImmediate();
		((BaseFragment)getActivity().getFragmentManager().findFragmentById(R.id.fragment_container)).onViewWillAppear();
	}

	/**
	 * For the subclasses to override. Called whenever the new fragment will become visible. Or whenever
	 * a fragment is popped and the current fragment becomes visible.
	 */
	public void onViewWillAppear() {
		//Do nothing
	}
}
