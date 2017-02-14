package com.semaphore.standardsupply.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.FavoritesFragment;
import com.semaphore.standardsupply.fragments.HelpInfoFragment;
import com.semaphore.standardsupply.fragments.MyOrdersFragment;
import com.semaphore.standardsupply.fragments.cart.CartFragment;
import com.semaphore.standardsupply.fragments.home.HomeFragment;
import com.semaphore.standardsupply.fragments.home.addItems.CategoriesFragment;
import com.semaphore.standardsupply.fragments.home.addItems.ItemDetailFragment;
import com.semaphore.standardsupply.model.User;
import com.semaphore.standardsupply.utils.SSTabListner;

import io.fabric.sdk.android.Fabric;

public class HomeActivity extends SSActivity {

	private static Activity homeActivity;

	public static boolean isHelp=true;

	Tab homeTab, searchTab, scanTab, favoritesTab, helpTab, myOrdersTab,
			cartTab;
	BaseFragment homeFragment = new HomeFragment();
	BaseFragment searchFragment = new CategoriesFragment();
	BaseFragment scanFragment = new ItemDetailFragment();
	BaseFragment favoritesFragment = new FavoritesFragment();
	BaseFragment helpFragment = new HelpInfoFragment();
	BaseFragment myOrdersFragment = new MyOrdersFragment();
	BaseFragment cartFragment = new CartFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homeActivity = this;
		Fabric.with(this, new Crashlytics());
		String email = User.currentUser().email;
		email = email.replace('@', '-').replace('.', '-');
		PushService.subscribe(this, email, HomeActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		setContentView(R.layout.activity_home);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		homeTab = actionBar.newTab().setText(R.string.home_tab);
		searchTab = actionBar.newTab().setText(R.string.search);
		scanTab = actionBar.newTab().setText(R.string.scan);
		favoritesTab = actionBar.newTab().setText(R.string.favorites);
		myOrdersTab = actionBar.newTab().setText(R.string.my_orders);
		helpTab = actionBar.newTab().setText(R.string.help);
		cartTab = actionBar.newTab().setText(R.string.cart);

		SSTabListner homeListener = new SSTabListner(homeFragment);
		homeFragment.tabListener = homeListener;
		SSTabListner searchListener = new SSTabListner(searchFragment);
		searchFragment.tabListener = searchListener;
		SSTabListner scanListner = new SSTabListner(scanFragment);
		scanFragment.tabListener = scanListner;
		SSTabListner favoritesListner = new SSTabListner(favoritesFragment);
		favoritesFragment.tabListener = favoritesListner;
		SSTabListner myOrderListner = new SSTabListner(myOrdersFragment);
		myOrdersFragment.tabListener = myOrderListner;
		SSTabListner helpListner = new SSTabListner(helpFragment);
		helpFragment.tabListener = helpListner;
		SSTabListner cartListener = new SSTabListner(cartFragment);
		cartFragment.tabListener = cartListener;

		homeTab.setTabListener(homeListener);
		searchTab.setTabListener(searchListener);
		scanTab.setTabListener(scanListner);
		favoritesTab.setTabListener(favoritesListner);
		myOrdersTab.setTabListener(myOrderListner);
		helpTab.setTabListener(helpListner);
		cartTab.setTabListener(cartListener);

		actionBar.addTab(homeTab);
		actionBar.addTab(searchTab);
		actionBar.addTab(scanTab);
		actionBar.addTab(favoritesTab);
		actionBar.addTab(cartTab);
		actionBar.addTab(myOrdersTab);
		actionBar.addTab(helpTab);

	}

	public void goHome() {
		ActionBar actionBar = getActionBar();
		actionBar.setSelectedNavigationItem(0);
	}

	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = getFragmentManager();

		if (fragmentManager.getBackStackEntryCount() > 0) {
			((BaseFragment) fragmentManager
					.findFragmentById(R.id.fragment_container)).pop();
		} else {
			// finish();
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Exit Application?");
			alertDialogBuilder
					.setMessage("Click yes to exit!")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// moveTaskToBack(true);
									// android.os.Process
									// .killProcess(android.os.Process.myPid());
									// System.exit(1);
									finish();
								}
							})

					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

		}
	}

	public static Activity getActivity() {
		return homeActivity;
	}

	public static void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View focusView = getActivity().getCurrentFocus();
		if (focusView == null)
			return;
		imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
	}

}
