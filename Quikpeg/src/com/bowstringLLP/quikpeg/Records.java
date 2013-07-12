package com.bowstringLLP.quikpeg;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Records implements Serializable, Comparable<Records>
{
	String name;
	String region;
	String address;
	String notes;
	Double latitude;
	Double longitude;
	Boolean isVerified;
	Integer mondayOpen;
	Integer mondayClose;
	Integer tuesdayOpen;
	Integer tuesdayClose;
	Integer wednesdayOpen;
	Integer wednesdayClose;
	Integer thursdayOpen;
	Integer thursdayClose;
	Integer fridayOpen;
	Integer fridayClose;
	Integer saturdayOpen;
	Integer saturdayClose;
	Integer sundayOpen;
	Integer sundayClose;
	Float distance;
	
	@Override
	public int compareTo(Records arg0) {
		try{
			if(distance == 0 || distance<arg0.distance)
				return -1;
		
			if(distance == 0 || distance>arg0.distance)
				return 1;
			
		return 0;
		}catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	public double timeTillOpenClose() throws NullPointerException
	{
		Calendar cal = new GregorianCalendar();
		Calendar Open = new GregorianCalendar();
		Calendar Close = new GregorianCalendar();
		
		switch(cal.get(Calendar.DAY_OF_WEEK))
		{
		case Calendar.MONDAY:
		{
			Close.set(Calendar.HOUR_OF_DAY, (mondayClose)/100);
			Close.set(Calendar.MINUTE, (mondayClose)%100);

			Open.set(Calendar.HOUR_OF_DAY, (mondayOpen)/100);
			Open.set(Calendar.MINUTE, (mondayOpen)%100);
			break;
		}
		case Calendar.TUESDAY:
		{
			Close.set(Calendar.HOUR_OF_DAY, (tuesdayClose)/100);
			Close.set(Calendar.MINUTE, (tuesdayClose)%100);

			Open.set(Calendar.HOUR_OF_DAY, (tuesdayOpen)/100);
			Open.set(Calendar.MINUTE, (tuesdayOpen)%100);
			break;
		}
		case Calendar.WEDNESDAY:
		{
			Close.set(Calendar.HOUR_OF_DAY, (wednesdayClose)/100);
			Close.set(Calendar.MINUTE, (wednesdayClose)%100);

			Open.set(Calendar.HOUR_OF_DAY, (wednesdayOpen)/100);
			Open.set(Calendar.MINUTE, (wednesdayOpen)%100);
			break;
		}
		case Calendar.THURSDAY:
		{
			Close.set(Calendar.HOUR_OF_DAY, (thursdayClose)/100);
			Close.set(Calendar.MINUTE, (thursdayClose)%100);

			Open.set(Calendar.HOUR_OF_DAY, (thursdayOpen)/100);
			Open.set(Calendar.MINUTE, (thursdayOpen)%100);
			break;
		}
		case Calendar.FRIDAY:
		{
			Close.set(Calendar.HOUR_OF_DAY, (fridayClose)/100);
			Close.set(Calendar.MINUTE, (fridayClose)%100);

			Open.set(Calendar.HOUR_OF_DAY, (fridayOpen)/100);
			Open.set(Calendar.MINUTE, (fridayOpen)%100);
			break;
		}
		case Calendar.SATURDAY:
		{
			Close.set(Calendar.HOUR_OF_DAY, (saturdayClose)/100);
			Close.set(Calendar.MINUTE, (saturdayClose)%100);

			Open.set(Calendar.HOUR_OF_DAY, (saturdayOpen)/100);
			Open.set(Calendar.MINUTE, (saturdayOpen)%100);
			break;
		}
		case Calendar.SUNDAY:
		{
			Close.set(Calendar.HOUR_OF_DAY, (sundayClose)/100);
			Close.set(Calendar.MINUTE, (sundayClose)%100);

			Open.set(Calendar.HOUR_OF_DAY, (sundayOpen)/100);
			Open.set(Calendar.MINUTE, (sundayOpen)%100);
			break;
		}
		}

		if(cal.before(Close)==true && cal.before(Open)==false)
		{
			double diff = Close.getTimeInMillis() - cal.getTimeInMillis();
			if(diff>1800000)
				return 100;
			else
				return 30;
		}
		else
		{
			double diff = Open.getTimeInMillis() - cal.getTimeInMillis();
			if(diff>0 && diff<1800000)
				return -30;
			else
				return -100;
		}
	}
}
