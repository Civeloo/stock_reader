package com.civeloo.stockreader.data;

import android.os.Environment;

public class DBUtil {
	public static final int DB_VERSION = 3;
	public static final String DB_FILE = "items.db";
	public static final String DB_FILE_IMPORT = "import.db";
	public static final String DB_FULLPATH = Environment.getDataDirectory()
			+ "/data/com.civeloo.stockreader/databases/";
	public static final String PATH_SDCARD = Environment.getExternalStorageDirectory().getPath();//"/mnt/sdcard/";

	// TABLAS
	public static final String TBL_ITEM = "item";
	public static final String TBL_LOC = "location";
	public static final String TBL_INV = "inventory";
	public static final String TBL_SER = "serie";
	public static final String TBL_SET = "setting";

	// TABLA DE ARTICULOS
	public static final String TITE_BARC = "barcode";
	public static final String TITE_PACK = "package";
	public static final String TITE_NAME = "name";
	public static final String TITE_PRIC = "price";
	public static final String TITE_EXIS = "existence";
	public static final String TITE_FRAC = "fractionally";
	public static final String TITE_CONV = "convertfactor";
	public static final String TITE_UNIT = "unitforpack";
	public static final String TITE_SERI = "serializable";

	// USADOS EN LOS INSERT AND UPDATES
	public static final String[] TART_COLS = { TITE_BARC, TITE_PACK,
			TITE_NAME, TITE_PRIC, TITE_EXIS, TITE_FRAC, TITE_CONV,
			TITE_UNIT, TITE_SERI };

	public static final String TART_ALLCOLS = TITE_BARC + "," + TITE_PACK
			+ "," + TITE_NAME + "," + TITE_PRIC + "," + TITE_EXIS + ","
			+ TITE_FRAC + "," + TITE_CONV + "," + TITE_UNIT + ","
			+ TITE_SERI;

	// TABLA DE LOCACIONES
	public static final String TLOC_ID = "id";
	public static final String TLOC_NAME = "name";
	public static final String[] TLOC_COLS = { TLOC_ID, TLOC_NAME };
	public static final String TLOC_ALLCOLS = TLOC_ID + "," + TLOC_NAME;

	// TABLA DE INVENTARIOS
	public static final String TINV_BARC = "barcode";
	public static final String TINV_CLAS = "class";
	public static final String TINV_LOCA = "location";
	public static final String TINV_QUAN = "quantity";

	// USADOS EN LOS FIND, INSERT AND UPDATES
	public static final String[] TINV_COLS = { TITE_BARC, TINV_CLAS, TINV_LOCA,
			TINV_QUAN };

	// TABLA DE SERIALIZABLES
	public static final String TSER_LOCA = "location";
	public static final String TSER_BARC = "barcode";
	public static final String TSER_SERI = "serial";
	public static final String[] TSER_COLS = { TSER_LOCA, TSER_BARC,
			TSER_SERI };

	// TABLA DE CONFIGURACION DEL APP
	public static final String TSET_URL = "url";
	public static final String TSET_PERMISSION = "permission";
	public static final String[] TSET_COLS = { TSET_URL, TSET_PERMISSION };
	public static final String TSET_ALLCOLS = TSET_URL + "," + TSET_PERMISSION;

}