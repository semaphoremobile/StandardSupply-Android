package com.semaphore.standardsupply.fragments.home;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.HelpFragment;
import com.semaphore.standardsupply.fragments.IAdFragment;
import com.semaphore.standardsupply.fragments.home.addItems.AddToOrderFragment;
import com.semaphore.standardsupply.fragments.home.profile.ProfileFragment;
import com.semaphore.standardsupply.handlers.home.AdvertisementHandler;
import com.semaphore.standardsupply.handlers.home.GetJobFlagHandler;
import com.semaphore.standardsupply.model.Model;
import com.squareup.picasso.Picasso;

@SuppressLint("CommitTransaction")
public class HomeFragment extends BaseFragment implements OnClickListener, IAdFragment {

	ImageView imgAd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		GetJobFlagHandler handler = new GetJobFlagHandler(this);
		handler.request();
		//TODO: Ensure container != null
		View v = inflater.inflate(R.layout.fragment_home, container,false);
		v.findViewById(R.id.profileTextView).setOnClickListener(this);
		v.findViewById(R.id.addItemsTextView).setOnClickListener(this);
		v.findViewById(R.id.productInfoTextView).setOnClickListener(this);
		v.findViewById(R.id.notificationsTextView).setOnClickListener(this);

		imgAd = (ImageView) v.findViewById(R.id.advertisement);
		return v;
		
	}
	@Override
	protected String getName() {
		return "Home";
	}

	@Override
	public void onViewWillAppear() {
		getActivity().getActionBar().setTitle("Home");
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		AdvertisementHandler handler = new AdvertisementHandler(this, size.x);
		handler.request();

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.profileTextView: onProfileClick(); break;
		case R.id.addItemsTextView: onAddItemsClick(); break;
		case R.id.productInfoTextView: onHelpClick(); break;
		case R.id.notificationsTextView: onNotificationsClick(); break;
		}
	}

	private void onProfileClick() {
		FragmentTransaction fmt = this.getFragmentManager().beginTransaction();
		add(fmt, new ProfileFragment());
	}

	private void onAddItemsClick() {
		FragmentTransaction fmt = this.getFragmentManager().beginTransaction();
		add(fmt, new AddToOrderFragment());
	}

	private void onHelpClick() {

		FragmentTransaction fmt = this.getFragmentManager().beginTransaction();
		add(fmt, new HelpFragment());
	}

	private void onNotificationsClick(){
		FragmentTransaction fmt = this.getFragmentManager().beginTransaction();
		add(fmt, new NotificationsFragment());
	}

	@Override
	public void receivedAd() {
		Activity activity = getActivity();
		if(activity != null){
			Picasso.with(getActivity())
			.load(Model.getInstance().advertisement.image_url)
			.placeholder(imgAd.getDrawable())
			.into(imgAd);
		}
	}
}
