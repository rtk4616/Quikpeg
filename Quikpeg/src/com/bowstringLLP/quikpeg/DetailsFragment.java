package com.bowstringLLP.quikpeg;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
	View detailsView;
	NetworkStatus netStatus;
	Records record;
	Bitmap portBitmap, landBitmap;

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setRetainInstance(true);
		this.netStatus = new NetworkStatus(getActivity());
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

	public void onActivityCreated(Bundle paramBundle) {
		super.onActivityCreated(paramBundle);
		
		if (MainActivity.dialog == null) {
			MainActivity.dialog = ProgressDialog.show(
					getActivity(), null, "Loading");
			MainActivity.dialog.setCancelable(true);
			MainActivity.dialog
					.setCanceledOnTouchOutside(false);
		} else if (MainActivity.dialog.isShowing() == false)
			MainActivity.dialog.show();
		
		if(getActivity().getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			if(portBitmap == null)
				getScreenBackground(portBitmap);
			else
				setScreenBackground(portBitmap);
		else
			if(landBitmap == null)
				getScreenBackground(landBitmap);
			else
				setScreenBackground(landBitmap);

		if (getArguments() != null)
			updateDetailView((Records) getArguments().getSerializable(
					"com.bowstringLLP.oneclickalcohol.STORE_RECORD"));
	}
		
	private void getScreenBackground(Bitmap bitmap)
	{
		new AsyncTask<Void, Void, Bitmap>()
				{
			Bitmap bitmap;
			@Override
			protected Bitmap doInBackground(Void... params) {
				Display localDisplay = getActivity().getWindowManager().getDefaultDisplay();
				Point localPoint = new Point();

				if (Build.VERSION.SDK_INT < 13) {
					localPoint.x = localDisplay.getWidth();
					localPoint.y = localDisplay.getHeight();
				} else
					localDisplay.getSize(localPoint);

				bitmap = BitmapModifier.decodeSampledBitmapFromResource(getResources(), R.drawable.aboutpagebackground, localPoint.x, localPoint.y);
				
				setScreenBackground(bitmap);
				
				return null;
			}
				}.execute();
	}
	
	private void setScreenBackground(Bitmap bitmap)
	{
		FrameLayout localFrameLayout = (FrameLayout) getActivity().findViewById(R.id.detailsLayout);
		
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
		}finally
		{
			if(MainActivity.dialog!=null)
				MainActivity.dialog.dismiss();
		}
	}
}