package com.semaphore.standardsupply.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.home.addItems.CompanyFavoritesFragment;
import com.semaphore.standardsupply.fragments.home.addItems.MyFavoritesFragment;
import com.semaphore.standardsupply.fragments.home.addItems.ShoppingListFragment;
import com.semaphore.standardsupply.fragments.home.profile.AdvertisementFragment;
import com.semaphore.standardsupply.handlers.home.AdvertisementHandler;
import com.semaphore.standardsupply.model.Model;

public class FavoritesFragment extends BaseFragment implements OnClickListener,
		IAdFragment {

	ListView lstFavs;
	ImageView imgAd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = (View) inflater.inflate(R.layout.fragment_favorites,
				container, false);
		lstFavs = (ListView) v.findViewById(R.id.lstItems);
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("My Favorites");
		categories.add("Company Favorites");
		categories.add("Company Shopping Lists");
		FavsAdapdter adapter = new FavsAdapdter(getActivity(), R.id.lstItems,
				categories, this);
		lstFavs.setOnItemClickListener(adapter.listener);
		lstFavs.setAdapter(adapter);

		imgAd = (ImageView) v.findViewById(R.id.advertisement);
		v.findViewById(R.id.advertisement).setOnClickListener(this);
		return v;
	}

	@Override
	protected String getName() {
		return "Favorites";
	}

	@Override
	public void onViewWillAppear() {
		getActivity().getActionBar().setTitle("Favorites");
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		AdvertisementHandler handler = new AdvertisementHandler(this, size.x);
		handler.request();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.advertisement:
			onAdvertisementClick();
			break;
		}
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
	}

	@Override
	public void receivedAd() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					final Bitmap bitmap = BitmapFactory
							.decodeStream((InputStream) new URL(Model
									.getInstance().advertisement.image_url)
									.getContent());
					Activity activity = FavoritesFragment.this.getActivity();
					if (activity == null) {
						return;
					}
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							imgAd.setImageBitmap(bitmap);
						}
					});
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}

class FavsAdapdter extends ArrayAdapter<String> {

	private ArrayList<String> categories;
	Context context;
	FavoritesFragment fFragment;

	public FavsAdapdter(Context context, int textViewResourceId,
			ArrayList<String> objects, FavoritesFragment fragment) {
		super(context, textViewResourceId, objects);
		this.categories = objects;
		this.context = context;
		fFragment = fragment;
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 == 0) {
				fFragment.add(((Activity) getContext()).getFragmentManager()
						.beginTransaction(), new MyFavoritesFragment());
			} else if (arg2 == 1) {
				fFragment.add(((Activity) getContext()).getFragmentManager()
						.beginTransaction(), new CompanyFavoritesFragment());
			} else if (arg2 == 2) {
				fFragment.add(((Activity) getContext()).getFragmentManager()
						.beginTransaction(), new ShoppingListFragment());
			}
		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.row_category, parent, false);
		}
		TextView textView = (TextView) convertView
				.findViewById(R.id.txtCategoryName);
		textView.setText(categories.get(position));
		return convertView;
	}
}
