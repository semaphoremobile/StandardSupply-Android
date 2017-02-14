package com.semaphore.standardsupply;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.content.Intent;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.model.Help;
import com.semaphore.standardsupply.model.IdNameObj;
import com.semaphore.standardsupply.model.Item;
import com.semaphore.standardsupply.model.ItemCategory;

public class SSApplication extends Application {
	public static boolean val = false;
	public static ArrayList<Help> helpList = new ArrayList<Help>(); 
	public static ArrayList<Item> itemList = new ArrayList<Item>();
	public static IdNameObj location;
	public static String jobNo;
	public static String poNo;
	public static boolean pickUp = false;
	public static ArrayList<ItemCategory> itemCategoryList = new ArrayList<ItemCategory>();
	public static String searchText = "";

	/**
	 * Enum used to identify the tracker that needs to be used for tracking.
	 * 
	 * A single tracker is usually enough for most purposes. In case you do need
	 * multiple trackers, storing them all in Application object helps ensure
	 * that they are created only once per application instance.
	 */
	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app.
		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
						// roll-up tracking.
		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
							// company.
	}

	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {

			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = analytics.newTracker(R.xml.global_tracker);
			mTrackers.put(trackerId, t);
		}
		return mTrackers.get(trackerId);
	}

	@Override
	public void onCreate() {
		// Setup handler for uncaught exceptions.
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				handleUncaughtException(thread, e);
			}
		});

		Parse.initialize(this, "nf3JJBQKPNg2ZOSHRcmgXHcBivFrVJl0y76feZjj",
				"KI1WYSRaT5jBKqDUBUppv2uN5Ple47733XymM5EY");
		PushService.setDefaultPushCallback(this, HomeActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		super.onCreate();

	}

	public void handleUncaughtException(Thread thread, Throwable e) {
		e.printStackTrace(); // not all Android versions will print the stack
								// trace automatically

		Intent intent = new Intent();
		intent.setAction("com.semaphore.standardsupply.SendLog"); // see step 5.
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when
														// starting from
														// Application
		intent.putExtra("exception", e);
		startActivity(intent);

		System.exit(1); // kill off the crashed app
	}
}
