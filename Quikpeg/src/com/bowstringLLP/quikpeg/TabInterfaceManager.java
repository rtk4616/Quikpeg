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
import android.widget.TabWidget;

import com.bowstringLLP.quikpeg.MainActivity.RecordsUpdateListener;

public class TabInterfaceManager implements TabHost.OnTabChangeListener{

	private TabHost mTabHost;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    private TabInfo mLastTab = null;
    FragmentActivity activity;
    
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
 
        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
 
    }
    
    public TabInterfaceManager(FragmentActivity activity)
    {
    	this.activity = activity;
    	initialiseTabHost();
    }
 
    /**
     * Step 2: Setup TabHost
     */
    private void initialiseTabHost() {
        mTabHost = (TabHost)activity.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        addTab(mTabHost, mTabHost.newTabSpec("List").setIndicator("List"), ( tabInfo = new TabInfo("List", MainFragment.class, null)));
        mapTabInfo.put(tabInfo.tag, tabInfo);
        addTab(mTabHost, mTabHost.newTabSpec("Map").setIndicator("Map"), ( tabInfo = new TabInfo("Map", MapTabFragment.class, null)));
        mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //
        mTabHost.setOnTabChangedListener(this);
       // TabWidget tab = (TabWidget) activity.findViewById(android.R.id.tabs);
        mTabHost.setCurrentTabByTag("Map");
    }
 
    /**
     * @param activity
     * @param tabHost
     * @param tabSpec
     * @param clss
     * @param args
     */
    private void addTab(TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(new TabFactory(activity));
        String tag = tabSpec.getTag();
 
        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state.  If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.detach(tabInfo.fragment);
            ft.commit();
            activity.getSupportFragmentManager().executePendingTransactions();
        }
 
        tabHost.addTab(tabSpec);
    }
 
    /** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
        TabInfo newTab = (TabInfo) mapTabInfo.get(tag);
        if (mLastTab != newTab) {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(activity,
                            newTab.clss.getName(), newTab.args);
                    ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }

                MainActivity.recListener = (RecordsUpdateListener) newTab.fragment;
            }
            
            mLastTab = newTab;
            ft.commit();
            activity.getSupportFragmentManager().executePendingTransactions();
        }
    }
}
