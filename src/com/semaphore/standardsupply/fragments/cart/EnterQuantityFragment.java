package com.semaphore.standardsupply.fragments.cart;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.model.CartItem;
import com.semaphore.standardsupply.model.Job;

public class EnterQuantityFragment extends BaseFragment {
	ListView lstJobNos;
	CartItem cartItem;
	@Override
	protected String getName() {
		return "Enter Quantity";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_enter_quantity, container,false);

		v.findViewById(R.id.btnAddJobNo).setOnClickListener(addListener);
		lstJobNos = (ListView) v.findViewById(R.id.lstJobNos);
		cartItem = (CartItem) getArguments().getSerializable("cart_item");

		return v;
	}

	public void onViewWillAppear() {
		lstJobNos.setAdapter(new JobNosAdapter(getActivity(), R.id.lstJobNos, cartItem.job_numbers, this));
		getActivity().getActionBar().setTitle("Enter Quantity");
	}
	OnClickListener addListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			final android.widget.EditText jobNumber = new android.widget.EditText(getActivity());
			builder.setView(jobNumber);

			// 2. Chain together various setter methods to set the dialog characteristics
			builder.setMessage("Enter the job number:")
			.setTitle("");

			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					String jNo = jobNumber.getText().toString();
					if(jNo.equals("")){
						if(jNo.equals("")){
							AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.getActivity());
							builder.setTitle("Job number required");
							builder.setMessage("The job number cannot be blank.");
							builder.setPositiveButton("OK", null);
							builder.create().show();
							return;
						}
					}
					Job job = new Job();
					job.number = jNo;
					job.quantity = 1;
					cartItem.job_numbers.add(job);
					((JobNosAdapter)lstJobNos.getAdapter()).addJob(job);
				}
			});

			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	};

	@Override
	public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_cart, menu);
	}

	@SuppressLint("CommitTransaction")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_cart){
			lstJobNos.setAdapter(null);
			add(getActivity().getFragmentManager().beginTransaction(), new CartFragment());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

class JobNosAdapter extends ArrayAdapter<Job>{

	private ArrayList<Job> jobs;
	Context context;
	private EnterQuantityFragment cFragment;
	public JobNosAdapter(Context context,
			int textViewResourceId, ArrayList<Job> objects,EnterQuantityFragment fragment ) {
		super(context, textViewResourceId, objects);
		this.jobs = new ArrayList<Job>();
		this.jobs.addAll(objects);
		this.context = context;
		this.cFragment = fragment;

	}

	public void addJob(Job job) {
		jobs.add(job);
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		}
	};



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_job, parent, false);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.txtItemName);
		textView.setText(cFragment.cartItem.getItemId());

		final TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
		txtQuantity.setText("" + jobs.get(position).quantity);
		txtQuantity.setTag(jobs.get(position));
		txtQuantity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(cFragment.getActivity());

				final android.widget.EditText jobNumber = new android.widget.EditText(cFragment.getActivity());
				builder.setView(jobNumber);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Enter quantity:")
				.setTitle("");

				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						String jNo = jobNumber.getText().toString();
						if(jNo.equals("") || jNo.equals("0")){
							AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.getActivity());
							builder.setTitle("Invalid Quantity");
							builder.setMessage("The quantity cannot be blank or 0.");
							builder.setPositiveButton("OK", null);
							builder.create().show();
							return;
						}
						Job job = (Job)txtQuantity.getTag();
						job.quantity = Integer.parseInt(jNo);
						notifyDataSetChanged();
						notifyDataSetInvalidated();
					}
				});

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		final TextView txtJobNo = (TextView) convertView.findViewById(R.id.txtJobNo);
		if(position < jobs.size()){
			String jobNo = jobs.get(position).number;
			txtJobNo.setText("Job No. " + (jobNo.equals("") ? "Enter job number!" : jobNo));
			txtJobNo.setTag(jobs.get(position));
			txtJobNo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// 1. Instantiate an AlertDialog.Builder with its constructor
					AlertDialog.Builder builder = new AlertDialog.Builder(cFragment.getActivity());

					final android.widget.EditText jobNumber = new android.widget.EditText(cFragment.getActivity());
					builder.setView(jobNumber);

					// 2. Chain together various setter methods to set the dialog characteristics
					builder.setMessage("Enter job number:")
					.setTitle("");

					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							String jNo = jobNumber.getText().toString();
							if(jNo.equals("")){
								AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.getActivity());
								builder.setTitle("Job number required");
								builder.setMessage("The job number cannot be blank.");
								builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										//Do nothing
									}
								});
								builder.create().show();
								return;
							}
							Job job = (Job)txtJobNo.getTag();
							job.number = jNo;
							notifyDataSetChanged();
							notifyDataSetInvalidated();
						}
					});

					// 3. Get the AlertDialog from create()
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			});

		}
		return convertView;
	}
}
