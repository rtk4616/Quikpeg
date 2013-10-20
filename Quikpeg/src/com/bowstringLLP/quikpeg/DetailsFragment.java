package com.bowstringLLP.quikpeg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
	View detailsView;
	NetworkStatus netStatus;
	Records record;

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

		Display localDisplay = getActivity().getWindowManager()
				.getDefaultDisplay();
		Point localPoint = new Point();
		Bitmap localBitmap;
		FrameLayout localFrameLayout;

		if (Build.VERSION.SDK_INT < 13) {
			localPoint.x = localDisplay.getWidth();
			localPoint.y = localDisplay.getHeight();
		} else
			localDisplay.getSize(localPoint);

		localBitmap = Bitmap.createScaledBitmap(
				((BitmapDrawable) getResources().getDrawable(
						R.drawable.aboutpagebackground)).getBitmap(),
				localPoint.x, localPoint.y, false);

		localFrameLayout = (FrameLayout) getActivity().findViewById(
				R.id.detailsLayout);

		if (Build.VERSION.SDK_INT >= 16)
			localFrameLayout.setBackground(new BitmapDrawable(getResources(),
					localBitmap));
		else
			localFrameLayout.setBackgroundDrawable(new BitmapDrawable(
					getResources(), localBitmap));

		if (getArguments() != null)
			updateDetailView((Records) getArguments().getSerializable(
					"com.bowstringLLP.oneclickalcohol.STORE_RECORD"));
	}

	public void onCreateOptionsMenu(Menu paramMenu,
			MenuInflater paramMenuInflater) {
		paramMenuInflater.inflate(R.menu.details, paramMenu);
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
		}
	}
}