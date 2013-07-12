package com.bowstringLLP.quikpeg;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bowstringLLP.quikpeg.R;

public class DetailsFragment extends Fragment {
	
	Records record;
	NetworkStatus netStatus;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		netStatus = new NetworkStatus(getActivity());
	}
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_details, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if(getArguments()!=null)
			updateDetailView((Records) getArguments().getSerializable(MainActivity.STORE_RECORD));
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main, menu);
	}
	
	public void updateDetailView(Records record)
	{
		try {
			TextView text = (TextView) getActivity().findViewById(R.id.nameText);
			if(record.timeTillOpenClose()>0  && getActivity().getSharedPreferences("com.bowstringLLP.quikpeg_preferences", Context.MODE_PRIVATE).getString("Mode", "NORMAL") != "DRY")
				text.setBackgroundColor(Color.parseColor("#88B800"));
			else
				text.setBackgroundColor(Color.parseColor("#B00000"));

			text.setText(record.name);

			if(record.isVerified)
				text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.verifieddetail, 0);
			else
				text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			
			if(MainActivity.isLocationUpdated && netStatus.isNetworkEnabled())
				getActivity().findViewById(R.id.mapButton).setVisibility(View.VISIBLE);
			else
				getActivity().findViewById(R.id.mapButton).setVisibility(View.GONE);

			text = (TextView) getActivity().findViewById(R.id.addressText);
			text.setText(record.address);

			text = (TextView) getActivity().findViewById(R.id.notesText);
			if(record.notes != null)
			{
				text.setText(record.notes);
				text.setVisibility(View.VISIBLE);
			}
			else
				text.setVisibility(View.GONE);

			if(record.mondayOpen != null || record.mondayClose != null)
			{
				text = (TextView) getActivity().findViewById(R.id.mondayTime);
				text.setText(record.mondayOpen +" - "+record.mondayClose);
			}
			else
				text.setText("Unknown");

			if(record.tuesdayOpen != null || record.tuesdayClose != null)
			{
				text = (TextView) getActivity().findViewById(R.id.tuesdayTime);
				text.setText(record.tuesdayOpen+" - "+record.tuesdayClose);
			}
			else
				text.setText("Unknown");

			if(record.wednesdayOpen != null || record.wednesdayClose != null)
			{
				text = (TextView) getActivity().findViewById(R.id.wednesdayTime);
				text.setText(record.wednesdayOpen +" - "+record.wednesdayClose);
			}
			else
				text.setText("Unknown");

			if(record.thursdayOpen != null || record.thursdayClose != null)
			{
				text = (TextView) getActivity().findViewById(R.id.thursdayTime);
				text.setText(record.thursdayOpen +" - "+record.thursdayClose);
			}
			else
				text.setText("Unknown");

			if(record.fridayOpen != null || record.fridayClose != null)
			{
				text = (TextView) getActivity().findViewById(R.id.fridayTime);
				text.setText(record.fridayOpen +" - "+record.fridayClose);
			}
			else
				text.setText("Unknown");

			if(record.saturdayOpen != null || record.saturdayClose != null)
			{
				text = (TextView) getActivity().findViewById(R.id.saturdayTime);
				text.setText(record.saturdayOpen +" - "+record.saturdayClose);
			}
			else
				text.setText("Unknown");

			if(record.sundayOpen != null || record.sundayClose != null)
			{
				text = (TextView) getActivity().findViewById(R.id.sundayTime);
				text.setText(record.sundayOpen +" - "+record.sundayClose);
			}
			else
				text.setText("Unknown");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
