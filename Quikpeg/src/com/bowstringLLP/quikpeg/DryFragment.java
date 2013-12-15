package com.bowstringLLP.quikpeg;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class DryFragment extends Fragment {
	private DryListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new DryListAdapter(getActivity());
	}

	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
		return paramLayoutInflater.inflate(R.layout.dry_layout,
				paramViewGroup, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Spinner spinner = (Spinner) getActivity().findViewById(
				R.id.state_spinner);
		ListView dryList = (ListView) getActivity().findViewById(R.id.rate_list);
		dryList.setAdapter(adapter);
		
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.StateNames, R.layout.spinner_layout);
		spinner.setAdapter(spinnerAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				CharSequence ch = (CharSequence) parent.getItemAtPosition(position);
				
				adapter.clear();
					List<DryDay> dryList = MainActivity.builder.getDryDaysForState(ch.toString());
					
					for(int i=0; i<dryList.size(); i++)
						adapter.add(dryList.get(i));

				adapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
