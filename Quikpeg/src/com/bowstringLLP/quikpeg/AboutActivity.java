package com.bowstringLLP.quikpeg;

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
import android.view.View;
import android.widget.FrameLayout;

import com.google.analytics.tracking.android.EasyTracker;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		setScreenBackground();
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	public void onButtonClick(View view) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.twitter.com/Quikpeg"));
		startActivity(browserIntent);
	}

	private void setScreenBackground() {
		new AsyncTask<Void, Void, Bitmap>() {
			Bitmap bitmap;

			@Override
			protected Bitmap doInBackground(Void... params) {
				Display localDisplay = getWindowManager().getDefaultDisplay();
				Point localPoint = new Point();

				if (Build.VERSION.SDK_INT < 13) {
					localPoint.x = localDisplay.getWidth();
					localPoint.y = localDisplay.getHeight();
				} else
					localDisplay.getSize(localPoint);

				bitmap = BitmapModifier.decodeSampledBitmapFromResource(
						getResources(), R.drawable.aboutpagebackground,
						localPoint.x, localPoint.y);

				FrameLayout localFrameLayout = (FrameLayout) findViewById(R.id.aboutLayout);

				if (Build.VERSION.SDK_INT >= 16)
					localFrameLayout.setBackground(new BitmapDrawable(
							getResources(), bitmap));
				else
					localFrameLayout.setBackgroundDrawable(new BitmapDrawable(
							getResources(), bitmap));
				return null;
			}
		}.execute();
	}
}
