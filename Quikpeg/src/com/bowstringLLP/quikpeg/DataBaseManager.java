package com.bowstringLLP.quikpeg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bowstringLLP.quikpeg.R;


public class DataBaseManager extends SQLiteOpenHelper {

	//the name of your database
	private static String DB_NAME = "OneStopAlcohol";

	private static SQLiteDatabase mDataBase;
	Context context;
	private static DataBaseManager sInstance = null;
	// database version    
	private static final int DATABASE_VERSION = 1;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 */
	public DataBaseManager(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.context = context;
		try {
			createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Singleton for DataBase
	 *
	 * @return singleton instance
	 */
	public static DataBaseManager instance() {

		if (sInstance == null) {
			sInstance = new DataBaseManager(null);
		}
		return sInstance;
	}


	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 *
	 * @throws java.io.IOException io exception
	 */
	private void createDataBase() throws IOException {

		try 
		{
			if(!isDatabaseUpdated())
			{
				if(mDataBase != null)
					close();
				context.deleteFile(DB_NAME);
				copyDataBase();
			}
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
		openDataBase();
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 *
	 * @return true if it exists, false if it doesn't
	 */
	private boolean isDatabaseUpdated() {
		boolean flag = false;

		try {
			openDataBase();

			if (mDataBase != null) {
				String query = "SELECT*FROM MainLocation";
				SQLiteCursor cursor = (SQLiteCursor) select(query);
				if(cursor.moveToFirst())
				{
					Date databaseDate = new SimpleDateFormat("dd/MM/yy").parse(cursor.getString(cursor.getColumnIndex("DateMod")));
					Date updatedDate = new SimpleDateFormat("dd/MM/yy").parse(context.getString(R.string.updateDate));
					if(databaseDate.before(updatedDate))
						flag = false;
					else
						flag = true;
				}
			}
			close();
		} catch (Exception e) {
			e.printStackTrace();
			close();
			flag = false;
		}
		return flag;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 *
	 * @throws java.io.IOException io exception
	 */
	public void copyDataBase() throws IOException {
		try
		{
			// Open your local db as the input stream
			InputStream myInput = context.getAssets().open(DB_NAME);

			// Open the empty db as the output stream
			OutputStream myOutput = context.openFileOutput(DB_NAME, Context.MODE_PRIVATE);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}


			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void openDataBase() throws SQLException {

		// Open the database
			mDataBase = SQLiteDatabase.openDatabase(context.getFileStreamPath(DB_NAME).toString(), null, SQLiteDatabase.OPEN_READWRITE);
	}

	/**
	 * Select method
	 *
	 * @param query select query
	 * @return - Cursor with the results
	 * @throws android.database.SQLException sql exception
	 */
	public Cursor select(String query) throws Exception {
		if(mDataBase == null)
			openDataBase();
		
		return mDataBase.rawQuery(query, null);
	}

	/**
	 * Insert method
	 *
	 * @param table  - name of the table
	 * @param values values to insert
	 * @throws android.database.SQLException sql exception
	 */
	public long insert(String table, ContentValues values) throws SQLException {
		if(mDataBase == null)
			openDataBase();
return mDataBase.insert(table, null, values);
	}

	/**
	 * Delete method
	 *
	 * @param table - table name
	 * @param where WHERE clause, if pass null, all the rows will be deleted
	 * @throws android.database.SQLException sql exception
	 */
	public void delete(String table, String where) throws SQLException {
		if(mDataBase == null)
			openDataBase();
		mDataBase.delete(table, where, null);
	}

	/**
	 * Update method
	 *
	 * @param table  - table name
	 * @param values - values to update
	 * @param where  - WHERE clause, if pass null, all rows will be updated
	 */
	public void update(String table, ContentValues values, String where) {
		if(mDataBase == null)
			openDataBase();
		
		mDataBase.update(table, values, where, null);
	}

	/**
	 * Let you make a raw query
	 *
	 * @param command - the sql comand you want to run
	 */
	public void sqlCommand(String command) {
		mDataBase.execSQL(command);
	}

	@Override
	public synchronized void close() {

		if (mDataBase != null)
			mDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
