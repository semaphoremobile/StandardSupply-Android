package com.semaphore.standardsupply.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.SSApplication;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.fragments.home.profile.MyTrucksFragment;
import com.semaphore.standardsupply.handlers.helplinks.HelpLinksHandler;
import com.semaphore.standardsupply.model.Help;
import com.semaphore.standardsupply.model.Model;

public class HelpInfoFragment extends BaseFragment {
	ListView lstHelpInfoItems;
	EditText txtLinkUrl;
	Button btnAddUrl;
	String[] urlValues;
	HelpInfoItemsAdapter adapter;
	ArrayList<Help> arrayList = new ArrayList<Help>();
	String url_val;
	int position_val;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		HelpLinksHandler handler_help = new HelpLinksHandler(this);
		handler_help.request();

		View v = inflater.inflate(R.layout.fragment_help, container, false);

		lstHelpInfoItems = (ListView) v.findViewById(R.id.lstHelpInfoItems);
		txtLinkUrl = (EditText) v.findViewById(R.id.editTextHelpURL);
		btnAddUrl = (Button) v.findViewById(R.id.buttonHelpURL);
		btnAddUrl.setVisibility(View.GONE);
		registerForContextMenu(lstHelpInfoItems);

		btnAddUrl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				addUrl();

			}
		});
		return v;
	}

	@Override
	protected String getName() {
		return "Help";
	}

	private void addUrl() {
		url_val = txtLinkUrl.getText().toString();
		if (url_val == null || url_val.equals("")) {
			Toast.makeText(getActivity(), "Please Enter a Url",
					Toast.LENGTH_LONG).show();

			return;
		}
		// boolean validUrl = Patterns.WEB_URL.matcher(url_val).matches();
		if (!URLUtil.isValidUrl(url_val)) {
			Toast.makeText(getActivity(), "Invalid URL", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// if (!validUrl) {
		// Toast.makeText(getActivity(), "Please Enter a Valid Url",
		// Toast.LENGTH_LONG).show();
		//
		// return;
		// }

		if (SSApplication.val) {
			Help help = new Help();
			help.url = txtLinkUrl.getText().toString();
			String old_url = urlValues[position_val];
			help.val = old_url;
			help.type = "Extra";
			// arrayList.add(position_val, help);
			Model.getInstance().persistHelpInfo(help);
			adapter.notifyDataSetChanged();
			SSApplication.val = false;
			setList();
			txtLinkUrl.setText("");
		} else {
			Help help = new Help();
			help.url = txtLinkUrl.getText().toString();
			help.type = "Extra";
			Model.getInstance().persistHelpInfo(help);
			setList();
			txtLinkUrl.setText("");

		}

	}

	public void editLink(int position) {
		// arrayList.remove(position);
		position_val = position;
		// adapter.notifyDataSetChanged();
		txtLinkUrl.setText(urlValues[position]);
		txtLinkUrl.setSelection(txtLinkUrl.getText().length());
		SSApplication.val = true;

	}

	public void removeLink(int position) {

		Help help = new Help();
		help.url = arrayList.get(position).url;
		arrayList.remove(position);
		Model.getInstance().updateHelpInfo(help);
		setList();
	}

	private void setList() {
		arrayList.clear();

		ArrayList<Help> arrayListFirst = new ArrayList<Help>();
		if (!SSApplication.val) {
			System.out.println("getDefaultHelpLinkInfo ::  "
					+ Model.getInstance().getDefaultHelpLinkInfo().size());
			arrayListFirst.addAll(Model.getInstance().getDefaultHelpLinkInfo());

		}

		arrayListFirst.addAll(Model.getInstance().getHelpInfo());
		System.out.println("LIST SIZE ::  " + arrayListFirst.size());
		adapter = new HelpInfoItemsAdapter(getActivity(),
				R.id.lstHelpInfoItems, arrayListFirst, this);
		urlValues = getUrl(arrayListFirst);
		System.out.println("arrayListFirst::: " + arrayListFirst.size());
		arrayList = arrayListFirst;
		lstHelpInfoItems.setOnItemClickListener(adapter.listener);
		System.out.println("arrayList::: " + arrayList.size());
		lstHelpInfoItems.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	private String[] getUrl(ArrayList<Help> theUrlArray) {
		String[] aURLArray = new String[theUrlArray.size()];
		try {
			for (int index = 0; index < theUrlArray.size(); index++) {
				aURLArray[index] = theUrlArray.get(index).url;
			}
		} catch (Exception theJsonException) {
			theJsonException.printStackTrace();
			Log.e(this.getClass().getName(),
					"Exception when constructing a team name array");
		}
		return aURLArray;
	}

	public void onViewWillAppear() {
		setList();

		if(HomeActivity.isHelp) {
			getActivity().getActionBar().setTitle("Help Info");
		}else{
			getActivity().getActionBar().setTitle("Product Info");

		}

		HomeActivity.isHelp=true;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle().equals("Delete")) {
			AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Model.getInstance().getCart().items.remove(acmi.position);
			Model.getInstance().persistCart();
			// setList();
			return false;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add("Delete");
	}

	OnClickListener changeTruckListener = new OnClickListener() {

		@SuppressLint("CommitTransaction")
		@Override
		public void onClick(View v) {
			add(getActivity().getFragmentManager().beginTransaction(),
					new MyTrucksFragment());
		}
	};

	@Override
	public void onCreateOptionsMenu(android.view.Menu menu,
			android.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_help, menu);
	}

	@SuppressLint("CommitTransaction")
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == R.id.action_add_items) {
			// Model.getInstance().restoreHelpInfo();
			//
			// Model.getInstance().persistHelpLinkInfo(SSApplication.helpList);
			// adapter.notifyDataSetChanged();
			// System.out.println("persistHelpLinkInfo.helpList::"
			// + Model.getInstance().getDefaultHelpLinkInfo().size());
			// setList();

		} else if (item.getItemId() == R.id.action_submit) {
		}

		return super.onOptionsItemSelected(item);
	}
}

