package com.semaphore.standardsupply.activity;

import com.semaphore.standardsupply.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		final ImageView splash = (ImageView) this.findViewById(R.id.splash);
		splash.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				finish();
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				SplashActivity.this.overridePendingTransition(R.layout.fade_in, R.layout.fade_out);
				
				
			}
		}, 3000);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}

}
