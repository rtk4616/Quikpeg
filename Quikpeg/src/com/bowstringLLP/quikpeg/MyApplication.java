package com.bowstringLLP.quikpeg;
import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(
		formKey = "",
        formUri = "https://bowstringstudio.cloudant.com/acra-quikpeg/_design/acra-storage/_update/report",
        reportType = org.acra.sender.HttpSender.Type.JSON,
        httpMethod = org.acra.sender.HttpSender.Method.PUT,
        formUriBasicAuthLogin="verediseendstedyselyctse",
        formUriBasicAuthPassword="HbVLg3lkj1JrwaXnRjKpGw74"
        // Your usual ACRA configuration
       // mode = ReportingInteractionMode.TOAST,
        //resToastText = R.string.crash_toast_text,
	  )
	  public class MyApplication extends Application {
	      @Override
	      public void onCreate() {
	          super.onCreate();

	          // The following line triggers the initialization of ACRA
	          ACRA.init(this);
	      }
	  }
