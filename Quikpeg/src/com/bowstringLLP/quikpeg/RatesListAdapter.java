package com.bowstringLLP.quikpeg;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class RatesListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private HashMap<String, List<Integer>> _listDataChild;

	public RatesListAdapter(Context context) {
		this._context = context;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return getGroup(groupPosition).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	    public View getChildView(int groupPosition, final int childPosition,
	            boolean isLastChild, View convertView, ViewGroup parent) {
	 
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this._context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.rates_list_layout, null);
	        }

	        if(_listDataChild == null)
	        	return null;
	        
	        String childText = getChild(groupPosition, 0).toString();
	        TextView txtListChild = (TextView) convertView
	                .findViewById(R.id.ml50rate);
	        txtListChild.setText(childText.matches("0") ? "-" : childText);

	        childText = getChild(groupPosition, 1).toString();
	        txtListChild = (TextView) convertView
	                .findViewById(R.id.ml200rate);
	        txtListChild.setText(childText.matches("0") ? "-" : childText);

	        childText = getChild(groupPosition, 2).toString(); 
	        txtListChild = (TextView) convertView
	                .findViewById(R.id.ml330rate);
	        txtListChild.setText(childText.matches("0") ? "-" : childText);

/*	        childText = getChild(groupPosition, 3).toString();
	        txtListChild = (TextView) convertView
	                .findViewById(R.id.ml375rate);
	        txtListChild.setText(childText.matches("0") ? "-" : childText);*/

	        childText = getChild(groupPosition, 4).toString();
	        txtListChild = (TextView) convertView
	                .findViewById(R.id.ml500rate);
	        txtListChild.setText(childText.matches("0") ? "-" : childText);

	        childText = getChild(groupPosition, 5).toString();
	        txtListChild = (TextView) convertView
	                .findViewById(R.id.ml650rate);
	        txtListChild.setText(childText.matches("0") ? "-" : childText);

	        childText = getChild(groupPosition, 6).toString();
	        txtListChild = (TextView) convertView
	                .findViewById(R.id.ml700rate);
	        txtListChild.setText(childText.matches("0") ? "-" : childText);

	        childText = getChild(groupPosition, 7).toString();
	        txtListChild = (TextView) convertView
	                .findViewById(R.id.ml750rate);
	        txtListChild.setText(childText.matches("0") ? "-" : childText);

	        childText = getChild(groupPosition, 8).toString();
	        txtListChild = (TextView) convertView
	                .findViewById(R.id.ml1000rate);
	        txtListChild.setText(childText.matches("0") ? "-" : childText);

	        /*childText = getChild(groupPosition, 9).toString();
	        txtListChild = (TextView) convertView
	                .findViewById(R.id.ml2000rate);
	        txtListChild.setText(childText=="0" ? "-" : childText);*/
	        
	        return convertView;
	    }

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public List<Integer> getGroup(int groupPosition) {
		return _listDataChild.get(getGroupTitle(groupPosition));
	}

	@Override
	public int getGroupCount() {
		if(_listDataChild!=null)
			return this._listDataChild.size();
		
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public String getGroupTitle(int groupPosition)
	{
		return (String) this._listDataChild.keySet().toArray()[groupPosition];
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroupTitle(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater
					.inflate(R.layout.rates_list_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public void setData(HashMap<String, List<Integer>> rateList) {
		_listDataChild = rateList;
	}
}
