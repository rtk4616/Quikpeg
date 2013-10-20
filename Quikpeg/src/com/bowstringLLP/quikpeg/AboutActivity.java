package com.bowstringLLP.quikpeg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.analytics.tracking.android.EasyTracker;

public class AboutActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
	public void onButtonClick(View view)
	{		
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/Quikpeg"));
		startActivity(browserIntent);
	}
}
