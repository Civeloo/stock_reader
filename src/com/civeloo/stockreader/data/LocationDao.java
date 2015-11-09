package com.civeloo.stockreader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.civeloo.stockreader.logic.Location;

public class LocationDao {
	SQLiteDatabase db;

	/** CONEXION A LA BASE **/
	public LocationDao(Context c) {
		// ABRO LA CONEXION CON SQLITE3
		db = (new DBHelperClient(c)).open();
	}

	/** SETEO DE VARIABLES LUEGO DE UNA CONSULTA **/
	private Location rowMapper(Cursor c) {
		// NO EXISTE LA LOCACION, NO CONTINUO !
		if (c.getCount() == 0)
			return new Location();

		Location row = new Location();
		c.moveToNext();

		row.setCodigo(c.getString(0));
		row.setDescripcion(c.getString(1));
		c.close();

		return row;
	}

	/** CONSULTA POR CODIGO **/
	public Location findByPrimaryKey(String plu) {
		String where = DBUtil.TLOC_ID + "= '" + plu + "'";
		Cursor c = db.query(DBUtil.TBL_LOC, DBUtil.TLOC_COLS, where, null,
				null, null, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS **/
	public Location find() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_LOC, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS QUE DEVUELVE CURSOR **/
	public Cursor getDataToSpinner() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_LOC, null);
		return c;
	}

	/** SETEO DE VALORES PARA AGREGAR Y ACTUALIZAR FILAS A LA TABLA **/
	private ContentValues loadObject(Location loc) {
		ContentValues c = new ContentValues();
		c.put(DBUtil.TLOC_ID,
				(loc.getCodigo() != null && loc.getCodigo() != "") ? loc
						.getCodigo() : "");
		c.put(DBUtil.TLOC_NAME, (loc.getDescripcion() != null && loc
				.getDescripcion() != "") ? loc.getDescripcion() : "");

		return c;
	}

	/** INSERCION DE FILAS EN UNA TABLA **/
	public void insert(Location loc) {
		db.insert(DBUtil.TBL_LOC, null, loadObject(loc));
	}

	/**
	 * ACTUALIZACION DE CAMPOS DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN
	 * CODIGO
	 **/
	public void update(Location loc) {
		db.update(DBUtil.TBL_LOC, loadObject(loc), DBUtil.TLOC_ID + "= '"
				+ loc.getCodigo() + "'", null);
	}

	/** ELIMINACION DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN CODIGO **/
	public void DeleteLocacion(String plu) {
		String where = DBUtil.TLOC_ID + "= '" + plu + "'";
		db.delete(DBUtil.TBL_LOC, where, null);
	}
}
