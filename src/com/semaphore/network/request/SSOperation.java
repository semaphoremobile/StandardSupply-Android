package com.semaphore.network.request;


import java.util.Map;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

import android.util.Log;

import com.google.gson.Gson;
import com.semaphore.network.helper.ISSCallback;

/**
 * A base class for making requests to the servers. 
 * By default it return a http method 'POST'. 
 * It can be changed by overriding the getHttpMethod() method.
 * Also by default it prefixes "http://162.242.148.122/api" to the action required.  
 * In order to make a call to a different url override getActionUrl() method.
 * Similarly override getContentTypeHeader() to have a Content-Type other than 'application/json'   
 * @author arjavdave
 *
 */
public class SSOperation {
	
	public static final String METHOD_GET = "GET";

	public static final String METHOD_POST = "POST";

	public static final String METHOD_PUT = "PUT";
	
	public static final String METHOD_DELETE = "DELETE";

	
	public String httpMethod = METHOD_POST;
	private ISSCallback callback = null;
	private String action = null;
	
	/**
	 * Constructor 
	 * @param action The action part in the url. 
	 * @param callback The callback which needs to be invoked upon response. 
	 */
	public SSOperation(String action, ISSCallback callback){
		this.action = action;
		this.callback = callback;
	}
	
	public ISSCallback getCallback(){
		return callback;
	}
	
	protected String getHttpMethod(){
		return httpMethod;
	}
	
	protected String getActionUrl(){
		String actionUrl = action == null ? "" : "http://stockmytruck.com/api" + action;
		Log.i("RequestResponse", "ActionUrl: " + actionUrl); //Staging: 162.242.148.122
		return actionUrl;
	}
	
	public HttpRequestBase getRequestBase(){
		if(METHOD_GET.equals(getHttpMethod())){
			return new HttpGet(getActionUrl());
		}
		else if(METHOD_POST.equals(getHttpMethod())){
			return new HttpPost(getActionUrl());
		}
		else if(METHOD_PUT.equals(getHttpMethod())){
			return new HttpPut(getActionUrl());
		}
		else if(METHOD_DELETE.equals(getHttpMethod())){
			return new HttpDelete(getActionUrl());
		}
		return null;
	}
	
	public String getContentTypeHeader(){
		return "application/x-www-form-urlencoded";
	}
	
	
	/**
	 * If the server needs a json request, override this method to have a HashMap of the params. 
	 * The getRequestString() will take care of converting it to json string.
	 * @return
	 */
	protected Map<String, Object> getRequest(){
		return null;
	}
	
	/**
	 * Override it only if there is a need of non-json request to be made. 
	 * If the request is pure json use getRequest() method.
	 * @return Converts the Map<String,Object> returned by getRequest() into json.
	 */
	public String getRequestString(){
		Object obj = getRequest();
		Gson gson = new Gson();
		return obj == null ? null : gson.toJson(obj);
	}
	
	/**
	 * 
	 * @return If the operation response needs to contain auth_token. 
	 */
	public boolean isAuthenticationRequired(){
		return true;
	}
	
}
