package com.bowstringLLP.quikpeg;

import java.util.Date;

public class DryDay
  implements Comparable<DryDay>
{
  Date holidayDate;
  String holidayName;

  public int compareTo(DryDay paramDryDay)
  {
    return new Date().compareTo(paramDryDay.holidayDate);
  }
}