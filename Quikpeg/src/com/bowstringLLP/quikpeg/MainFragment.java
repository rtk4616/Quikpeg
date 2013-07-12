package com.bowstringLLP.quikpeg;

import java.util.List;

import org.apache.http.protocol.HTTP;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bowstringLLP.quikpeg.MainActivity.RecordsUpdateListener;

public class MainFragment extends Fragment implements RecordsUpdateListener {

	ProgressBar progressBar = null;
	TextView emptyTextView = null;
	ListView listView = null;
	ListItemClickListener mListener;
	CustomListAdapter adapter;
	Long timeElapsed = (long) 0;
	
	public interface ListItemClickListener
	{
		public void onListItemClick(int position);
		public void fetchRecords(boolean shouldRefresh);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		adapter = new CustomListAdapter(getActivity());
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (ListItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ListItemClickListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) {
			initializeActionBar();
		}
		
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@TargetApi(11)
	private void initializeActionBar() {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getListView().setEmptyView(getEmptyTextView());
		mListener.fetchRecords(false);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mListener.onListItemClick(arg2);
			}
		});
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main, menu);
		
		Intent prefsIntent = new Intent(getActivity(), SettingsActivity.class);
		MenuItem preferences = menu.findItem(R.id.action_settings);
		preferences.setIntent(prefsIntent);

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		// The intent does not have a URI, so declare the "text/plain" MIME type
		emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, getResources().getStringArray(R.array.suggestionRecipients)); // recipients
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
		MenuItem suggest = menu.findItem(R.id.Suggestion);
		suggest.setIntent(emailIntent);

		Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
		MenuItem about = menu.findItem(R.id.About);
		about.setIntent(aboutIntent);
		
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.Refresh:
			mListener.fetchRecords(true);
			break;
		case android.R.id.home:
			getActivity().onBackPressed();
			break;
		default:
			startActivity(item.getIntent());
		}
		return true;
	}

	private ListView getListView()
	{
		//if(listView == null)
			listView = (ListView) getActivity().findViewById(R.id.list);

		return listView;
	}

	@Override
	public void onRecordsUpdated(List<Records> records) {
		adapter.setContent(records);
		getListView().setAdapter(adapter);

		String str = getActivity().getSharedPreferences("com.bowstringLLP.quikpeg_preferences", Context.MODE_PRIVATE).getString("Mode", "NORMAL");
		switch(MainActivity.Mode.valueOf(str))
		{
		case	NORMAL:
		case	DAYBEFOREDRY:
			getActivity().setTitle(getString(R.string.mainTitle));
			getActivity().setTitleColor(Color.parseColor("#ffffff"));
			break;
			
		case	DRY:
			getActivity().setTitle("DRY DAY");
			getActivity().setTitleColor(Color.parseColor("#D89020"));
			break;
			
		case	LASTGOODSEARCH:
			getActivity().setTitle("LAST GOOD SEARCH");
			getActivity().setTitleColor(Color.parseColor("#D89020"));
		}
		
		if(MainActivity.dialog != null)
			MainActivity.dialog.dismiss();
	}
	
	public TextView getEmptyTextView()
	{
		//if(emptyTextView == null)
			emptyTextView = (TextView) getActivity().findViewById(R.id.emptyView);
		
		return emptyTextView;
	}
}
