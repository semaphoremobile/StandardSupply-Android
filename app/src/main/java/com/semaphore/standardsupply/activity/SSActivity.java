package com.semaphore.standardsupply.activity;

import android.app.Activity;
import android.content.res.Configuration;

/**
 * A base class to handle things that might be common to all Activites. e.g.
 * error handling.
 * 
 * @author arjavdave
 * 
 */
public abstract class SSActivity extends Activity {

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onResume() {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onResume();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//
//	}

	public void OnHandlerCompleted(){

	}
}
