package com.bowstringLLP.quikpeg;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewWrapper
{
  View base;
  TextView distance = null;
  TextView name = null;
  TextView openClose = null;
  TextView region = null;
  ImageView verifyImage = null;

  ViewWrapper(View paramView)
  {
    this.base = paramView;
  }

  TextView getDistance()
  {
    if (this.distance == null)
      this.distance = ((TextView)this.base.findViewById(2131099679));
    return this.distance;
  }

  TextView getName()
  {
    if (this.name == null)
      this.name = ((TextView)this.base.findViewById(2131099677));
    return this.name;
  }

  TextView getOpenClose()
  {
    if (this.openClose == null)
      this.openClose = ((TextView)this.base.findViewById(2131099680));
    return this.openClose;
  }

  public TextView getRegion()
  {
    if (this.region == null)
      this.region = ((TextView)this.base.findViewById(2131099678));
    return this.region;
  }

  ImageView getVerifyImage()
  {
    if (this.verifyImage == null)
      this.verifyImage = ((ImageView)this.base.findViewById(2131099676));
    return this.verifyImage;
  }
}

/* Location:           C:\apktool1.5.2\dex2jar-0.0.9.15\quikpeg_dex2jar.jar
 * Qualified Name:     com.bowstringLLP.quikpeg.ViewWrapper
 * JD-Core Version:    0.6.0
 */