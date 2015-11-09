package com.civeloo.stockreader.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String TAG = "DBHelper";
	public String failTitle = "";
	public String failMsg = "";

	/****************************** PUBLIC METHODS ***********************************/
	public DBHelper(Context context) {
		super(context, DBUtil.DB_FILE, null, DBUtil.DB_VERSION);
	}

	/** CUANDO HAY CAMBIO DE VERSION DE LA DB **/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String erase = ";DROP TABLE IF EXISTS " + DBUtil.TBL_INV
				+ ";DROP TABLE IF EXISTS " + DBUtil.TBL_SER + ";";
		try {
			// String erase = "DROP TABLE IF EXISTS " + DBUtil.TBL_INV
			// + ";DROP TABLE IF EXISTS " + DBUtil.TBL_SER + ";";
			db.execSQL(erase);
			Log.w(TAG, "LOG: Database erased...");
		} catch (SQLException e) {
			failTitle = "Error al borrar base de datos";
			failMsg = e.getMessage();
			e.printStackTrace();
		}
		onCreate(db);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		try {
			database.execSQL("CREATE TABLE IF NOT EXISTS " + DBUtil.TBL_INV
					+ "( " + DBUtil.TINV_BARC + " VARCHAR(20),"
					+ DBUtil.TINV_CLAS + " VARCHAR(20)," + DBUtil.TINV_LOCA
					+ " VARCHAR(20)," + DBUtil.TINV_QUAN + " DECIMAL(10,3));");

			database.execSQL("CREATE TABLE IF NOT EXISTS " + DBUtil.TBL_SER
					+ " ( " + DBUtil.TSER_LOCA + " VARCHAR(20), "
					+ DBUtil.TSER_BARC + " VARCHAR(20), "
					+ DBUtil.TSER_SERI + " VARCHAR(100)" + ");");

			Log.w(TAG, "LOG: Database Created...");
		} catch (SQLException e) {
			failTitle = "Error al crear base de datos";
			failMsg = e.getMessage();
			e.printStackTrace();
		}
	}

	public SQLiteDatabase open() throws SQLException {
		return this.getWritableDatabase();
	}

	public void close() {
		this.close();
	}

}