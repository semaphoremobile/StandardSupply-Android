/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.semaphore.standardsupply.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class CScanActivity extends SSActivity {

    private static final String TAG = CScanActivity.class.getSimpleName();
    public static final String INTENT_MULTI_SCAN = "intent_multi_scan";
    private CompoundBarcodeView barcodeView;
    private String actionFrom = "";
    private String scanCustID = "";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;

    //private TextView custidText;

    Button doneButton;
    Button requestImageButton;
    Button toggleTorchButton;

    private boolean isLightOn = false;
    private int counterInt = 0;
    private AlphaAnimation fadeIn,fadeOut;

    List<ResultPoint> resultList;
    boolean checkScan = false;

    private List<String> custRefIDList;

    Context mContext;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            Log.v("scan ", "scan entered scan actvity call back");
            if (result.getText() != null) {
                if (result.getText() != null) {

                    String qrcode = result.getText();
                    barcodeView.setStatusText(qrcode);
                    //scanCustID = qrcode;
                    scanCustID = qrcode;

                      passResultData();
                            finish();

                    Log.d("OUTPUT", qrcode);
                   // result=qrcode;
                }else{

                }
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            resultList = resultPoints;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Fabric.with(this, new Crashlytics());
        Log.v("scan ", "scan entered scan actvity");

        mContext = this;

        doneButton = (Button) findViewById(R.id.button_done);
        requestImageButton = (Button) findViewById(R.id.button_request_image);
        toggleTorchButton = (Button) findViewById(R.id.button_toggle_torch);

      /*  doneButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                doneScanning();
            }
        });

        requestImageButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                requestImageData();
            }
        });*/

        toggleTorchButton.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                if (isLightOn) {

                    turnOffFlashLight();
                    isLightOn = false;

                } else {
                    turnOnFlashLight();
                    isLightOn = true;

                }
            }
        });


        try {
            Intent intent = getIntent();
            actionFrom = intent.getStringExtra(AppConstants.KEY_SCAN_ID);
//            custRefIDList = intent.getStringArrayListExtra(AppConstants.KEY_SCAN_LIST);

            if (custRefIDList != null)
                Log.v(TAG, "ramya from intetnt: " + custRefIDList.size());
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }


        setTitle("Scan your QR code");

      /*  edCustidText = (EditText)findViewById(R.id.custidText);
        btnContinue = (Button)findViewById(R.id.btnContinue);
*/
        custRefIDList = new ArrayList<String>();


        barcodeView = (CompoundBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.setStatusText("Waiting for QR code");
        // initBatCodeView();
        initBarcode();

        fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
        //custidText.startAnimation(fadeOut);
        fadeIn.setDuration(1200);
        fadeIn.setFillAfter(true);
        fadeOut.setDuration(1200);
        fadeOut.setFillAfter(true);
        fadeOut.setStartOffset(4200 + fadeIn.getStartOffset());
    }

    private void initBatCodeView() {
        barcodeView.decodeContinuous(callback);
    }

    private void initBarcode(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
            // Permission has already been granted
            initBatCodeView();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, so initialize the bar code
                    initBatCodeView();
                } else {
                    // Permission was denied!!
                }
                return;
            }
        }
    }


    Camera cam = null;
    public void turnOnFlashLight() {
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cam = Camera.open();
                Camera.Parameters p = cam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception throws in turning on flashlight.", Toast.LENGTH_SHORT).show();
        }
    }

    public void turnOffFlashLight() {
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cam.stopPreview();
                cam.release();
                cam = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception throws in turning off flashlight.", Toast.LENGTH_SHORT).show();
        }
    }

    public void passResultData(){
        Intent i = getIntent();
        i.putExtra(AppConstants.KEY_SCAN_ID, scanCustID);
        Log.v("scan ", "scan entered scan actvity scanCustID"+scanCustID);
        setResult(RESULT_OK, i);
        finish();
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
