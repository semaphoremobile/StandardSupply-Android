package com.semaphore.standardsupply;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;


public class SendLog extends Activity
{
	private Throwable e;
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    e = (Throwable) getIntent().getExtras().getSerializable("exception");
    requestWindowFeature (Window.FEATURE_NO_TITLE); // make a dialog without a titlebar
    setFinishOnTouchOutside (false); // prevent users from dismissing the dialog by tapping outside
    setContentView (R.layout.send_log);
    sendLogFile();
  }
	
  private void sendLogFile ()
  {
    Intent intent = new Intent (Intent.ACTION_SEND);
    intent.setType ("plain/text");
    intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"renee@semaphoremobile.com"});
    intent.putExtra (Intent.EXTRA_SUBJECT, "SS Exception StackTrace");
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    
    Process process;
	try {
		process = Runtime.getRuntime().exec("logcat *:E");
		BufferedReader bufferedReader = new BufferedReader(
			    new InputStreamReader(process.getInputStream()));

			    StringBuilder log=new StringBuilder();
			    String line = "";
			    while ((line = bufferedReader.readLine()) != null) {
			      log.append(line);
			    }
			    e.printStackTrace(pw);
			    intent.putExtra (Intent.EXTRA_TEXT, sw.toString() + "\n\n" + log); // do this so some email clients don't complain about empty body.
			    startActivity (intent);
			    finish();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    
  }


}