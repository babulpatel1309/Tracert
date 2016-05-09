package com.kid.retro.com.tracerttask.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kid.retro.com.tracerttask.Model.LocationHistory;
import com.kid.retro.com.tracerttask.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

	private static SQLiteDatabase myDataBase;
	private static Context myContext;
	public int count = 0;

	@SuppressWarnings("static-access")
	public DatabaseHelper(Context context) {

		super(context, context.getResources().getString(R.string.DB_NAME),
				null, 1);
		this.myContext = context;
	}

	// ---Create the database---
	public void createDataBase() throws IOException {

		// ---Check whether database is already created or not---
		boolean dbExist = checkDataBase();

		if (!dbExist) {
			this.getReadableDatabase();
			try {
				// ---If not created then copy the database---
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error " + " database" + e.getMessage());
			}
		}

	}

	private void copyDataBase() throws IOException {

		InputStream myInput = myContext.getAssets().open(
				myContext.getString(R.string.DB_NAME));

		String outFileName = myContext.getString(R.string.DB_PATH)
				+ myContext.getString(R.string.DB_NAME);

		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	// --- Check whether database already created or not---
	private boolean checkDataBase() {

		try {

			String myPath = myContext.getString(R.string.DB_PATH)
					+ myContext.getString(R.string.DB_NAME);
			File f = new File(myPath);
			if (f.exists())
				return true;
			else
				return false;

		} catch (SQLiteException e) {

			e.printStackTrace();
			return false;
		}

	}

	public static void openDataBase() throws SQLException {

		// --- Open the database---
		String myPath = myContext.getString(R.string.DB_PATH)
				+ myContext.getString(R.string.DB_NAME);
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

		Log.e("MY DATABASE", "" + myDataBase);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void addLocation(String s, String formatted, String s1, String s2, String finalTxt) {

		openDataBase();
		ContentValues values = new ContentValues();
		values.put("email", s);
		values.put("date", formatted);
		values.put("latitude", s1);
		values.put("longitude", s2);
		values.put("Address", finalTxt);
		myDataBase.insert("Location", null, values);
		close();
	}

	public ArrayList<LocationHistory> getparentItem() {
		// TODO Auto-generated method stub


		final Cursor cursor;

		try {
			openDataBase();
		}catch (Exception e){
			e.printStackTrace();
		}
		ArrayList<LocationHistory> arrayList = new ArrayList<LocationHistory>();

		LocationHistory productDesc;

		cursor = myDataBase.rawQuery("select * from Location", null);

		if (cursor != null) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {

				productDesc = new LocationHistory();


				productDesc.setLocation_id(cursor.getInt(cursor
						.getColumnIndex("id")));
				productDesc.setLatitude(cursor.getString(cursor
						.getColumnIndex("latitude")));
				productDesc.setLongitude(cursor.getString(cursor
						.getColumnIndex("longitude")));
				productDesc.setAddress(cursor.getString(cursor
						.getColumnIndex("Address")));

				productDesc.setOn_date(cursor.getString(cursor
						.getColumnIndex("date")));


				arrayList.add(productDesc);

				cursor.moveToNext();

			}
		}
		myDataBase.close();

		cursor.close();

		SQLiteDatabase.releaseMemory();

		return arrayList;

	}

}
