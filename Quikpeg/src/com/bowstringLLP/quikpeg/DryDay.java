package com.bowstringLLP.quikpeg;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DryDay implements Comparable<DryDay> {

	String holidayName;
	Calendar holidayDate;
	
	@Override
	public int compareTo(DryDay arg0) {
		Calendar currentDate = new GregorianCalendar();
			return currentDate.compareTo(arg0.holidayDate);
	}
}
