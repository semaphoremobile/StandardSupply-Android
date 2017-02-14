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
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.HelpFragment;
import com.semaphore.standardsupply.fragments.HelpInfoFragment;
import com.semaphore.standardsupply.fragments.IAdFragment;
import com.semaphore.standardsupply.fragments.home.addItems.AddToOrderFragment;
import com.semaphore.standardsupply.fragments.home.profile.AdvertisementFragment;
import com.semaphore.standardsupply.fragments.home.profile.ProfileFragment;
import com.semaphore.standardsupply.handlers.helplinks.HelpLinksHandler;
import com.semaphore.standardsupply.handlers.home.AdvertisementHandler;
import com.semaphore.standardsupply.handlers.home.GetJobFlagHandler;
import com.semaphore.standardsupply.model.Model;
import com.squareup.picasso.Picasso;

@SuppressLint("CommitTransaction")
public class HomeFragment extends BaseFragment implements OnClickListener,
		IAdFragment {

	ImageView imgAd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		GetJobFlagHandler handler = new GetJobFlagHandler(this);
		handler.request();
		// TODO: Ensure container != null
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		v.findViewById(R.id.profileTextView).setOnClickListener(this);
		v.findViewById(R.id.addItemsTextView).setOnClickListener(this);
		v.findViewById(R.id.productInfoTextView).setOnClickListener(this);
		v.findViewById(R.id.notificationsTextView).setOnClickListener(this);
		v.findViewById(R.id.advertisement).setOnClickListener(this);
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

		HelpLinksHandler handler_help = new HelpLinksHandler(this);
		handler_help.request();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.profileTextView:
			onProfileClick();
			break;
		case R.id.addItemsTextView:
			onAddItemsClick();
			break;
		case R.id.productInfoTextView:
			onHelpClick();
			break;
		case R.id.notificationsTextView:
			onNotificationsClick();
			break;
		case R.id.advertisement:
			onAdvertisementClick();
			break;
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


		HomeActivity.isHelp=false;
		FragmentTransaction fmt = this.getFragmentManager().beginTransaction();
		add(fmt, new HelpInfoFragment());
	}

	private void onNotificationsClick() {
		FragmentTransaction fmt = this.getFragmentManager().beginTransaction();
		add(fmt, new NotificationsFragment());
	}

	private void onAdvertisementClick() {
		// String url = Model.getInstance().advertisement.url;
		// if (!url.startsWith("http://") && !url.startsWith("https://")) {
		// url = "http://" + url;
		// }
		// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
		// Uri.parse(url));
		// startActivity(browserIntent);

		FragmentTransaction fmt = this.getFragmentManager().beginTransaction();
		add(fmt, new AdvertisementFragment());

		// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		// getActivity());
		// alertDialogBuilder.setTitle("Standard Supply");
		// alertDialogBuilder
		// .setMessage(
		// "You've clicked an Advertisement. A link will open now.. ")
		// .setCancelable(false)
		// .setPositiveButton("Yes",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		//
		// }
		// })
		//
		// .setNegativeButton("No", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		//
		// dialog.cancel();
		// }
		// });
		//
		// AlertDialog alertDialog = alertDialogBuilder.create();
		// alertDialog.show();

	}

	@Override
	public void receivedAd() {
		Activity activity = getActivity();
		if (activity != null) {
			Picasso.with(getActivity())
					.load(Model.getInstance().advertisement.image_url)
					.placeholder(imgAd.getDrawable()).into(imgAd);

		}
	}
}
