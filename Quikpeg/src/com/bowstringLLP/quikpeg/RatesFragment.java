package com.bowstringLLP.quikpeg;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

public class RatesFragment extends Fragment {
	
	private RatesListAdapter adapter;
	private ArrayAdapter<CharSequence> spinnerAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new RatesListAdapter(getActivity());
		 setRetainInstance(true);
	}

	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
		return paramLayoutInflater.inflate(R.layout.rates_layout,
				paramViewGroup, false);
	}

	@Override
	public void onActivityCreated(Bundle paramBundle) {
		super.onActivityCreated(paramBundle);

		Spinner spinner = (Spinner) getActivity().findViewById(
				R.id.state_rate_spinner);
		ExpandableListView ratesList = (ExpandableListView) getActivity().findViewById(R.id.lvExp);
		ratesList.setAdapter(adapter);
		
		spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.RatesListStateNames, R.layout.spinner_layout);
		//spinnerAdapter.remove(spinnerAdapter.getItem(0));
		spinner.setAdapter(spinnerAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				CharSequence ch = (CharSequence) parent.getItemAtPosition(position);
				
					HashMap<String, List<Integer>> rateList = MainActivity.builder.getRatesForState(ch.toString());
					
					adapter.setData(rateList);

				adapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}
