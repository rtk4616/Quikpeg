package com.bowstringLLP.quikpeg;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import android.app.Application;

@ReportsCrashes(formKey = "", formUri = "https://bowstringstudio.cloudant.com/acra-quikpeg/_design/acra-storage/_update/report", formUriBasicAuthLogin = "verediseendstedyselyctse", formUriBasicAuthPassword = "HbVLg3lkj1JrwaXnRjKpGw74", httpMethod = HttpSender.Method.PUT, reportType = HttpSender.Type.JSON)
public class MyApplication extends Application {
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
	}
}

/*
 * Location: C:\apktool1.5.2\dex2jar-0.0.9.15\quikpeg_dex2jar.jar Qualified
 * Name: com.bowstringLLP.quikpeg.MyApplication JD-Core Version: 0.6.0
 */