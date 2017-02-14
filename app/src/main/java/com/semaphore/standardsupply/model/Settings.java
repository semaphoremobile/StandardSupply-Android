package com.semaphore.standardsupply.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

import com.google.gson.Gson;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.model.User;

public class Settings {

	private static final String CURRENT_USER = "current_user";
	private static final String DEFAULT_LOCATION = "default_location";
	private static final String DEFAULT_TRUCK = "default_truck";
	private static final String JOB_FLAG = "job_flag";


	public static void putInt(Activity activity, String key, int value) {
		SharedPreferences prefs = activity.getSharedPreferences(
				"com.semaphore.activity", Context.MODE_PRIVATE);
		prefs.edit().putInt(key, value).commit();
	}

	public static Integer getInt(Activity activity, String key) {
		SharedPreferences prefs = activity.getSharedPreferences(
				"com.semaphore.activity", Context.MODE_PRIVATE);
		return prefs.getInt(key, (Integer) Integer.MIN_VALUE);
	}

	public static void putString(Activity activity, String key, String value) {
		SharedPreferences prefs = activity.getSharedPreferences(
				"com.semaphore.activity", Context.MODE_PRIVATE);
		prefs.edit().putString(key, value).commit();
	}

	public static String getString(Activity activity, String key) {
		SharedPreferences prefs = activity.getSharedPreferences(
				"com.semaphore.activity", Context.MODE_PRIVATE);
		return prefs.getString(key, "");
	}

	public static void putObject(Activity activity, Serializable object, String key){
		SharedPreferences appSharedPrefs = activity.getSharedPreferences(
				"com.semaphore.activity", Context.MODE_PRIVATE);
		Editor prefsEditor = appSharedPrefs.edit();
		if(object == null){
			prefsEditor.remove(key);
			prefsEditor.commit();
			return;
		}
		Gson gson = new Gson();
		String json = gson.toJson(object);

		if(json != null){
			prefsEditor.putString(key, json);
			prefsEditor.commit();
		}
	}

	public static <T> T getObject(Activity activity, String key, Class<T> type){
		SharedPreferences appSharedPrefs = activity.getSharedPreferences(
				"com.semaphore.activity", Context.MODE_PRIVATE);
		String json = appSharedPrefs.getString(key, "");
		Gson gson = new Gson();
		return gson.fromJson(json, type);
	}

	public static User getUser(Activity activity) {
		return (User) Settings.getObject(activity, CURRENT_USER, User.class);
	}

	public static void putUser(Activity activity, User user) {
		Settings.putObject(activity, user, CURRENT_USER);
	}

	public static IdNameObj getLocation(Activity activity) {
		return (IdNameObj) Settings.getObject(activity, DEFAULT_LOCATION, IdNameObj.class);
	}

	public static void putLocation(Activity activity, IdNameObj location) {
		Settings.putObject(activity, location, DEFAULT_LOCATION);
	}

	public static IdNameObj getTruck(Activity activity) {
		return (IdNameObj) Settings.getObject(activity, DEFAULT_TRUCK, IdNameObj.class);
	}

	public static void putTruck(Activity activity, IdNameObj truck) {
		Settings.putObject(activity, truck, DEFAULT_TRUCK);
	}

	public static void putJobFlag(Activity activity, String jobflag) {
		Settings.putObject(activity, jobflag, JOB_FLAG);
	}

	public static String getJobFlag(Activity activity){
		return (String) Settings.getObject(activity, JOB_FLAG, String.class);
	}

	public static void addAddress(Address addr) { 
		File f = new File(Environment.getExternalStorageDirectory() + File.separator + "addresses.dat");

		FileOutputStream   outStream  = null;
		ObjectOutputStream objectOutStream  = null;

		try {
			if(f.exists()){
				outStream = new FileOutputStream(f, true);
				objectOutStream = new AppendingObjectOutputStream(outStream);
			}
			else{
				f.createNewFile();
				outStream = new FileOutputStream(f);
				objectOutStream = new ObjectOutputStream(outStream);
			}
			objectOutStream.writeObject(addr);
			int size = getInt(HomeActivity.getActivity(), "addressSize") + 1;
			if(size <= 0){
				size = 1;
			}
			putInt(HomeActivity.getActivity(), "addressSize", size);
		}
		catch (Exception e) {
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
		finally {
			try {
				if (outStream != null)   outStream.close();
				if (objectOutStream != null)   objectOutStream.close();
			}
			catch (Exception e) { /* do nothing */ }
		}
	}

	public static ArrayList<Address> loadAddresses(){
		File f = new File(Environment.getExternalStorageDirectory() + File.separator + "addresses.dat");
		if(f.exists() == false){
			return new ArrayList<Address>();
		}

		FileInputStream inStream = null;
		ObjectInputStream objectInStream = null;
		try {
			inStream = new FileInputStream(f);
			objectInStream = new ObjectInputStream(inStream);
			int size = getInt(HomeActivity.getActivity(), "addressSize");
			ArrayList<Address> rl = new ArrayList<Address>();
			for (int c=0; c < size; c++)
				rl.add((Address) objectInStream.readObject());
			return rl;
		} catch (Exception e) {
			if(inStream != null){
				return new ArrayList<Address>();
			}
		}
		finally{
			try{
				if(inStream != null) inStream.close();
				if(objectInStream != null) objectInStream.close();
			}catch(Exception e){

			}
		}
		return new ArrayList<Address>();
	}

	public static void deleteAddresses(){
		File f = new File(Environment.getExternalStorageDirectory() + File.separator + "addresses.dat");

		try {
			if(f.exists()){
				f.delete();
			}

			putInt(HomeActivity.getActivity(), "addressSize", 0);
		}
		catch (Exception e) {

		}
	}
}