/**
 * 
 */
package com.bowstringLLP.quikpeg;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.bowstringLLP.quikpeg.R;

/**
 * @author rishi
 *
 */
public class CustomListAdapter extends ArrayAdapter<Records>{

	Activity context;
	List<Records> rec;

	public CustomListAdapter(Activity context) {
		super(context, R.layout.list_view_layout);
		this.context=context;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		try {
			if (rec == null)
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

			if(rec.get(position).name !=null)
			{
				wrapper.getName().setText(rec.get(position).name);
				wrapper.getRegion().setText(rec.get(position).region);
			}
			else
				wrapper.getName().setText("Unknown");

			if(rec.get(position).distance != null)
			{
				wrapper.getDistance().setEnabled(true);
				if(rec.get(position).distance<1)
				{
					int f = (int) (rec.get(position).distance * 1000);
					wrapper.getDistance().setText(f + " m");
				}
				else
					wrapper.getDistance().setText(rec.get(position).distance.toString() + " km");
			}
			else
				wrapper.getDistance().setText("Unknown");

			if(rec.get(position).isVerified)
				wrapper.getVerifyImage().setVisibility(View.VISIBLE);
			else
				wrapper.getVerifyImage().setVisibility(View.GONE);

			try
			{
				if(rec.get(position).timeTillOpenClose()>30  && MainActivity.settings.getString("Mode", "NORMAL") != "DRY")
				{
					wrapper.getOpenClose().setText("Open");
					wrapper.getOpenClose().setTextColor(Color.parseColor("#88B800"));
				}
				else if(rec.get(position).timeTillOpenClose()>0  && MainActivity.settings.getString("Mode", "NORMAL") != "DRY")
				{
					wrapper.getOpenClose().setText("Closing Soon");
					wrapper.getOpenClose().setTextColor(Color.parseColor("#88B800"));
				}
				else if(rec.get(position).timeTillOpenClose()>-100  && MainActivity.settings.getString("Mode", "NORMAL") != "DRY")
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

	public void setContent(List<Records> rec) {
		this.rec = rec;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rec == null ? 0 : rec.size();
	}

}
