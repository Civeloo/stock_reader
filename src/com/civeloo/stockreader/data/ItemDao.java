package com.civeloo.stockreader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.civeloo.stockreader.logic.Item;

public class ItemDao extends Item {
	SQLiteDatabase db;

	/** CONEXION A LA BASE **/
	public ItemDao(Context c) {
		// ABRO LA CONEXION CON SQLITE3
		db = (new DBHelperClient(c)).open();
	}

	/** SETEO DE VARIABLES LUEGO DE UNA CONSULTA **/
	private Item rowMapper(Cursor c) {
		// NO EXISTE EL ARTICULO, NO CONTINUO !
		if (c.getCount() == 0)
			return new Item();

		c.moveToNext();
		Item row = new Item();
		row.setPlu(c.getString(0));
		row.setCod_pkg(c.getString(1));
		row.setDescr(c.getString(2));
		row.setPrecio(c.getDouble(3));
		row.setExistencia(c.getShort(4));
		// row.setFraccionable((c.getShort(5) == 1) ? true : false);
		row.setFraccionable(c.getString(5).contains("true")
				|| c.getShort(5) == 1);
		row.setFactor_conver(c.getDouble(6));
		row.setUnidades_x_pack(c.getInt(7));
		// row.setSerializable((c.getShort(8) == 1) ? true : false);
		row.setSerializable(c.getString(8).contains("true")
				|| c.getShort(8) == 1);

		c.close();
		return row;
	}

	/** CONSULTA POR CODIGO **/
	public Item findByPrimaryKey(String id) {
		String where = DBUtil.TITE_BARC + "='" + id + "'" + " OR "
				+ DBUtil.TITE_PACK + "='" + id + "'";
		Cursor c = db.query(DBUtil.TBL_ITEM, DBUtil.TART_COLS, where, null,
				null, null, null);
		return rowMapper(c);
	}

	/** FUNCIONES INTERNAS DE JAVA PARA COMPARACIONES **/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((db == null) ? 0 : db.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemDao other = (ItemDao) obj;
		if (db == null) {
			if (other.db != null)
				return false;
		} else if (!db.equals(other.db))
			return false;
		return true;
	}

	/** CONSULTA SIN FILTROS **/
	public Item find() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_ITEM, null);
		return rowMapper(c);
	}

	/** CONSULTA SIN FILTROS QUE DEVUELVE CURSOR **/
	public Cursor getDataTo() {
		Cursor c = db.rawQuery("SELECT * FROM " + DBUtil.TBL_ITEM, null);
		return c;
	}

	/** SETEO DE VALORES PARA AGREGAR Y ACTUALIZAR FILAS A LA TABLA **/
	private ContentValues loadObject(Item a) {
		ContentValues c = new ContentValues();
		c.put(DBUtil.TITE_BARC,
				(a.getPlu() != null && a.getPlu() != "") ? a.getPlu() : "");
		c.put(DBUtil.TITE_PACK,
				(a.getCod_pkg() != null && a.getCod_pkg() != "") ? a
						.getCod_pkg() : "");
		c.put(DBUtil.TITE_NAME,
				(a.getDescr() != null && a.getDescr() != "") ? a.getDescr()
						: "");
		c.put(DBUtil.TITE_PRIC, a.getPrecio());
		c.put(DBUtil.TITE_EXIS, a.getExistencia());
		c.put(DBUtil.TITE_FRAC, (a.isFraccionable()) ? 1 : 0);
		c.put(DBUtil.TITE_CONV, a.getFactor_conver());
		c.put(DBUtil.TITE_UNIT, a.getUnidades_x_pack());
		c.put(DBUtil.TITE_SERI, (a.isSerializable()) ? 1 : 0);
		return c;
	}

	/** INSERCION DE FILAS EN UNA TABLA **/
	public void insert(Item a) {
		db.insert(DBUtil.TBL_ITEM, null, loadObject(a));
	}

	/**
	 * ACTUALIZACION DE CAMPOS DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN
	 * CODIGO
	 **/
	public void update(Item a) {
		db.update(DBUtil.TBL_ITEM, loadObject(a),
				DBUtil.TITE_BARC + "='" + a.getPlu() + "'", null);
	}

	/** ELIMINACION DE UNA FILA EN UNA TABLA CORRESPONDIENTE A UN CODIGO **/
	public void DeleteArticulo(String plu) {
		String where = DBUtil.TITE_BARC + "='" + plu + "'";
		db.delete(DBUtil.TBL_ITEM, where, null);
	}

	/** CREACION DE INDICE PARA UNA TABLA **/
	public void createIndex() {
		db.execSQL("CREATE UNIQUE INDEX idx_articulo ON articulos (PLU,cod_pkg)");

	}
}