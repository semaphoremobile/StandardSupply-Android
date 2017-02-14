package com.semaphore.standardsupply.activity;

/*
public class RLScanActivity extends BarcodeScanActivity
{	
	public static final String INTENT_MULTI_SCAN = "intent_multi_scan";
		
	int mCurrentApiVersion = android.os.Build.VERSION.SDK_INT;    

	// The viewfinderView is the rectangle in the center showing the user where to
	// place the barcode. FoundText shows how many barcodes have been found. hintText
	// is used to guide the user through multi-step decode processes.
	View viewfinderView = null;
	Button doneButton;
	Button requestImageButton;
	Button toggleTorchButton;
	TextView hintTextView;
	TextView foundTextView;
		
	private boolean isMultiScan = false;
	private boolean isInRange = false;
	
	Display mDisplay;
	String mRequestedOrientation;
	
	SharedPreferences prefs;

	*/
/**
 * Called when the activity is first created.  Because the camera gets initialized in super.onResume(), we can't find out
 * whether the torch is available until after super.onResume() completes.
 * So, we enable or disable the 'Light' button here instead of in onCreate().
 * Camera error callback. Implement this method to handle any camera
 * errors thrown by Android OS.
 * Called by the SDK repeatedly while scanning is happening. This method
 * override is how your app can find out about the status of the scanning
 * session in real time.
 *//*

    @Override
    public void onCreate(Bundle icicle) 
    {    	    	
    	prefs = this.getSharedPreferences("scannerOptionsPrefs", MODE_PRIVATE);
    	mRequestedOrientation = BarcodeScanActivity.PREF_ORIENTATION_PORTRAIT;
    	
    	super.onCreate(icicle);
		Fabric.with(this, new Crashlytics());
    	 // Here we can set what types of barcode to scan
//        enabledTypes.setEan13(true);
//        enabledTypes.setEan8(false);
//        enabledTypes.setEan5(false);
//        enabledTypes.setEan2(false);
//        enabledTypes.setUpce(false);
//        enabledTypes.setCode128(false);
//        enabledTypes.setCode39(false);
//        enabledTypes.setCode93(false);
//        enabledTypes.setITF(false);
//        enabledTypes.setCodabar(false); 
//        enabledTypes.setGS1Databar(false); 
//        enabledTypes.setGS1DatabarExpanded(false); 
//        enabledTypes.setQRCode(false);
//        enabledTypes.setDataMatrix(false);
//        enabledTypes.setPDF417(false);
        
    	enabledTypes.setEan13(true);
    	enabledTypes.setEan8(true);
    	enabledTypes.setEan5(true);
    	enabledTypes.setEan2(true);
    	enabledTypes.setUpce(true);
    	enabledTypes.setCode128(true);
    	enabledTypes.setCode39(true);
    	enabledTypes.setCode93(true);
    	enabledTypes.setITF(true);
    	enabledTypes.setCodabar(true); 
    	enabledTypes.setGS1Databar(true); 
    	enabledTypes.setGS1DatabarExpanded(true); 
    	enabledTypes.setQRCode(true);
    	enabledTypes.setDataMatrix(true);
    	enabledTypes.setPDF417(true);
        
        // To Remove the Title bar from RedLaserSDK Activity, add the NoTitleBar Theme in your Android Manifest.
        // Remove the Status Bar from this window.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        mDisplay = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        
        
    	setContentView(R.layout.activity_scan);
    	
        // Find and position the viewfinder view
        viewfinderView = findViewById(R.id.view_finder);
        if (mCurrentApiVersion >= 8)
        {
        	Point point = new Point();
        	mDisplay.getSize(point);
        	LayoutParams params = new LayoutParams((int)(point.x * .75f), 
        			(int)( point.y * .33f));
        	params.gravity = Gravity.CENTER;
        	viewfinderView.setLayoutParams(params);
        }
                
    	isMultiScan = false;
        if (this.getIntent().getBooleanExtra(INTENT_MULTI_SCAN, false) == true)
        {
        	isMultiScan = true;
        }
        
        doneButton = (Button) findViewById(R.id.button_done);
        doneButton.setOnClickListener(new OnClickListener() 
        {
        	public void onClick(View v) 
        	{
        		doneScanning();
			}
        });
        
        requestImageButton = (Button) findViewById(R.id.button_request_image);
        requestImageButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				requestImageData();
			}
        });
        
        toggleTorchButton = (Button) findViewById(R.id.button_toggle_torch);
        toggleTorchButton.setOnClickListener(new OnClickListener()
        {

			public void onClick(View v) 
			{
				boolean torch = getTorch();
				setTorch(!torch);
			}
        });
        
        hintTextView = (TextView) findViewById(R.id.hint_text);
        foundTextView = (TextView) findViewById(R.id.num_found_text);
    }
 
	*/
