package com.semaphore.network.request;

import java.util.Map;

import com.semaphore.network.helper.ISSCallback;
import com.semaphore.standardsupply.model.User;

public class HelpLinkOperation extends SSOperation {

	public HelpLinkOperation(ISSCallback callback) {
		// super("/getHelplinks.json?auth_token="
		// + User.currentUser().getAuthToken(), callback);
		super("/getHelplinks.json?user_email=" + User.currentUser().email
				+ "&auth_token=" + User.currentUser().getAuthToken(), callback);
	}

	@Override
	protected String getHttpMethod() {
		return METHOD_GET;
	}

	@Override
	protected Map<String, Object> getRequest() {
		return null;
	}

}
