package com.bowstringLLP.quikpeg;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DryDay
  implements Comparable<DryDay>
{
  Calendar holidayDate;
  String holidayName;

  public int compareTo(DryDay paramDryDay)
  {
    return new GregorianCalendar().compareTo(paramDryDay.holidayDate);
  }
}

/* Location:           C:\apktool1.5.2\dex2jar-0.0.9.15\quikpeg_dex2jar.jar
 * Qualified Name:     com.bowstringLLP.quikpeg.DryDay
 * JD-Core Version:    0.6.0
 */