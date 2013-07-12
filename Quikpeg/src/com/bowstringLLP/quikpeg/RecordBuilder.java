package com.bowstringLLP.quikpeg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.location.Location;
import android.os.StrictMode;

import com.bowstringLLP.quikpeg.MainActivity.Mode;

public class RecordBuilder{

	DataBaseManager manager;
	SQLiteCursor cursor;
	private List<Records> masterRecordList, openRecordList;
	DryDay dryDay;
	Records rec;
	Context context;

	public RecordBuilder(Context context)
	{
		this.context = context;
		manager = new DataBaseManager(context);
	}

	public boolean buildRecordList(Location currentLocation)
	{
		try{
			String stateName = getCity(currentLocation).name;
			makeList(currentLocation, stateName);
			isDryDay(stateName);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private CityRecords getCity(Location currentLocation)
	{
		List<CityRecords> cityRecList = new ArrayList<CityRecords>();
		try{
			if(cursor!=null)
				cursor.close();

			String query = "SELECT*FROM MainLocation";
			cursor = (SQLiteCursor) manager.select(query);
			Location loc = new Location(currentLocation);
			
			if(cursor.moveToFirst())
			{
				do
				{
					CityRecords cityRec = new CityRecords();

					cityRec.name = cursor.getString(cursor.getColumnIndex("City"));
					cityRec.latitude = cursor.getDouble(cursor.getColumnIndex("Latitude"));
					cityRec.longitude = cursor.getDouble(cursor.getColumnIndex("Longitude"));
					cityRec.dateMod = cursor.getString(cursor.getColumnIndex("DateMod"));

					loc.setLatitude(cityRec.latitude);
					loc.setLongitude(cityRec.longitude);


					cityRec.distance = currentLocation.distanceTo(loc);

					cityRecList.add(cityRec);
				}while(cursor.moveToNext());

				Collections.sort(cityRecList);
				cursor.close();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return cityRecList.get(0);
	}

	private void makeList(Location currentLocation, String stateName)
	{
		try
		{
			String query = "SELECT*FROM " + (currentLocation == null ? "OfflineList" : stateName);
			cursor = (SQLiteCursor) manager.select(query);
			Location storeLocation = new Location(currentLocation);
			if(cursor.moveToFirst())
			{
				if(masterRecordList == null)
					masterRecordList = new ArrayList<Records>();
				else
					masterRecordList.clear();
				if(openRecordList == null)
					openRecordList = new ArrayList<Records>();
				else
					openRecordList.clear();

				do
				{
					storeLocation.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
					storeLocation.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitude")));

					float f = currentLocation == null ? 0 : currentLocation.distanceTo(storeLocation);
					if(f <= 3000)
					{

						rec = new Records();
						rec.name = cursor.getString(cursor.getColumnIndex("Name"));
						rec.latitude = storeLocation.getLatitude();
						rec.longitude = storeLocation.getLongitude();

						String str = cursor.getString(cursor.getColumnIndex("Verified"));
						rec.isVerified =  str.toUpperCase(Locale.ENGLISH).matches("YES") ? true : false;

						rec.region = cursor.getString(cursor.getColumnIndex("Region"));
						rec.address = cursor.getString(cursor.getColumnIndex("Address"));
						rec.notes = cursor.getString(cursor.getColumnIndex("Notes"));
						rec.mondayOpen = cursor.getInt(cursor.getColumnIndex("MondayOpen"));
						rec.mondayClose = cursor.getInt(cursor.getColumnIndex("MondayClose"));
						rec.tuesdayOpen = cursor.getInt(cursor.getColumnIndex("TuesdayOpen"));
						rec.tuesdayClose = cursor.getInt(cursor.getColumnIndex("TuesdayClose"));
						rec.wednesdayOpen = cursor.getInt(cursor.getColumnIndex("WednesdayOpen"));
						rec.wednesdayClose = cursor.getInt(cursor.getColumnIndex("WednesdayClose"));
						rec.thursdayOpen = cursor.getInt(cursor.getColumnIndex("ThursdayOpen"));
						rec.thursdayClose = cursor.getInt(cursor.getColumnIndex("ThursdayClose"));
						rec.fridayOpen = cursor.getInt(cursor.getColumnIndex("FridayOpen"));
						rec.fridayClose = cursor.getInt(cursor.getColumnIndex("FridayClose"));
						rec.saturdayOpen = cursor.getInt(cursor.getColumnIndex("SaturdayOpen"));
						rec.saturdayClose = cursor.getInt(cursor.getColumnIndex("SaturdayClose"));
						rec.sundayOpen = cursor.getInt(cursor.getColumnIndex("SundayOpen"));
						rec.sundayClose = cursor.getInt(cursor.getColumnIndex("SundayClose"));				
						rec.distance = currentLocation == null ? null : getDistanceFromLocation(storeLocation, currentLocation);
						masterRecordList.add(rec);
						if(rec.timeTillOpenClose()>0)
							openRecordList.add(rec);
					}
				}while(cursor.moveToNext());
				cursor.close();

				if(currentLocation!=null)
				{
					Collections.sort(masterRecordList);
					Collections.sort(openRecordList);
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void isDryDay(String stateName)
	{
		try
		{
			String query = "SELECT*FROM DryDays";
			cursor = (SQLiteCursor) manager.select(query);

			if(cursor.moveToFirst())
			{
				Calendar currentDate = new GregorianCalendar();
				Calendar cal = new GregorianCalendar();
				do
				{
					String state = cursor.getString(cursor.getColumnIndex("State")).toUpperCase(Locale.ENGLISH);
					if(state.matches(stateName) || state.matches("NATIONAL"))
					{
						cal.setTime(new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).parse(cursor.getString(cursor.getColumnIndex("Date"))));
						switch(daysBetween(currentDate.getTime(), cal.getTime()))
						{
						case 0:	dryDay = new DryDay();
						dryDay.holidayDate.setTime(new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).parse(cursor.getString(cursor.getColumnIndex("Date"))));
						dryDay.holidayName = cursor.getString(cursor.getColumnIndex("Holiday"));
						MainActivity.settings.edit().putString("Mode", Mode.DRY.toString()).commit();
						break;

						case 1:	dryDay = new DryDay();
						dryDay.holidayDate.setTime(new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).parse(cursor.getString(cursor.getColumnIndex("Date"))));
						dryDay.holidayName = cursor.getString(cursor.getColumnIndex("Holiday"));
						MainActivity.settings.edit().putString("Mode", Mode.DAYBEFOREDRY.toString()).commit();
						break;
						}
					}
				}while(cursor.moveToNext());
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int daysBetween(Date d1, Date d2){
	     return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	         }
	
	public List<Records> getMasterRecordList()
	{
		return masterRecordList;
	}

	public List<Records> getOpenRecordList()
	{
		return openRecordList;
	}
	
	public DryDay getDryDay()
	{
		return dryDay;
	}

	private Float getDistanceFromLocation(Location storeLocation, Location currentLocation)
	{	
		try
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			StringBuilder urlString = new StringBuilder();
			//urlString.append("http://maps.googleapis.com/maps/api/distancematrix/json?");
			//urlString.append("origins=");//from
			//urlString.append( Double.toString(locOrigin.getLatitude() / 1E6));
			//urlString.append(",");
			//urlString.append( Double.toString(locOrigin.getLongitude()/ 1E6));
			//urlString.append("&destinations=");//to
			//urlString.append( Double.toString(latDest/ 1E6));
			//urlString.append(",");
			//urlString.append( Double.toString(longDest/ 1E6));
			//urlString.append("&mode=driving&sensor=true");
			//Log.d("xxx","URL="+urlString.toString());
			urlString.append("http://maps.googleapis.com/maps/api/distancematrix/json?origins=12.994,77.663317&destinations=12.585187,77.046223&mode=driving&sensor=true");

			// get the JSON And parse it to get the directions data.
			HttpURLConnection urlConnection= null;
			URL url = null;

			url = new URL("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "&destinations=" + storeLocation.getLatitude() + "," + storeLocation.getLongitude() + "&mode=driving&sensor=true");
			urlConnection=(HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("GET");
			//urlConnection.setDoOutput(true);
			//urlConnection.setDoInput(true);
			//urlConnection.connect();

			//InputStream inStream = urlConnection.getInputStream();
			//try{
			BufferedReader bReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			//} catch (Exception e) {
			//	e.printStackTrace();
			//}


			String temp, response = "";
			while((temp = bReader.readLine()) != null){
				//Parse data
				response += temp;
			}
			//Close the reader, stream & connection
			bReader.close();
			//inStream.close();
			urlConnection.disconnect();

			//Sortout JSONresponse 
			JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
			JSONArray rows = object.getJSONArray("rows");
			JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
			JSONObject distance;
			String[] sDistance = new String[20];
			for (int i=0; i<elements.length(); i++)
			{
				distance = elements.getJSONObject(i).getJSONObject("distance");
				sDistance[i] = distance.getString("text");
			}

			return Float.parseFloat(sDistance[0].split(" ")[0]);
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}		

	public void writeGoodRecords()
	{
		try
		{
			ContentValues value = new ContentValues();
			for(int i=0; i<masterRecordList.size(); i++)
			{
				Records rec = masterRecordList.get(i);
				value.clear();
				value.put("Name", rec.name);
				value.put("Region", rec.region);
				value.put("Address", rec.address);
				value.put("Verified",rec.isVerified == true ? "YES" : "NO");
				value.put("Longitude", rec.longitude);
				value.put("Latitude", rec.latitude);
				value.put("MondayOpen", rec.mondayOpen);
				value.put("MondayClose", rec.mondayClose);
				value.put("TuesdayOpen", rec.tuesdayOpen);
				value.put("TuesdayClose", rec.tuesdayClose);
				value.put("WednesdayOpen", rec.wednesdayOpen);
				value.put("WednesdayClose", rec.wednesdayClose);
				value.put("ThursdayOpen", rec.thursdayOpen);
				value.put("ThursdayClose", rec.thursdayClose);
				value.put("FridayOpen", rec.fridayOpen);
				value.put("FridayClose", rec.fridayClose);
				value.put("SaturdayOpen", rec.saturdayOpen);
				value.put("SaturdayClose", rec.saturdayClose);
				value.put("SundayOpen", rec.sundayOpen);
				value.put("SundayClose", rec.sundayClose);
				value.put("_id", i);

				manager.delete("OfflineList", null);
				manager.insert("OfflineList", value);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean readGoodRecords()
	{
		try{
			makeList(null, null);
			Location loc = new Location("Null");
			if(masterRecordList != null)
			{
				loc.setLatitude(masterRecordList.get(0).latitude);
				loc.setLongitude(masterRecordList.get(0).longitude);
			}

			isDryDay(getCity(loc).name);
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public void closeDatabase()
	{
		manager.close();
	}
}

