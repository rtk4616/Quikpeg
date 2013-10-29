package com.bowstringLLP.quikpeg;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import android.app.Application;
import android.graphics.Bitmap;

@ReportsCrashes(formKey = "", formUri = "https://bowstringstudio.cloudant.com/acra-quikpeg/_design/acra-storage/_update/report", formUriBasicAuthLogin = "verediseendstedyselyctse", formUriBasicAuthPassword = "HbVLg3lkj1JrwaXnRjKpGw74", httpMethod = HttpSender.Method.PUT, reportType = HttpSender.Type.JSON)
public class MyApplication extends Application {
	
	private Bitmap portBitmap, landBitmap;
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
	}
	
	public Bitmap getPortBitmap()
	{
		return portBitmap;
	}
	
	public void setPortBitmap(Bitmap bitmap)
	{
		portBitmap = bitmap;
	}
	
	public Bitmap getLandBitmap()
	{
		return landBitmap;
	}
	
	public void setLandBitmap(Bitmap bitmap)
	{
		landBitmap = bitmap;
	}
}