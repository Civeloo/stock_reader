package com.civeloo.stockreader.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperClient extends SQLiteOpenHelper {

	private static final String TAG = "DBHelperClient";
	public String failTitle = "";
	public String failMsg = "";

	/****************************** PUBLIC METHODS ***********************************/
	public DBHelperClient(Context context) {
		super(context, DBUtil.DB_FILE_IMPORT, null, DBUtil.DB_VERSION);
	}

	/** CUANDO HAY CAMBIO DE VERSION DE LA DB **/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			// String erase = "DROP TABLE IF EXISTS " + DBUtil.TBL_ART
			// + ";DROP TABLE IF EXISTS " + DBUtil.TBL_LOC + ";";
			// db.execSQL(erase);
			Log.d(TAG, "[DEBUG]: Database erased....................[ OK ]");
		} catch (SQLException e) {
			failTitle = "Fail to delete database";
			failMsg = e.toString();
			e.printStackTrace();
		}
		onCreate(db);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		try {
			database.execSQL("CREATE TABLE IF NOT EXISTS " + DBUtil.TBL_ITEM
					+ " ( " + DBUtil.TITE_BARC + " VARCHAR(20),"
					+ DBUtil.TITE_PACK + " VARCHAR(20)," + DBUtil.TITE_NAME
					+ " VARCHAR(70)," + DBUtil.TITE_PRIC + " DECIMAL(10,3),"
					+ DBUtil.TITE_EXIS + " DECIMAL(10,3)," + DBUtil.TITE_FRAC
					+ " BOOLEAN, " + DBUtil.TITE_CONV + " DECIMAL(10,3),"
					+ DBUtil.TITE_UNIT + " INTEGER, " + DBUtil.TITE_SERI
					+ " BOOLEAN" + ");");
			database.execSQL("CREATE TABLE IF NOT EXISTS " + DBUtil.TBL_LOC
					+ " ( " + DBUtil.TLOC_ID + " VARCHAR(20), "
					+ DBUtil.TLOC_NAME + " VARCHAR(70)" + ");");
			database.execSQL("CREATE TABLE IF NOT EXISTS " + DBUtil.TBL_SET
					+ " ( " + DBUtil.TSET_URL + " VARCHAR(20), "
					+ DBUtil.TSET_PERMISSION + " INTEGER " + ");");
			Log.d(TAG, "[DEBUG]: Database Created...............[ OK ]");
		} catch (SQLException e) {
			failTitle = "Fail to delete database";
			failMsg = e.getMessage();
			Log.e(TAG, "[ERROR]: " + e.getMessage());
		}
	}

	public SQLiteDatabase open() throws SQLException {
		return this.getWritableDatabase();
	}

	public void close() {
		this.close();
	}

}