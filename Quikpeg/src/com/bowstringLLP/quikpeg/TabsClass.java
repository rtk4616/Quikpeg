package com.bowstringLLP.quikpeg;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

/**
 * @author mwho
 * 
 */
public class TabsClass implements TabHost.OnTabChangeListener{

	public TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private TabInfo mLastTab = null;
	private FragmentActivity parentActivity;

	private class TabInfo {
		private String tag;
		private Class clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}
	}

	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @return
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	public TabsClass(Bundle args, FragmentActivity parentActivity) {
		this.parentActivity = parentActivity;
		initialiseTabHost(args);
	}

	/**
	 * Step 2: Setup TabHost
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) parentActivity.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		addTab(this.mTabHost,
				this.mTabHost.newTabSpec(MainActivity.LIST_TAB).setIndicator(MainActivity.LIST_TAB),
				(tabInfo = new TabInfo(MainActivity.LIST_TAB, MainFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		addTab(this.mTabHost,
				this.mTabHost.newTabSpec(MainActivity.DRY_TAB).setIndicator(MainActivity.DRY_TAB),
				(tabInfo = new TabInfo(MainActivity.DRY_TAB, DryFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		addTab(this.mTabHost,
				this.mTabHost.newTabSpec(MainActivity.RATES_TAB).setIndicator(MainActivity.RATES_TAB),
				(tabInfo = new TabInfo(MainActivity.RATES_TAB, RatesFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		
		mTabHost.setOnTabChangedListener(this);

		if (args != null)
			onTabChanged(args.getString(MainActivity.SELECTED_TAB));
		else
			onTabChanged(MainActivity.LIST_TAB);
	}

	/**
	 * @param tabClass
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	public void addTab(TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(new TabFactory(parentActivity));
		String tag = tabSpec.getTag();

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		tabInfo.fragment = parentActivity.getSupportFragmentManager()
				.findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = parentActivity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
		//	parentActivity.getSupportFragmentManager()
		//			.executePendingTransactions();
		}
		tabHost.addTab(tabSpec);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	@Override
	public void onTabChanged(String tag) {
		TabInfo newTab = (TabInfo) mapTabInfo.get(tag);
		if (mLastTab != newTab) {
			FragmentTransaction ft = parentActivity.getSupportFragmentManager()
					.beginTransaction();
			if (mLastTab != null)
			{
				ft.hide(mLastTab.fragment);
				for(int i=0; i<parentActivity.getSupportFragmentManager().getBackStackEntryCount(); i++)
					parentActivity.getSupportFragmentManager().popBackStackImmediate();
			}
			
			if (newTab != null) {
				if (newTab.fragment == null)
				{	newTab.fragment = Fragment.instantiate(parentActivity,
							newTab.clss.getName(), newTab.args);

				ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
				}
				else
				ft.show(newTab.fragment);
			}
			mLastTab = newTab;
			ft.commit();
		}
	}
}
