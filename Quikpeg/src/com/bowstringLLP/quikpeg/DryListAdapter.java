package com.bowstringLLP.quikpeg;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DryListAdapter extends ArrayAdapter<DryDay> {

	Activity parentActivity;
	public DryListAdapter(Activity parentActivity) {
		super(parentActivity, R.layout.dry_list_layout);
		this.parentActivity=parentActivity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(getItem(position)==null)
			return null;

		if (convertView==null) {
			LayoutInflater inflater = parentActivity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.dry_list_layout, null);
		}
		
		//if(position%2==0)
			convertView.setBackgroundColor(Color.BLACK);
		//else
			//convertView.setBackgroundColor(Color.GRAY);
		
		if(getItem(position).holidayDate != null)
		{
			TextView date = (TextView) convertView.findViewById(R.id.holidayDate);
			date.setText(new SimpleDateFormat("dd MMM yy", Locale.ENGLISH).format(getItem(position).holidayDate));
		}
		
		if(getItem(position).holidayName != null)
		{
			TextView name = (TextView) convertView.findViewById(R.id.holidayName);
			name.setText(getItem(position).holidayName);
		}
		
		return convertView;
	}
}
