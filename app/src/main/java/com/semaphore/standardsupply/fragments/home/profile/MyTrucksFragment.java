package com.semaphore.standardsupply.fragments.home.profile;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.home.profile.DeleteTruckHandler;
import com.semaphore.standardsupply.model.IdNameObj;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;

public class MyTrucksFragment extends BaseFragment implements Observer {

	ListView lstTrucks;
	DeleteTruckHandler deleteHandler;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		lstTrucks = (ListView) inflater.inflate(R.layout.fragment_list_items, container, false);
		registerForContextMenu(lstTrucks);
		return lstTrucks;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_item_detail, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getTitle().equals("Delete")){
			AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
			IdNameObj truck = Model.getInstance().getTrucksCache().items.get(acmi.position);
			deleteHandler = new DeleteTruckHandler(MyTrucksFragment.this);
			deleteHandler.truck = truck;
			deleteHandler.request();
			return false;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add("Delete");
	}

	@Override
	protected String getName() {
		return "My Trucks";
	}

	@SuppressLint("CommitTransaction")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_add){
			add(getActivity().getFragmentManager().beginTransaction(), new AddTruckFragment());
			return false;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onViewWillAppear() {
		Model.getInstance().getTrucksCache().invalidate();
		Model.getInstance().getTrucksCache().addObserver(this);
		Model.getInstance().getTrucksCache().request();
		getActivity().getActionBar().setTitle("Select Truck");
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		Model.getInstance().getTrucksCache().deleteObserver(this);
		setList();
	}

	private void setList(){
		MyTrucksAdapter adapter = new MyTrucksAdapter(getActivity(), R.id.lstItems, Model.getInstance().getTrucksCache().items);
		lstTrucks.setOnItemClickListener(adapter.listener);
		lstTrucks.setAdapter(adapter);
	}

}

class MyTrucksAdapter extends ArrayAdapter<IdNameObj>{

	private ArrayList<IdNameObj> trucks;
	Context context;
	IdNameObj truck;
	public MyTrucksAdapter(Context context,
			int textViewResourceId, ArrayList<IdNameObj> objects) {
		super(context, textViewResourceId, objects);
		this.trucks = new ArrayList<IdNameObj>();
		trucks.addAll(objects);
		this.context = context;
		truck = Settings.getTruck((Activity) context);
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Settings.putTruck((Activity) context, trucks.get(arg2));
			truck = trucks.get(arg2);
			notifyDataSetChanged();
			notifyDataSetInvalidated();
			invalidated = true;
		}
	};

	private boolean invalidated = false;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null || invalidated  == true){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(truck != null && truck.id == trucks.get(position).id){
				convertView = inflater.inflate(R.layout.row_truck_selected, parent, false);
			}
			else{
				convertView = inflater.inflate(R.layout.row_truck, parent, false);
			}
		}
		TextView txtTruckNo = (TextView) convertView.findViewById(R.id.txtTruckNo);
		txtTruckNo.setText("Truck No: " + trucks.get(position).name);
		return convertView;
	}
}
