package com.bowstringLLP.quikpeg;

import java.util.List;

import org.apache.http.protocol.HTTP;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bowstringLLP.quikpeg.MainActivity.RecordsUpdateListener;

public class MainFragment extends Fragment implements RecordsUpdateListener {
	static View mainView;
	CustomListAdapter adapter;
	TextView emptyTextView = null;
	ListView listView = null;
	ListItemClickListener mListener;
	ProgressBar progressBar = null;
	List<Records> records;
	Long timeElapsed = Long.valueOf(0L);

	private ListView getListView() {
		listView = ((ListView) getActivity().findViewById(R.id.list));
		return listView;
	}

	@TargetApi(11)
	private void initializeActionBar() {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
	}

	private void switchView() {
		MapTabFragment mapFragment = new MapTabFragment();
		
		FragmentTransaction localFragmentTransaction = getFragmentManager()
				.beginTransaction();
		localFragmentTransaction.replace(R.id.fragment_container, mapFragment);
		localFragmentTransaction.commit();
	}

	public TextView getEmptyTextView() {
		emptyTextView = ((TextView) getActivity().findViewById(R.id.emptyView));
		return emptyTextView;
	}

	public void onActivityCreated(Bundle paramBundle) {
		super.onActivityCreated(paramBundle);

		View localView = ((LayoutInflater) getActivity().getSystemService(
				"layout_inflater"))
				.inflate(R.layout.footer_layout, null, false);

		getListView().addFooterView(localView, null, false);
		
		if (MainActivity.dialog == null) {
			MainActivity.dialog = ProgressDialog.show(getActivity(), null, "Loading");
			MainActivity.dialog.setCancelable(true);
			MainActivity.dialog
					.setCanceledOnTouchOutside(false);
		} else if (MainActivity.dialog.isShowing() == false)
			MainActivity.dialog.show();
		
		getListView().setEmptyView(getEmptyTextView());

		MainActivity.recListener = this;

		getListView().setAdapter(adapter);
		mListener.fetchRecords(false);

		getListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> paramAdapterView,
							View paramView, int paramInt, long paramLong) {
						mListener.onListItemClick(paramInt);
					}
				});

		getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScroll(AbsListView paramAbsListView,
					int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount == totalItemCount)
						&& (totalItemCount > visibleItemCount)) {
					getActivity().findViewById(android.R.id.empty)
							.setVisibility(View.VISIBLE);
					onRecordsUpdated(records, totalItemCount + 20);
				}
			}

			public void onScrollStateChanged(AbsListView paramAbsListView,
					int paramInt) {
			}
		});
	}

	public void onAttach(Activity paramActivity) {
		super.onAttach(paramActivity);
		try {
			mListener = ((ListItemClickListener) paramActivity);
			return;
		} catch (ClassCastException localClassCastException) {
		}
		throw new ClassCastException(paramActivity.toString()
				+ " must implement ListItemClickListener");
	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		this.adapter = new CustomListAdapter(getActivity());
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main, menu);

		Intent prefsIntent = new Intent(getActivity(), SettingsActivity.class);
		MenuItem preferences = menu.findItem(R.id.action_settings);
		preferences.setIntent(prefsIntent);

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		// The intent does not have a URI, so declare the "text/plain" MIME type
		emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
		emailIntent.putExtra(Intent.EXTRA_EMAIL,
				getResources().getStringArray(R.array.suggestionRecipients)); // recipients
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
		MenuItem suggest = menu.findItem(R.id.Suggestion);
		suggest.setIntent(emailIntent);

		Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
		MenuItem about = menu.findItem(R.id.About);
		about.setIntent(aboutIntent);

		super.onCreateOptionsMenu(menu, inflater);
	}

	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);

		if (Build.VERSION.SDK_INT >= 11)
			initializeActionBar();

		if (mainView == null)
			mainView = paramLayoutInflater.inflate(R.layout.fragment_main,
					paramViewGroup, false);
		else {
			ViewGroup grp = (ViewGroup) mainView.getParent();
			grp.removeAllViews();
		}
		return mainView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Refresh:
			mListener.fetchRecords(true);
			break;
		case android.R.id.home:
			getActivity().onBackPressed();
			break;
		case R.id.switchView:
			switchView();
			break;
		default:
			startActivity(item.getIntent());
		}
		return true;
	}

	public void onRecordsUpdated(List<Records> records, int offset) {
		if (records != null)
			this.records = records;

		if (this.records != null) {
			for (int i = this.adapter.getCount(); i < offset && i<this.records.size(); i++)
				adapter.add(this.records.get(i));

			adapter.notifyDataSetChanged();

			String str = getActivity().getSharedPreferences(
					"com.bowstringLLP.quikpeg_preferences",
					Context.MODE_PRIVATE).getString("Mode", "NORMAL");
			switch (MainActivity.Mode.valueOf(str)) {
			case NORMAL:
			case DAYBEFOREDRY:
				getActivity().setTitle(getString(R.string.mainTitle));
				getActivity().setTitleColor(Color.parseColor("#ffffff"));
				break;

			case DRY:
				getActivity().setTitle("DRY DAY");
				getActivity().setTitleColor(Color.parseColor("#D89020"));
				break;

			case LASTGOODSEARCH:
				getActivity().setTitle("LAST GOOD SEARCH");
				getActivity().setTitleColor(Color.parseColor("#D89020"));
			}

			if (MainActivity.dialog != null)
				MainActivity.dialog.dismiss();
		}
	}

	public interface ListItemClickListener {
		public void fetchRecords(boolean paramBoolean);

		public void onListItemClick(int paramInt);
	}
}