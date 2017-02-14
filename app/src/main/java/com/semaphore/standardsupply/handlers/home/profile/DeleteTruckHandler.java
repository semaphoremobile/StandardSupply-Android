package com.semaphore.standardsupply.handlers.home.profile;

import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.DeleteTruckOperation;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.IdNameObj;
import com.semaphore.standardsupply.model.Model;

public class DeleteTruckHandler extends Handler {

	public DeleteTruckHandler(BaseFragment fragment) {
		super(fragment);
		// TODO Auto-generated constructor stub
	}

	public IdNameObj truck;
	@Override
	public void request() {
		DeleteTruckOperation operation = new DeleteTruckOperation(truck.id, this);
		HttpAsync.makeRequest(operation);
	}

	
	@Override
	public void callback(String result) {
		Model.getInstance().getTrucksCache().invalidate();
		fragment.onViewWillAppear();
	}
}
