package com.bowstringLLP.quikpeg;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {

	Context context;
	
	public NetworkStatus(Context context)
	{
	this.context = context;	
	}
	
	public boolean isNetworkEnabled()
	{
		try{
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileInfo = 
					connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (wifiInfo.isConnected() || mobileInfo.isConnected())
				return true;
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
}
