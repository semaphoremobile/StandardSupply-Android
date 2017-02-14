package com.semaphore.standardsupply.handlers.home.profile;

import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.request.auth.UpdateUserOperation;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.Handler;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.User;

public class AddTruckHandler extends Handler {

	private String truckName;
	public AddTruckHandler(BaseFragment fragment, String truckName) {
		super(fragment);
		this.truckName = truckName;
	}

	@Override
	public void request() {
		UpdateUserOperation operation = new UpdateUserOperation(this);
		User user = User.currentUser();
		user.truck_name = truckName;
		
		operation.user = user;
		HttpAsync.makeRequest(operation);
	}
	
	@Override
	public void callback(String result) {
		Model.getInstance().getTrucksCache().invalidate();
		fragment.pop();
	}

}
