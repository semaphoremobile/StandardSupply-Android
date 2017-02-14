package com.semaphore.standardsupply.fragments.home.profile;

import java.util.Observable;
import java.util.Observer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.fragments.BaseFragment;

public class AdvertisementFragment extends BaseFragment implements Observer {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_adv, container, false);

		return v;
	}

	// public void receivedImage(){
	// if(Model.getInstance().avatarUrl == null){
	// Model.getInstance().profileImage = null;
	// imgProfile.setImageResource(R.drawable.profile_img);
	// return;
	// }
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// URL url = null;
	// try {
	// url = new URL(Model.getInstance().avatarUrl);
	// } catch (MalformedURLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// URLConnection conn;
	// try {
	// conn = url.openConnection();
	// conn.connect();
	// InputStream is = conn.getInputStream();
	//
	//
	//
	// BufferedInputStream bis = new BufferedInputStream(is);
	//
	// final Bitmap bm = BitmapFactory.decodeStream(bis);
	// if(getActivity() == null){
	// return;
	// }
	// getActivity().runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// if(bm != null){
	// Model.getInstance().profileImage = bm;
	// imgProfile.setImageBitmap(bm);
	// }
	// }
	// });
	// bis.close();
	// is.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }).start();
	// }
	//
	// @Override
	// protected String getName() {
	// return "Profile";
	// }
	//
	@Override
	public void onViewWillAppear() {
		// getActivity().getActionBar().setTitle("Profile");
		// User user = User.currentUser();
		// txtFirstName.setText(user.first_name);
		// txtLastName.setText(user.last_name);
		//
		// txtCustomerId.setText(user.customer_id == 0 ? "" : "" +
		// user.customer_id);
		// txtDefaultBranchId.setText(location == null ? "" : location.name);
	}

	//
	@Override
	public void onCreateOptionsMenu(android.view.Menu menu,
			android.view.MenuInflater inflater) {
		// super.onCreateOptionsMenu(menu, inflater);
		// inflater.inflate(R.menu.menu_truck, menu);
	}

	//
	// @SuppressLint("CommitTransaction")
	// @Override
	// public boolean onOptionsItemSelected(android.view.MenuItem item) {
	// if(item.getItemId() == R.id.action_trucks){
	// add(getActivity().getFragmentManager().beginTransaction(), new
	// MyTrucksFragment());
	// return false;
	// }
	// return super.onOptionsItemSelected(item);
	// }
	//
	// private int RESULT_LOAD_IMAGE = 1;
	// OnClickListener imageListener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// Intent i = new Intent(
	// Intent.ACTION_PICK,
	// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	//
	// startActivityForResult(i, RESULT_LOAD_IMAGE);
	// }
	// };
	//
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// getActivity();
	// if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK
	// && null != data) {
	// Uri selectedImage = data.getData();
	// // String[] filePathColumn = { MediaStore.Images.Media.DATA };
	//
	// Bitmap yourSelectedImage = null;
	// try {
	// yourSelectedImage = decodeUri(selectedImage);
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// //
	// // Cursor cursor =
	// getActivity().getContentResolver().query(selectedImage,
	// // filePathColumn, null, null, null);
	// // cursor.moveToFirst();
	// //
	// // int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	// // String picturePath = cursor.getString(columnIndex);
	// // cursor.close();
	//
	// Bitmap bm = yourSelectedImage;
	// Model.getInstance().profileImage = bm;
	// imgProfile.setImageBitmap(bm);
	//
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// bm.compress(Bitmap.CompressFormat.PNG, 0, baos); //bm is the bitmap
	// object
	// byte[] b = baos.toByteArray();
	// encodedImg = Base64.encodeToString(b, Base64.DEFAULT);
	//
	// }
	// }
	//
	// private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException
	// {
	//
	// // Decode image size
	// BitmapFactory.Options o = new BitmapFactory.Options();
	// o.inJustDecodeBounds = true;
	// BitmapFactory.decodeStream(
	// getActivity().getContentResolver().openInputStream(selectedImage), null,
	// o);
	//
	// // The new size we want to scale to
	// final int REQUIRED_SIZE = 140;
	//
	// // Find the correct scale value. It should be the power of 2.
	// int width_tmp = o.outWidth, height_tmp = o.outHeight;
	// int scale = 1;
	// while (true) {
	// if (width_tmp / 2 < REQUIRED_SIZE
	// || height_tmp / 2 < REQUIRED_SIZE) {
	// break;
	// }
	// width_tmp /= 2;
	// height_tmp /= 2;
	// scale *= 2;
	// }
	//
	// // Decode with inSampleSize
	// BitmapFactory.Options o2 = new BitmapFactory.Options();
	// o2.inSampleSize = scale;
	// return
	// BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage),
	// null, o2);
	//
	// }
	//
	// OnClickListener branchListener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // 1. Instantiate an AlertDialog.Builder with its constructor
	// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	//
	//
	// final CharSequence[] items = new
	// String[Model.getInstance().getLocationsCache().items.size()];
	//
	// int i = 0;
	// for (IdNameObj obj : Model.getInstance().getLocationsCache().items) {
	// items[i] = obj.name;
	// i++;
	// }
	//
	// builder.setItems(items, new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface arg0, int arg1) {
	// location = Model.getInstance().getLocationsCache().items.get(arg1);
	// Settings.putLocation(getActivity(),location);
	// txtDefaultBranchId.setText(location.name);
	// }
	// });
	// builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	// @SuppressLint("CommitTransaction")
	// public void onClick(DialogInterface dialog, int id) {
	//
	// }
	// });
	//
	// // 3. Get the AlertDialog from create()
	// AlertDialog dialog = builder.create();
	// dialog.show();
	// }
	// };
	//
	// OnClickListener saveListener = new OnClickListener() {
	//
	//
	// @Override
	// public void onClick(View v) {
	// if(validate()){
	// User user = User.currentUser();
	// if(encodedImg != null){
	// user.imageUrl = encodedImg;
	// }
	// user.first_name = txtFirstName.getText().toString();
	// user.last_name = txtLastName.getText().toString();
	// user.customer_id = Integer.parseInt(txtCustomerId.getText().toString());
	// user.default_branch_id = location.id;
	// UpdateUserHandler handler = new UpdateUserHandler((SSActivity)
	// getActivity());
	// handler.user = user;
	// handler.request();
	// }
	// }
	// };
	//
	// OnClickListener changePasswordListener = new OnClickListener() {
	//
	// @SuppressLint("CommitTransaction")
	// @Override
	// public void onClick(View v) {
	// FragmentTransaction fmt = getFragmentManager().beginTransaction();
	// add(fmt, new ChangePasswordFragment());
	// }
	// };
	//
	// OnClickListener logoutListener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// Settings.putUser(getActivity(), null);
	// Model.getInstance().resest();
	// Intent intent = new Intent(getActivity(), LoginActivity.class);
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	// Intent.FLAG_ACTIVITY_NEW_TASK);
	// startActivity(intent);
	// }
	// };
	//
	//
	// private boolean validate(){
	// boolean validated = true;
	// return validated;
	// }
	//
	// @Override
	// public void update(Observable arg0, Object arg1) {
	// Model.getInstance().getLocationsCache().deleteObserver(this);
	// ArrayList<IdNameObj> items =
	// Model.getInstance().getLocationsCache().items;
	// if(items == null || items.size() == 0){
	// // 1. Instantiate an AlertDialog.Builder with its constructor
	// AlertDialog.Builder builder = new
	// AlertDialog.Builder(AdvertisementFragment.this.getActivity());
	//
	// builder.setMessage("Sorry the list of locations cannot be fetched");
	// builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	// @SuppressLint("CommitTransaction")
	// public void onClick(DialogInterface dialog, int id) {
	//
	// }
	// });
	//
	// // 3. Get the AlertDialog from create()
	// AlertDialog dialog = builder.create();
	// dialog.show();
	// }
	// else{
	// if(location == null){
	// location = items.get(0);
	// Settings.putLocation(getActivity(), location);
	// txtDefaultBranchId.setText(location.name);
	// }
	// else if(location.name == null){
	// for (IdNameObj loc : items) {
	// if(loc.id == location.id){
	// location = loc;
	// Settings.putLocation(getActivity(), location);
	// txtDefaultBranchId.setText(location.name);
	// break;
	// }
	// }
	// }
	// }

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
