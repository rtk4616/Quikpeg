package com.bowstringLLP.quikpeg;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DetailsFragment extends Fragment {
	View detailsView;
	NetworkStatus netStatus;
	Records record;
 MyApplication myApp;

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		this.netStatus = new NetworkStatus(getActivity());
		myApp = (MyApplication) getActivity().getApplication();
	}

	@TargetApi(11)
	private void initializeActionBar() {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		((ImageView) getActivity().findViewById(android.R.id.home)).setPadding(15,0,7,0);
	}
	
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);

		if (detailsView != null) {
			ViewGroup localViewGroup = (ViewGroup) detailsView.getParent();
			if (localViewGroup != null)
				localViewGroup.removeView(detailsView);
		}
		try {
			detailsView = paramLayoutInflater.inflate(
					R.layout.fragment_details, paramViewGroup, false);

			return detailsView;
		} catch (InflateException localInflateException) {
			localInflateException.printStackTrace();
			getActivity().getSupportFragmentManager().popBackStack();
			return null;
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		try
		{
			AdView adView = (AdView) getActivity().findViewById(R.id.adView);
		AdRequest request = new AdRequest.Builder()
	    .build();
		
		adView.loadAd(request);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void onActivityCreated(Bundle paramBundle) {
		super.onActivityCreated(paramBundle);

		if (MainActivity.dialog == null) {
			MainActivity.dialog = ProgressDialog.show(getActivity(), null,
					"Loading");
			MainActivity.dialog.setCancelable(true);
			MainActivity.dialog.setCanceledOnTouchOutside(false);
		} else if (MainActivity.dialog.isShowing() == false)
			MainActivity.dialog.show();
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			initializeActionBar();
		}
		
		if (getActivity().getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			if (myApp.getPortBitmap() == null)
				getScreenBackground();
			else
				setScreenBackground(myApp.getPortBitmap());
		else if (myApp.getLandBitmap() == null)
			getScreenBackground();
		else
			setScreenBackground(myApp.getLandBitmap());

		if (getArguments() != null)
			updateDetailView((Records) getArguments().getSerializable(
					"com.bowstringLLP.oneclickalcohol.STORE_RECORD"));
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			getActivity().getSupportFragmentManager().popBackStack();/*
							 * if(getSupportFragmentManager().getBackStackEntryCount
							 * ()>0) getSupportFragmentManager().popBackStack();
							 */
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void getScreenBackground() {
		new AsyncTask<Void, Void, Bitmap>() {
			@SuppressLint("NewApi")
			@Override
			protected Bitmap doInBackground(Void... params) {
				Display localDisplay = getActivity().getWindowManager()
						.getDefaultDisplay();
				Point localPoint = new Point();

				if (Build.VERSION.SDK_INT < 13) {
					localPoint.x = localDisplay.getWidth();
					localPoint.y = localDisplay.getHeight();
				} else
					localDisplay.getSize(localPoint);

				return BitmapModifier.decodeSampledBitmapFromResource(
						getResources(), R.drawable.aboutpagebackground,
						localPoint.x, localPoint.y);
			}

			protected void onPostExecute(Bitmap result) {
				try
				{
					if (getActivity().getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
					myApp.setPortBitmap(result);
				else
					myApp.setLandBitmap(result);
				setScreenBackground(result);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}.execute();
	}

	@SuppressLint("NewApi")
	private void setScreenBackground(Bitmap bitmap) {
		FrameLayout localFrameLayout = (FrameLayout) getActivity()
				.findViewById(R.id.detailsLayout);

		if (Build.VERSION.SDK_INT >= 16)
			localFrameLayout.setBackground(new BitmapDrawable(getResources(),
					bitmap));
		else
			localFrameLayout.setBackgroundDrawable(new BitmapDrawable(
					getResources(), bitmap));
	}
	
	public void updateDetailView(Records record) {
		try {
			TextView text = (TextView) getActivity()
					.findViewById(R.id.nameText);
			if (record.timeTillOpenClose() > 0
					&& getActivity().getSharedPreferences(
							"com.bowstringLLP.quikpeg_preferences",
							Context.MODE_PRIVATE).getString("Mode", "NORMAL") != "DRY")
				text.setBackgroundColor(Color.parseColor("#88B800"));
			else
				text.setBackgroundColor(Color.parseColor("#B00000"));

			text.setText(record.name);

			if (record.isVerified)
				text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.verifieddetail, 0);
			else
				text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

			if (MainActivity.isLocationUpdated && netStatus.isNetworkEnabled())
				getActivity().findViewById(R.id.mapButton).setVisibility(
						View.VISIBLE);
			else
				getActivity().findViewById(R.id.mapButton).setVisibility(
						View.GONE);

			text = (TextView) getActivity().findViewById(R.id.addressText);
			text.setText(record.address);

			text = (TextView) getActivity().findViewById(R.id.notesText);
			if (record.notes != null) {
				text.setText(record.notes);
				text.setVisibility(View.VISIBLE);
			} else
				text.setVisibility(View.GONE);

			if (record.mondayOpen != null || record.mondayClose != null) {
				text = (TextView) getActivity().findViewById(R.id.mondayTime);
				text.setText(record.mondayOpen + " - " + record.mondayClose);
			} else
				text.setText("Unknown");

			if (record.tuesdayOpen != null || record.tuesdayClose != null) {
				text = (TextView) getActivity().findViewById(R.id.tuesdayTime);
				text.setText(record.tuesdayOpen + " - " + record.tuesdayClose);
			} else
				text.setText("Unknown");

			if (record.wednesdayOpen != null || record.wednesdayClose != null) {
				text = (TextView) getActivity()
						.findViewById(R.id.wednesdayTime);
				text.setText(record.wednesdayOpen + " - "
						+ record.wednesdayClose);
			} else
				text.setText("Unknown");

			if (record.thursdayOpen != null || record.thursdayClose != null) {
				text = (TextView) getActivity().findViewById(R.id.thursdayTime);
				text.setText(record.thursdayOpen + " - " + record.thursdayClose);
			} else
				text.setText("Unknown");

			if (record.fridayOpen != null || record.fridayClose != null) {
				text = (TextView) getActivity().findViewById(R.id.fridayTime);
				text.setText(record.fridayOpen + " - " + record.fridayClose);
			} else
				text.setText("Unknown");

			if (record.saturdayOpen != null || record.saturdayClose != null) {
				text = (TextView) getActivity().findViewById(R.id.saturdayTime);
				text.setText(record.saturdayOpen + " - " + record.saturdayClose);
			} else
				text.setText("Unknown");

			if (record.sundayOpen != null || record.sundayClose != null) {
				text = (TextView) getActivity().findViewById(R.id.sundayTime);
				text.setText(record.sundayOpen + " - " + record.sundayClose);
			} else
				text.setText("Unknown");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (MainActivity.dialog != null)
				MainActivity.dialog.dismiss();
		}
	}
}