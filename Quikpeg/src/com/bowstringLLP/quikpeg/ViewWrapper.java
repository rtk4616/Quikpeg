package com.bowstringLLP.quikpeg;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bowstringLLP.quikpeg.R;


public class ViewWrapper {
	View base;
	TextView name = null;
	TextView region = null;
	TextView distance = null;
	TextView openClose = null;
	ImageView verifyImage = null;
	
	ViewWrapper(View base) {
		this.base=base;
	}

	TextView getName() {
		if (name==null) {
			name=(TextView)base.findViewById(R.id.NameText);
		}

		return(name);
	}

	TextView getDistance() {
		if (distance==null) {
			distance=(TextView)base.findViewById(R.id.DistanceText);
		}

		return(distance);
	}

	TextView getOpenClose() {
		if (openClose==null) {
			openClose=(TextView)base.findViewById(R.id.OpenClose);
		}

		return(openClose);
	}
	
	ImageView getVerifyImage() {
		if (verifyImage==null) {
			verifyImage=(ImageView)base.findViewById(R.id.verifyImage);
		}

		return(verifyImage);
	}

	public TextView getRegion() {
		if (region==null) {
			region=(TextView)base.findViewById(R.id.RegionText);
		}
		
		return region;
	}
}
