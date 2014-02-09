package com.bowstringLLP.quikpeg;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

@TargetApi(11)
public final class TabListener<T extends Fragment> implements
		ActionBar.TabListener {
	private Fragment mFragment;
	private final FragmentActivity mActivity;
	private final String mTag;
	private final Class<T> mClass;

	/**
	 * Constructor used each time a new tab is created.
	 * 
	 * @param activity
	 *            The host Activity, used to instantiate the fragment
	 * @param tag
	 *            The identifier tag for the fragment
	 * @param clz
	 *            The fragment's Class, used to instantiate the fragment
	 */
	public TabListener(FragmentActivity activity, String tag, Class<T> clz) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
	}

	/* The following are each of the ActionBar.TabListener callbacks */

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Check if the fragment is already initialized
		android.support.v4.app.FragmentTransaction fft = mActivity
				.getSupportFragmentManager().beginTransaction();

		mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(
				mTag);
		if (mFragment == null) { // If not, instantiate and add it to the
									// activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			fft.add(android.R.id.content, mFragment, mTag);
		} else
			fft.show(mFragment);
		fft.commit();
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

		android.support.v4.app.FragmentTransaction fft = mActivity
				.getSupportFragmentManager().beginTransaction();

		for(int i=0; i<mActivity.getSupportFragmentManager().getBackStackEntryCount(); i++)
			mActivity.getSupportFragmentManager().popBackStackImmediate();
		
		fft.hide(mFragment);
		fft.commit();
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab. Usually do nothing.
	}
}