/** Because the camera gets initialized in super.onResume(), we can't find out
 *  whether the torch is available until after super.onResume() completes.
 *  So, we enable or disable the 'Light' button here instead of in onCreate().
 *//*

	protected void onResume()
	{
		super.onResume();
		
		toggleTorchButton.setEnabled(isTorchAvailable());
	}

    */
/** Camera error callback. Implement this method to handle any camera
 *  errors thrown by Android OS.
 *//*

	@Override
	protected void onError(int error) 
	{
		Log.d("RLSampleScannerActivity", "Received an error from the Camera.");	
	}
	
	*/
/** Called by the SDK repeatedly while scanning is happening. This method
 *  override is how your app can find out about the status of the scanning
 *  session in real time.
 *//*

	@SuppressWarnings("deprecation")
	@Override
	protected void onScanStatusUpdate(Map<String, Object> scanStatus) 
	{
		String foundText = "0 Barcodes Found";
		
		// Retrieve the set of found barcodes from the HashMap
	    @SuppressWarnings(value = "unchecked")
		Set<BarcodeResult> allResults = (Set<BarcodeResult>) scanStatus.get(Status.STATUS_FOUND_BARCODES);
		
		if (allResults != null && allResults.size() > 0)
		{
			// Show how many barcodes we've found
			foundText = allResults.size() + " Barcodes Found";

			if (!isMultiScan)
			{
				// If we're in single scan mode and we've found something, we're done.
				doneScanning();
			}
		}
		
		// change color of viewfinder box if in-range status changes
		Boolean inRangeStatus = (Boolean) scanStatus.get(Status.STATUS_IN_RANGE);
		if (inRangeStatus != null)
		{
			if (isInRange != inRangeStatus.booleanValue())
			{
				isInRange = inRangeStatus.booleanValue();
				if (isInRange)
				{
					viewfinderView.setBackgroundDrawable(getResources().getDrawable(R.drawable.viewfinder_green));
				} else
				{
					viewfinderView.setBackgroundDrawable(getResources().getDrawable(R.drawable.viewfinder_white));
				}
			}
		}
		
		foundTextView.setText(foundText);
		
		// Guidance. This is for recognizing long barcodes in parts by swiping the device across
		// the barcode. See the documentaiton for more details.
		Integer guidanceLevel = (Integer) scanStatus.get(Status.STATUS_GUIDANCE);
		if (guidanceLevel == null)
		{
			hintTextView.setText("");
		} else
		{
			if (guidanceLevel.intValue() == 1)
			{
				hintTextView.setText(getResources().getText(R.string.guidance_string));
			} else if (guidanceLevel.intValue() == 2)
			{
				BarcodeResult partialBarcode = (BarcodeResult) scanStatus.get(Status.STATUS_PARTIAL_BARCODE);
				String partialString = partialBarcode.barcodeString;
				hintTextView.setText(partialString);
			}
		} 
		
		// This status string will only appear in response to a call to requestImageData().
		if (scanStatus.get(Status.STATUS_CAMERA_SNAPSHOT) != null)
		{
			// imageData will always be a JPEG image passed in a byte array. Depending on 
			// device hardware and API level, the image may be a preview frame that has
			// been JPEG compressed, or it may be a JPEG taken from the Camera's picture-taking
			// apparatus.
			byte[] imageData = (byte[]) scanStatus.get(Status.STATUS_CAMERA_SNAPSHOT);
			
			// Write the image data out to a file. Use a command like:
			//     adb pull /mnt/sdcard/Android/data/com.ebay.rlsample/files/PreviewCapture.jpg
			// to look at what gets captured.
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED))
			{
				FileOutputStream output = null;
				try {
					// Since this is a demo, just keep overwriting this one file
					File imageDir = new File(Environment.getExternalStorageDirectory(), 
							"Android/data/com.ebay.rlsample/files");
					imageDir.mkdirs();
					File imageFile = new File(imageDir, "PreviewCapture.jpg");
					output = new FileOutputStream(imageFile, false);
					output.write(imageData, 0, imageData.length);
					output.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	*/

/** BarcodeScanActivity queries this method during onCreate() to determine which
 *  orientation it should use.
 *//*

	@Override
	public String getOrientationSetting() 
	{
		return PREF_ORIENTATION_PORTRAIT;
	}    
}*/

public class RLScanActivity {

}