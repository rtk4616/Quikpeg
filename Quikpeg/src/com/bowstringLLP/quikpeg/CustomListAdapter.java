/**
 * 
 */
package com.bowstringLLP.quikpeg;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * @author rishi
 *
 */
public class CustomListAdapter extends ArrayAdapter<Records>{

	Activity context;

	public CustomListAdapter(Activity context) {
		super(context, R.layout.list_view_layout);
		this.context=context;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		try {
			if (getItem(position) == null)
				return null;

			ViewWrapper wrapper=null;

			if (row==null) {
				LayoutInflater inflater=context.getLayoutInflater();

				row = inflater.inflate(R.layout.list_view_layout, null);
				wrapper=new ViewWrapper(row);
				row.setTag(wrapper);
			}
			else {
				wrapper=(ViewWrapper)row.getTag();
			}

			if(getItem(position).name !=null)
			{
				wrapper.getName().setText(getItem(position).name);
				wrapper.getRegion().setText(getItem(position).region);
			}
			else
				wrapper.getName().setText("Unknown");

			if(getItem(position).distance != null)
			{
				wrapper.getDistance().setEnabled(true);
				if(getItem(position).distance<1)
				{
					float f = (float) (getItem(position).distance * 1000);
					wrapper.getDistance().setText(String.format("%.2f", f) + " m");
				}
				else
					wrapper.getDistance().setText(String.format("%.2f", getItem(position).distance) + " km");
			}
			else
				wrapper.getDistance().setText("Unknown");

			if(getItem(position).isVerified)
				wrapper.getVerifyImage().setVisibility(View.VISIBLE);
			else
				wrapper.getVerifyImage().setVisibility(View.GONE);

			try
			{
				if(getItem(position).timeTillOpenClose()>30  && MainActivity.settings.getString("Mode", "NORMAL") != "DRY")
				{
					wrapper.getOpenClose().setText("Open");
					wrapper.getOpenClose().setTextColor(Color.parseColor("#88B800"));
				}
				else if(getItem(position).timeTillOpenClose()>0  && MainActivity.settings.getString("Mode", "NORMAL") != "DRY")
				{
					wrapper.getOpenClose().setText("Closing Soon");
					wrapper.getOpenClose().setTextColor(Color.parseColor("#88B800"));
				}
				else if(getItem(position).timeTillOpenClose()>-100  && MainActivity.settings.getString("Mode", "NORMAL") != "DRY")
				{
					wrapper.getOpenClose().setText("Opening Soon");
					wrapper.getOpenClose().setTextColor(Color.parseColor("#B00000"));
				}
				else
				{
					wrapper.getOpenClose().setText("Closed");
					wrapper.getOpenClose().setTextColor(Color.parseColor("#B00000"));
				}
			}
			catch(Exception e)
			{
				wrapper.getOpenClose().setEnabled(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return(row);
	}
}
