package com.bowstringLLP.quikpeg;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.analytics.tracking.android.EasyTracker;

public class AboutActivity extends Activity {

	Bitmap bit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		if (Build.VERSION.SDK_INT >= 11)
			initializeActionBar();
		setScreenBackground();
	}

	@TargetApi(11)
	private void initializeActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		bit.recycle();
		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onButtonClick(View view) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.twitter.com/Quikpeg"));
		startActivity(browserIntent);
	}

	private void setScreenBackground() {
		new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Void... params) {
				Display localDisplay = getWindowManager().getDefaultDisplay();
				Point localPoint = new Point();

				if (Build.VERSION.SDK_INT < 13) {
					localPoint.x = localDisplay.getWidth();
					localPoint.y = localDisplay.getHeight();
				} else
					localDisplay.getSize(localPoint);

				return BitmapModifier.decodeSampledBitmapFromResource(
						getResources(), R.drawable.about,
						localPoint.x, localPoint.y);
			}
			protected void onPostExecute(Bitmap result) {
			FrameLayout localFrameLayout = (FrameLayout) findViewById(R.id.aboutLayout);

			bit = result;
			if (Build.VERSION.SDK_INT >= 16)
				localFrameLayout.setBackground(new BitmapDrawable(
						getResources(), result));
			else
				localFrameLayout.setBackgroundDrawable(new BitmapDrawable(
						getResources(), result));
			}
		}.execute();
	}
}
