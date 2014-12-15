package com.semaphore.standardsupply.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Observable;

import com.semaphore.network.helper.HttpAsync;
import com.semaphore.network.helper.ISSCallback;
import com.semaphore.network.request.SSOperation;

public class ItemCache <T> extends Observable implements ISSCallback{

	public String error;
	public ArrayList<T> items;
	public boolean requestInProgress;
	public ItemFactory<T> itemFactory;
	public SSOperation operation;
	private boolean hasMore;
	
	public ItemCache(String listKey, Type type) {
		itemFactory = new  ItemFactory<T>(listKey, type);
		hasMore = true;
	}
	

	public void request(){
		if(requestInProgress == true) return;
		if(hasMore == false){
			setChanged();
			notifyObservers(this);
			return;
		}
		requestInProgress = true;
		error = null;
		
		queueRequest();
	}
	
	protected void queueRequest(){
		HttpAsync.makeRequest(operation);
	}
	
	public void invalidate(){
		items = null;
		requestInProgress = false;
		hasMore = true;
		notifyObservers(this);
	}

	@Override
	public void callback(String result) {
		this.items = itemFactory.getItems(result);
		requestInProgress = false;
		hasMore = false;
		setChanged();
		notifyObservers(this);
	}

	@Override
	public void error(String error) {
		this.error = error;
		requestInProgress = false;
		setChanged();
		notifyObservers(this);
	}
}