class HelpInfoItemsAdapter extends ArrayAdapter<Help> {

	private ArrayList<Help> cartItems;
	Context context;
	HelpInfoFragment cFragment;

	public HelpInfoItemsAdapter(Context context, int textViewResourceId,
			ArrayList<Help> objects, HelpInfoFragment fragment) {
		super(context, textViewResourceId, objects);
		this.cartItems = new ArrayList<Help>();
		this.cartItems.addAll(objects);
		this.context = context;
		cFragment = fragment;

	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			Toast.makeText(getContext(), "Item clicked", Toast.LENGTH_SHORT)
					.show();
		}
	};

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// cartItems = Model.getInstance().getCart().items;
		final Help help = cartItems.get(position);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_help_item, parent,
					false);
		}
		TextView txtItemName = (TextView) convertView
				.findViewById(R.id.txtHelpItem);
		txtItemName.setText(cartItems.get(position).url);

		Button btnEdit = (Button) convertView.findViewById(R.id.button1);
		Button btnDelete = (Button) convertView.findViewById(R.id.button2);
		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cFragment.editLink(position);
			}
		});

		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(context)
						.setTitle("Alert")
						.setMessage("Are you sure want to delete?")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int whichButton) {
										cFragment.removeLink(position);
									}
								}).setNegativeButton(android.R.string.no, null)
						.show();

			}
		});

		if (help.type.equalsIgnoreCase("Default")) {
			btnEdit.setVisibility(View.INVISIBLE);
			btnDelete.setVisibility(View.INVISIBLE);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = help.url;
				if (!url.startsWith("http://") && !url.startsWith("https://")) {
					url = "http://" + url;
				}
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url));
				cFragment.startActivity(browserIntent);
			}
		});

		return convertView;
	}

}