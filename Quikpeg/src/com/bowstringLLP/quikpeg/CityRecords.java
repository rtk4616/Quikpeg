package com.bowstringLLP.quikpeg;

public class CityRecords implements Comparable<CityRecords> {

	String name;
	double latitude;
	double longitude;
	String dateMod;
	Float distance;
	
	@Override
	public int compareTo(CityRecords arg0) {
		try{
			
			if(distance == 0 || distance<arg0.distance)
				return -1;
		
			if(distance == 0 || distance>arg0.distance)
				return 1;
			
			return 0;
		}catch(Exception e)
		{
			return 0;
		}
	}

}
