package com.semaphore.standardsupply.utils;

import java.util.ArrayList;
import java.util.Iterator;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;

public class SSTabListner implements TabListener {

	protected BaseFragment fragment; 
	private ArrayList<BaseFragment> fragments;
	
	public SSTabListner(BaseFragment fragment){
		this.fragment = fragment;
		fragments = new ArrayList<BaseFragment>();
		fragments.add(fragment);
	}
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction ft) {
		fragment.onTabReselected();
		for (Iterator<BaseFragment> iterator = fragments.iterator(); iterator.hasNext();) {
			BaseFragment frg = (BaseFragment) iterator.next();
			if (frg.getClass() != fragment.getClass()) {
				ft.remove(frg);	
			}
		}
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction ft) {
		
		ft.replace(R.id.fragment_container, fragment);
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction ft) {
		for (Iterator<BaseFragment> iterator = fragments.iterator(); iterator.hasNext();) {
			BaseFragment frg = (BaseFragment) iterator.next();
			if (frg.getClass() != fragment.getClass()) {
				ft.remove(frg);	
			}
		}
	}
	
	public ArrayList<BaseFragment> getFragments(){
		return fragments;
	}
}
