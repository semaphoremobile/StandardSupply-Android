package com.semaphore.standardsupply.fragments.home.addItems;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.SSApplication;
import com.semaphore.standardsupply.activity.CScanActivity;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.fragments.cart.EnterQuantityFragment;
import com.semaphore.standardsupply.handlers.cart.ScanHandler;
import com.semaphore.standardsupply.handlers.cart.SubmitOrderHandler;
import com.semaphore.standardsupply.handlers.home.addItems.FavoriteHandler;
import com.semaphore.standardsupply.model.Cart;
import com.semaphore.standardsupply.model.CartItem;
import com.semaphore.standardsupply.model.IdNameObj;
import com.semaphore.standardsupply.model.Item;
import com.semaphore.standardsupply.model.Job;
import com.semaphore.standardsupply.model.Model;
import com.semaphore.standardsupply.model.Settings;
import com.semaphore.standardsupply.utils.AppConstants;
import com.semaphore.standardsupply.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class ItemDetailFragment extends BaseFragment implements Observer {

    private Item item;
    TextView txtItemName;
    TextView txtItemDetail;
    TextView txtItemNotes;
    Button btnPickup;
    Button btnAddToCart;
    ImageView imgItem;
    String barcode;
    public ImageView btnFavorite;
    IdNameObj location;
    ProgressDialog progress;
    private String pickupJobNo;

    @Override
    protected String getName() {
        return "Item Detail";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_detail, container,
                false);

        btnFavorite = (ImageView) v.findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(favoriteListener);
        txtItemName = (TextView) v.findViewById(R.id.txtItemName);
        txtItemDetail = (TextView) v.findViewById(R.id.txtItemDetail);

        btnPickup = (Button) v.findViewById(R.id.btnPickUp);
        btnPickup.setOnClickListener(pickupListener);

        btnAddToCart = (Button) v.findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(addToCartListener);

        imgItem = (ImageView) v.findViewById(R.id.imgItemImage);

        txtItemName.setText("Loading...");
        txtItemDetail.setText("");

        txtItemNotes = (TextView) v.findViewById(R.id.txtItemNotes);
        txtItemNotes.setText("");

        if (getArguments() == null) {
            item = null;
            /*Intent scanIntent = new Intent(HomeActivity.getActivity(),
                    RLScanActivity.class);
			scanIntent.putExtra(RLScanActivity.INTENT_MULTI_SCAN, false);
			startActivityForResult(scanIntent, 1);*/
            Log.v("scan ", "scan entered intent");
            Intent scanIntent = new Intent(HomeActivity.getActivity(), CScanActivity.class);
            scanIntent.putExtra(CScanActivity.INTENT_MULTI_SCAN, "scan");
            startActivityForResult(scanIntent, 1);

        } else if (getArguments().containsKey("barcode")) {
            barcode = getArguments().getString("barcode");
            ScanHandler handler = new ScanHandler(ItemDetailFragment.this,
                    barcode);
            handler.request();
        } else {
            if (getArguments().containsKey("item")) {
                item = (Item) getArguments().getSerializable("item");
            }
        }

        updateItem();
        return v;
    }

    private void updateItem() {
        if (item != null) {
            if (item.favorite == true) {
                btnFavorite.setImageResource(R.drawable.star_icn_active);
            }
            getActivity().getActionBar().setTitle(item.getItemId());
            txtItemName.setText(item.getItemId());
            txtItemDetail.setText(item.item_desc);
            if (item.notes != null) {
                txtItemNotes.setText(Html.fromHtml(item.notes));
            }
            Picasso.with(getActivity()).load(item.image)
                    .placeholder(R.drawable.no_item_image)
                    .error(R.drawable.no_item_image).into(imgItem);
        }

    }

    @Override
    public void onViewWillAppear() {
        if (item != null) {
            getActivity().getActionBar().setTitle(item.getItemId());
        }
    }

    private void showDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final android.widget.EditText jobNumber = new android.widget.EditText(
                getActivity());
        builder.setView(jobNumber);

        // 2. Chain together various setter methods to set the dialog
        // characteristics
        builder.setMessage("Please enter the main job number:").setTitle("");

        String settingsString = Settings.getJobFlag(getActivity());
        if (settingsString != null && settingsString.equals("N")) {
            builder.setNegativeButton("Skip",
                    new DialogInterface.OnClickListener() {
                        @SuppressLint("CommitTransaction")
                        public void onClick(DialogInterface dialog, int id) {
                            Job job = new Job();
                            job.number = "";
                            job.quantity = 1;
                            CartItem cartItem = CartItem.cartItemForItem(item);
                            cartItem.job_numbers.add(job);
                            Model.getInstance().defaultJobNo = "";

                            Model.getInstance().getCart().items.add(cartItem);
                            Model.getInstance().persistCart();

                            EnterQuantityFragment fragment = new EnterQuantityFragment();
                            final Bundle bundle = new Bundle();
                            bundle.putSerializable("cart_item", cartItem);
                            fragment.setArguments(bundle);
                            add(getActivity().getFragmentManager()
                                    .beginTransaction(), fragment);
                        }
                    });
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @SuppressLint("CommitTransaction")
            public void onClick(DialogInterface dialog, int id) {

                String jNo = jobNumber.getText().toString();
                if (jNo.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getActivity());
                    builder.setTitle("Job number required");
                    builder.setMessage("The job number cannot be blank.");
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                    return;
                }
                Model.getInstance().defaultJobNo = jNo;
                Job job = new Job();
                job.number = jNo;
                job.quantity = 1;
                CartItem cartItem = CartItem.cartItemForItem(item);
                cartItem.job_numbers.add(job);

                Model.getInstance().getCart().items.add(cartItem);
                Model.getInstance().persistCart();

                EnterQuantityFragment fragment = new EnterQuantityFragment();
                final Bundle bundle = new Bundle();
                bundle.putSerializable("cart_item", cartItem);
                fragment.setArguments(bundle);
                add(getActivity().getFragmentManager().beginTransaction(),
                        fragment);
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    OnClickListener favoriteListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            FavoriteHandler handler = new FavoriteHandler(
                    ItemDetailFragment.this, item);
            handler.request();
        }
    };

    OnClickListener pickupListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            showChangeDialog();
        }
    };

    private void showPickupJobDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final android.widget.EditText jobNumber = new android.widget.EditText(
                getActivity());
        builder.setView(jobNumber);

        // 2. Chain together various setter methods to set the dialog
        // characteristics
        builder.setMessage("Please enter the main job number:").setTitle("");

        String settingsString = Settings.getJobFlag(getActivity());
        if (settingsString != null && settingsString.equals("N")) {
            builder.setNegativeButton("Skip",
                    new DialogInterface.OnClickListener() {
                        @SuppressLint("CommitTransaction")
                        public void onClick(DialogInterface dialog, int id) {
                            pickupJobNo = "";
                            showPODialog();
                        }
                    });
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @SuppressLint("CommitTransaction")
            public void onClick(DialogInterface dialog, int id) {

                String jNo = jobNumber.getText().toString();
                if (jNo.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getActivity());
                    builder.setTitle("Job number required");
                    builder.setMessage("The job number cannot be blank.");
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                    return;
                }
                pickupJobNo = jNo;
                showPODialog();
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showChangeDialog() {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String locName = location == null ? Settings
                .getLocation(getActivity()).name : location.name;
        SSApplication.location = location;
        // 2. Chain together various setter methods to set the dialog
        // characteristics
        builder.setMessage(
                "Your pickup location is: " + locName.toUpperCase(Locale.US))
                .setTitle("");
        builder.setPositiveButton("click to continue",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SSApplication.pickUp = true;
                        PickUpItemDetailFragment fragment = new PickUpItemDetailFragment();
                        final Bundle bundle = new Bundle();
                        bundle.putSerializable("item", item);
                        fragment.setArguments(bundle);
                        add(getActivity().getFragmentManager()
                                .beginTransaction(), fragment);
                        // showPickupJobDialog();
                    }
                });

        builder.setNegativeButton("click to change",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Model.getInstance().getLocationsCache().items == null) {
                            // Load the locations cache
                            Model.getInstance().getLocationsCache()
                                    .addObserver(ItemDetailFragment.this);
                            Model.getInstance().getLocationsCache().request();

                            progress = new ProgressDialog(getActivity());
                            progress.setMessage("fetching locations...");
                            progress.show();

                        } else {
                            showPickUpNowDialog();
                        }
                    }
                });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPickUpNowDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] items = new String[Model.getInstance()
                .getLocationsCache().items.size()];

        int i = 0;
        for (IdNameObj obj : Model.getInstance().getLocationsCache().items) {
            items[i] = obj.name;
            i++;
        }

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                location = Model.getInstance().getLocationsCache().items
                        .get(arg1);
                showChangeDialog();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @SuppressLint("CommitTransaction")
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPODialog() {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final android.widget.EditText jobNumber = new android.widget.EditText(
                getActivity());
        builder.setView(jobNumber);

        // 2. Chain together various setter methods to set the dialog
        // characteristics
        builder.setMessage("Please enter the po number:").setTitle("");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @SuppressLint("CommitTransaction")
            public void onClick(DialogInterface dialog, int id) {
                String jNo = jobNumber.getText().toString();
                CartItem cartItem = CartItem.cartItemForItem(item);
                Job job = new Job();
                job.number = pickupJobNo;
                job.quantity = 1;
                Model.getInstance().defaultJobNo = "";
                cartItem.job_numbers = new ArrayList<Job>();
                cartItem.job_numbers.add(job);

                Cart cart = new Cart();
                cart.deliveryMethod = getString(R.string.pick_up_now);
                cart.location = location == null ? Settings
                        .getLocation(getActivity()) : location;
                cart.truckId = "" + Settings.getTruck(getActivity()).id;
                cart.PO = jNo;
                cart.items.add(cartItem);

                SubmitOrderHandler handler = new SubmitOrderHandler(
                        (SSActivity) getActivity());
                handler.cart = cart;
                handler.request();
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog1 = builder.create();
        dialog1.show();
    }

    OnClickListener addToCartListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (SSApplication.pickUp) {
                PickUpItemDetailFragment fragment = new PickUpItemDetailFragment();
                final Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                fragment.setArguments(bundle);
                add(getActivity().getFragmentManager().beginTransaction(),
                        fragment);
                return;
            }
            if (Model.getInstance().defaultJobNo == null) {
                showDialog();
            } else {

                Job job = new Job();
                job.number = Model.getInstance().defaultJobNo;
                job.quantity = 1;
                CartItem cartItem = CartItem
                        .cartItemForItem(ItemDetailFragment.this.item);
                cartItem.job_numbers.add(job);

                Model.getInstance().getCart().items.add(cartItem);
                Model.getInstance().persistCart();

                EnterQuantityFragment fragment = new EnterQuantityFragment();
                final Bundle bundle = new Bundle();
                bundle.putSerializable("cart_item", cartItem);
                fragment.setArguments(bundle);
                add(getActivity().getFragmentManager().beginTransaction(),
                        fragment);

            }
        }
    };

	/*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// We came from the scanning activity; the return intent contains a
		// RESULT_EXTRA key
		// whose value is an ArrayList of BarcodeResult objects that we found
		// while scanning.
		if (resultCode == Activity.RESULT_OK) {
			ArrayList<BarcodeResult> barcodes = data
					.getParcelableArrayListExtra(BarcodeScanActivity.RESULT_EXTRA);
			if (barcodes != null && barcodes.size() > 0) {
				barcode = barcodes.get(0).barcodeString;
				Toast.makeText(getActivity(),
						"Barcode Type = " + barcodes.get(0).getBarcodeType(),
						Toast.LENGTH_LONG).show();
				ScanHandler handler = new ScanHandler(ItemDetailFragment.this,
						barcode);
				handler.request();
			} else {
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				builder.setMessage("Sorry the item cannot be found!")
						.setNeutralButton("OK", null);

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // We came from the scanning activity; the return intent contains a RESULT_EXTRA key
        // whose value is an ArrayList of BarcodeResult objects that we found while scanning.
        if (resultCode == Activity.RESULT_OK) {
            Log.v("scan ", "scan entered reslut ok");
//			Toast.makeText(getActivity(),"OnActviityResult", Toast.LENGTH_SHORT).show();
            String scanvalue = data.getExtras().getString(AppConstants.KEY_SCAN_ID).replace(" ", "%20");
            //String scanvalue = data.getExtras().getString(AppConstants.KEY_SCAN_ID);

            if (!scanvalue.equals("")) {
                imgItem.setImageBitmap(null);
                Log.v("scan ", "scan value" + scanvalue);
                if (CommonUtils.isNetworkAvailable(getActivity())) {
                    ScanHandler handler = new ScanHandler(ItemDetailFragment.this, scanvalue);
                    handler.request();
                } else {
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage("Please Check your Network Connectivity.").setNeutralButton("OK", null);

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            } else {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("Sorry the item cannot be found!").setNeutralButton("OK", null);

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void receivedBarcode(String barcode) {
        this.btnAddToCart.setEnabled(true);
        this.btnPickup.setEnabled(true);
        this.btnFavorite.setEnabled(true);
        ArrayList<Item> itemms = Model.getInstance()
                .getItemDetailCache(barcode).items;
        item = itemms.get(0);
        updateItem();
    }

    @Override
    public void onTabReselected() {
        txtItemName.setText("Loading...");
        txtItemDetail.setText("");
        txtItemNotes.setText("");
        item = null;

        for (Iterator<BaseFragment> iterator = this.tabListener.getFragments()
                .iterator(); iterator.hasNext(); ) {
            BaseFragment frg = (BaseFragment) iterator.next();
            if (frg.getClass() != this.getClass()) {
                getFragmentManager().popBackStackImmediate();
            }
        }

	/*	Intent scanIntent = new Intent(HomeActivity.getActivity(),
                RLScanActivity.class);
		scanIntent.putExtra(RLScanActivity.INTENT_MULTI_SCAN, false);
		startActivityForResult(scanIntent, 1);*/
        Intent scanIntent = new Intent(HomeActivity.getActivity(), CScanActivity.class);
        scanIntent.putExtra(CScanActivity.INTENT_MULTI_SCAN, "scan");
        startActivityForResult(scanIntent, 1);

    }

    @Override
    public void update(Observable observable, Object data) {
        // To dismiss the dialog
        progress.dismiss();
        if (Model.getInstance().getLocationsCache().items != null) {

            showPickUpNowDialog();
        }
    }

    public void barcodeNotFound() {
        this.txtItemName.setText("Item not found.");
        this.txtItemDetail.setText("");
        this.btnAddToCart.setEnabled(false);
        this.btnPickup.setEnabled(false);
        this.btnFavorite.setEnabled(false);
    }
}
