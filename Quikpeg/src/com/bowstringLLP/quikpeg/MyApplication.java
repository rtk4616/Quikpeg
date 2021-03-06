package com.bowstringLLP.quikpeg;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import android.app.Application;
import android.graphics.Bitmap;

import com.testflightapp.lib.TestFlight;

@ReportsCrashes(formKey = "", formUri = "https://bowstringstudio.cloudant.com/acra-quikpeg/_design/acra-storage/_update/report", formUriBasicAuthLogin = "verediseendstedyselyctse", formUriBasicAuthPassword = "HbVLg3lkj1JrwaXnRjKpGw74", httpMethod = HttpSender.Method.PUT, reportType = HttpSender.Type.JSON)
public class MyApplication extends Application {
	
	private Bitmap portBitmap, landBitmap;
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
        TestFlight.takeOff(this, "ab941133-f4da-4b56-9f47-d84a61ad7348");
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