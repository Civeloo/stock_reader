package com.civeloo.stockreader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.civeloo.stockreader.logic.Inventory;

public class InventoryDao extends Inventory {
	DBHelper data;
	SQLiteDatabase db;

	/** CONEXION A LA BASE **/
	public InventoryDao(Context c) {
		// ABRO LA CONEXION CON SQLITE3
		db = (new DBHelper(c)).open();
	}

	/** SETEO DE VARIABLES LUEGO DE UNA CONSULTA **/
	private Inventory rowMapper(Cursor c) {
		// NO EXISTE EL CAMPO, NO CONTINUO !
		if (c.getCount() == 0)
			return new Inventory();

		c.moveToNext();
		Inventory row = new Inventory();
		row.setPlu(c.getString(0));
		row.setTipCod(c.getString(1));
		row.setCodLoc(c.getString(2));
		row.setCantArt(c.getDouble(3));
		c.close();
		return row;
	}

	/** CONSULTA POR CODIGO **/
	public Inventory findByPrimaryKey(String id, String location) {
		String where = "(" + DBUtil.TINV_BARC + "='" + id + "'" + " OR "
				+ DBUtil.TINV_CLAS + "='" + id + "'" + ") and "
				+ DBUtil.TINV_LOCA + "='" + location + "'";
		Cursor c = db.query(DBUtil.TBL_INV, DBUtil.TINV_COLS, where, null,
				null, null, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS **/
	public Inventory find() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_INV, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS QUE DEVUELVE CURSOR **/
	public Cursor getDataTo() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_INV, null);
		return c;
	}

	/** SETEO DE VALORES PARA AGREGAR Y ACTUALIZAR FILAS A LA TABLA **/
	private ContentValues loadObject(Inventory a) {
		ContentValues c = new ContentValues();
		c.put(DBUtil.TINV_BARC,
				(a.getPlu() != null && a.getPlu() != "") ? a.getPlu() : "");
		c.put(DBUtil.TINV_CLAS,
				(a.getTipCod() != null && a.getTipCod() != "") ? a.getTipCod()
						: "");
		c.put(DBUtil.TINV_LOCA,
				(a.getCodLoc() != null && a.getCodLoc() != "") ? a.getCodLoc()
						: "");
		c.put(DBUtil.TINV_QUAN,
				(a.getCantArt() != null && a.getCantArt() != 0) ? a
						.getCantArt() : 0);
		return c;
	}

	/** INSERCION DE FILAS EN UNA TABLA **/
	public void insert(Inventory a) {
		db.insert(DBUtil.TBL_INV, null, loadObject(a));
	}

	/**
	 * ACTUALIZACION DE CAMPOS DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN
	 * CODIGO
	 **/
	public void update(Inventory a) {
		String where = "" + DBUtil.TINV_BARC + "='" + a.getPlu() + "'" + " AND "
				//+ DBUtil.TINV_CLAS + "='" + a.getTipCod() + "'" + ") AND "
				+ DBUtil.TINV_LOCA + "='" + a.getCodLoc() + "'";
		db.update(DBUtil.TBL_INV, loadObject(a), where, null);
	}

	/** ELIMINACION DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN CODIGO **/
	public void DeleteInventario(String id, String location) {
		String where = "" + DBUtil.TINV_BARC + "='" + id + "'" + " AND "
				//+ DBUtil.TINV_CLAS + "='" + id + "'" + ") and "
				+ DBUtil.TINV_LOCA + "='" + location + "'";
		db.delete(DBUtil.TBL_INV, where, null);
	}

	/** ELIMINACION DE TODOS LOS REGISTROS DE UNA TABLA **/
	public void DeleteAllInventario() {
		db.delete(DBUtil.TBL_INV, null, null);
	}

	/** DEVUELVE LA CONEXION A LA BASE **/
	public SQLiteDatabase getDBConnection() {
		return this.db;
	}
}
