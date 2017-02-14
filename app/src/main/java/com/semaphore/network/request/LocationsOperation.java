package com.semaphore.network.request;

import com.semaphore.network.helper.ISSCallback;

public class LocationsOperation extends SSOperation {

	public LocationsOperation(ISSCallback callback) {
		super("/select_options.json", callback);
	}
	
	@Override
	public boolean isAuthenticationRequired() {
		return false;
	}

	
}
