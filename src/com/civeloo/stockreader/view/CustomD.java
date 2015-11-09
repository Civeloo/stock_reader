package com.civeloo.stockreader.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.civeloo.stockreader.R;
import com.civeloo.stockreader.data.DBHelper;
import com.civeloo.stockreader.data.DBHelperClient;
import com.civeloo.stockreader.data.DBUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.Calendar;

public class CustomD extends Activity {
	// variables
	ImageButton add;
	public TextView text;
	public String path, fail;
	public Integer operation, table;
	public final String SEPARATOR_COMMA = ",";
	public final String SEPARATOR_PIPE = "|";
	public final String SEPARATOR_POINTANDCOMMA = ";";
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	public static final int DIALOG_EXPORT_PROGRESS = 1;
	private ProgressDialog mProgressDialog;
	// *********************************************************************
	File res = null, org = null;
	FileOutputStream fos = null;
	BufferedInputStream inputStr;
	URL url = null;
	int sizefile;
	long startTime;

	/** CUANDO SE CREA EL ACTIVITY */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// traer datos del formulario llamador
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			path = extras.getString("url");
			operation = extras.getInt("operation");
			if (operation == 3)
				table = extras.getInt("table");
		}
		setContentView(R.layout.custom_dialog);
	}

	/** CUANDO INICIA LA PANTALLA **/
	@Override
	protected void onResume() {
		super.onResume();
		fail = "";
		switch (operation) {
		case (1): {
			importWeb(path);
		}
			break;
		case (2): {
			importSD(path);
		}
			break;
		case (3): {
			importCsv(path);
		}
			break;
		case (4): {
			exportSD(path);
		}
			break;
		case (5): {
			exportCsv(path);
		}
			break;
		}
	}

	/** TRAE LA FECHA Y HORA ACTUAL **/
	public String getDateNow() {
		Calendar ci = Calendar.getInstance();
		return ("" + ci.get(Calendar.YEAR) + (ci.get(Calendar.MONTH) + 1)
				+ ci.get(Calendar.DAY_OF_MONTH) + ci.get(Calendar.HOUR)
				+ ci.get(Calendar.MINUTE) + ci.get(Calendar.SECOND));
	}

	/** IMPORTA UN ARCHIVO **/
	public void importSD(String destino) {
		try {
			copyFile(new File(destino), new File(DBUtil.DB_FULLPATH
					+ DBUtil.DB_FILE_IMPORT));
		} catch (IOException ex) {
			fail = "Error al importar base ";
			Log.e("ERROR: COPY DB", ex.getMessage());
		} finally {
			exit();
		}
	}

	/** COPIA UN ARCHIVO **/
	public void copyFile(File src, File dst) throws IOException {
		FileChannel inChannel = new FileInputStream(src).getChannel();
		FileChannel outChannel = new FileOutputStream(dst).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

	/** EXPORTA UN ARCHIVO **/
	public void exportSD(String destino) {
		try {
			if (destino.equals("scanner")) {
				destino=DBUtil.PATH_SDCARD+destino;
				CreateDir(destino); 
			}
			copyFile(new File(DBUtil.DB_FULLPATH + DBUtil.DB_FILE), new File(
					destino + "/" + getDateNow() + DBUtil.DB_FILE));
		} catch (IOException ex) {
			fail = "Error al copiar base ";
			Log.e("ERROR: COPY DB", ex.getMessage());
		} finally {
			exit();
		}
	}

	/** CREA UN DIRECTORIO **/
	public void CreateDir(String dir) {
		// crea la carpeta si no existe
		File file = new File(dir);
		if (!file.exists())
			file.mkdirs();
	}

	/** CIERRA LA ACTIVITY Y PASA LOS PARAMETROS **/
	public void exit() {
		// En la Activity llamada utilizamos esto para devolver el parametro
		Intent resultIntent;
		resultIntent = new Intent();
		resultIntent.putExtra("result", fail);
		resultIntent.putExtra("operation", "1");
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

	/** IMPORTA UNA BASE DESDE LA WEB **/
	private void importWeb(String url) {
		new DownloadFileAsync().execute(url);
	}

	/** IMPORTA UNA TABLA POR CSV **/
	private void importCsv(String url) {
		new ImportCsvFileAsync().execute(url);
	}

	/** EXPORTA UNA TABLA POR CSV **/
	private void exportCsv(String url) {
		new ExportCsvFileAsync().execute(url);
	}

	/** DIALOGO DE PROGRESO **/
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Importando...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		case DIALOG_EXPORT_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Exportando...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}

	/** DESCARGA ASICRONICA DE LA WEB **/
	class DownloadFileAsync extends AsyncTask<String, String, String> {
		// ANTES DE EJECUTAR
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		// EJECUCION
		@Override
		protected String doInBackground(String... aurl) {
			int count;
			try {
				URL url = null;
				try {
					url = new URL(aurl[0]);
				} catch (MalformedURLException e) {
					fail = "Mal formada url: " + aurl[0];
					Log.e("DownloadDB", fail);
					// return;
				}
				URLConnection conexion = url.openConnection();
				try {
					conexion.connect();
				} catch (Exception e) {
					// TODO: handle exception
					fail = "Error al conectarse";
				}
				

				int lenghtOfFile = conexion.getContentLength();
				Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(DBUtil.PATH_SDCARD
						+ "import.db");

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				try {
					copyFile(
							new File(DBUtil.PATH_SDCARD + "import.db"),
							new File(DBUtil.DB_FULLPATH + DBUtil.DB_FILE_IMPORT));
				} catch (IOException ex) {
					Log.e("ERROR: COPY DB", ex.getMessage());
				}
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				fail = "Error al descargar de la web";
			} finally {
				try {
					File file = new File(DBUtil.PATH_SDCARD + "import.db");
					if (file != null && file.exists())
						file.delete();
					exit();
				} catch (Exception e2) {
					// TODO: handle exception
					fail = "Error al descargar de la web";
				}
			}
			return null;
		}

		// DURANTE LA EJECUCION
		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		// AL FINALIZAR LA EJECUCION
		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

	}

	/** IMPORTACION ASINCRONICA DE UN ARCHIVO POR CSV **/
	class ImportCsvFileAsync extends AsyncTask<String, String, String> {
		// ANTES
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		// AHORA
		@Override
		protected String doInBackground(String... destino) {
			String tablaTo = "";
			String col = "";// campos
			switch (table) {
			case 0: {
				tablaTo = DBUtil.TBL_ITEM;
				col = DBUtil.TART_ALLCOLS;
			}
				break;
			case 1: {
				tablaTo = DBUtil.TBL_LOC;
				col = DBUtil.TLOC_ALLCOLS;
			}
				break;
			case 2: {
				tablaTo = DBUtil.TBL_SET;
				col = DBUtil.TSET_ALLCOLS;
			}
				break;
			}
			String fileCSV = destino[0];
			if (!(new File(fileCSV)).exists())
				try {
					throw new FileNotFoundException(
							"No es posible hallar el archivo");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			SQLiteDatabase db = (new DBHelperClient(CustomD.this)).open();
			Boolean isBlocking = false;
			String datacvs = "";
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(fileCSV));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String qry = "INSERT INTO " + tablaTo + "(" + col + ") VALUES";
			// SCREEN BLOCK PROGRESS DIALOG
			isBlocking = true;
			String x[];
			//
			BufferedReader br1 = null;
			try {
				br1 = new BufferedReader(new FileReader(fileCSV));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int i = 0;
			int length = 0;
			try {
				while (br1.readLine() != null) {
					length++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// deteccion del delimitador
			try {
				while ((datacvs = br.readLine()) != null) {
					x = datacvs.split(";");
					if (x.length < 2) {
						x = datacvs.split("|");
					}
					if (x.length < 2) {
						x = datacvs.split("\t");
					}
					if (x.length < 2) {
						x = datacvs.split(",");
					}
					if (x.length < 2)
						throw new Exception(
								"El delimitador no corresponde con el del archivo, por favor use [;]");
					try {
						Integer l = 0;
						switch (table) {
						case 0: {// tablaTo=DBUtil.TBL_ART;
							datacvs = "("
									+ ((!x[0].equals("")) ? "'" + x[0] + "'"
											: "null");
							datacvs += ","
									+ ((!x[1].equals("")) ? "'" + x[1] + "'"
											: "null");
							datacvs += ","
									+ ((!x[2].equals("")) ? "'" + x[2] + "'"
											: "null");
							datacvs += ","
									+ ((!x[3].equals("")) ? x[3] : "null");
							datacvs += ","
									+ ((!x[4].equals("")) ? x[4] : "null");
							datacvs += ","
									+ ((x[5].contains("true")) ? "'true'"
											: "'false'");
							datacvs += ","
									+ ((!x[6].equals("")) ? x[6] : "null");
							datacvs += ","
									+ ((!x[7].equals("")) ? x[7] : "null");
							datacvs += ","
									+ ((x[8].contains("true")) ? "'true'"
											: "'false'");
							datacvs += ")";
							l = 9;
						}
							break;
						case 1: {// tablaTo=DBUtil.TBL_LOC;
							datacvs = "("
									+ ((!x[0].equals("")) ? "'" + x[0] + "'"
											: "null");
							datacvs += ","
									+ ((!x[1].equals("")) ? "'" + x[1] + "'"
											: "null");
							datacvs += ")";
							l = 2;
						}
							break;
						case 2: {// tablaTo=DBUtil.TBL_SET;
							datacvs = "("
									+ ((!x[0].equals("")) ? "'" + x[0] + "'"
											: "null");
							datacvs += ","
									+ ((!x[1].equals("")) ? "'" + x[1] + "'"
											: "null");
							datacvs += ")";
							l = 2;
						}
							break;
						}
						// SI HAY MAS DE l LA DESCRIPCION DEL TABLA TENIA UNA
						// COMA
						if (x.length == l) {
							db.execSQL("DELETE FROM " + tablaTo);
							db.execSQL(qry + datacvs);
						}
						i++;
						publishProgress("" + (int) ((i * 100) / length));
					} catch (Exception ex) {
						fail = "El archivo a importar no correponde a la tabla seleccionada";// "ERROR:"
																								// +
																								// ex.getMessage();
						Log.e("IMPORT CSV", fail);
					}
				}
			} catch (Exception ex) {
				fail = ex.getMessage();
				Log.e("IMPORTCSV", fail);
				// throw ex;
				// } catch (FileNotFoundException fex) {
				// Log.e("Import:", "Error: no es posible hallar el archivo");
			} finally {
				if (db.isOpen())
					db.close();
				if (isBlocking)
					isBlocking = false; // CALL SCREEN UNBLOCK (BORRAR LINEA DE
										// ARRIBA)
				exit();
			}
			return null;

		}

		// DURANTE
		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		// DESPUES
		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

	}

	/** EXPORTACION ASINCRONA A UN ARCHIVO CSV **/
	class ExportCsvFileAsync extends AsyncTask<String, String, String> {
		// ANTES
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_EXPORT_PROGRESS);
		}

		// AHORA
		@Override
		protected String doInBackground(String... destino) {
			if (destino[0].equals("scanner")) {
				destino[0]=DBUtil.PATH_SDCARD+destino[0];
				CreateDir(destino[0]); 
			}
			int i = 0;
			String separator = SEPARATOR_POINTANDCOMMA;
			// EXPORTACION DE INVENTARIO
			String tablaTo = DBUtil.TBL_INV;
			SQLiteDatabase db = (new DBHelper(CustomD.this)).open();
			String filedest = destino[0] + "/";
			String output = getDateNow() + tablaTo + ".csv";
			filedest = (filedest.equals("")) ? DBUtil.PATH_SDCARD : filedest;
			filedest += output;
			File res = new File(filedest);
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileWriter(res));
				Cursor c = db.rawQuery("SELECT * FROM " + tablaTo, null);
				StringBuilder row;
				while (c.moveToNext()) {
					row = new StringBuilder();
					row.append((c.isNull(0)) ? "" : "\"" + c.getString(0)
							+ "\"");
					row.append(separator);
					row.append((c.isNull(1)) ? "" : "\"" + c.getString(1)
							+ "\"");
					row.append(separator);
					row.append((c.isNull(2)) ? "" : "\"" + c.getString(2)
							+ "\"");
					row.append(separator);
					row.append((c.isNull(3)) ? "" : c.getDouble(3));
					pw.println(row.toString());
					i++;
					publishProgress("" + (int) ((i * 100) / c.getCount()));
				}
				pw.flush();
				if (pw != null)
					pw.close();
				// EXPORTACION DE SERIALIZABLES
				tablaTo = DBUtil.TBL_SER;
				filedest = destino[0] + "/";
				output = getDateNow() + tablaTo + ".csv";
				filedest = (filedest.equals("")) ? DBUtil.PATH_SDCARD
						: filedest;
				filedest += output;
				res = new File(filedest);
				pw = null;
				pw = new PrintWriter(new FileWriter(res));
				c = db.rawQuery("SELECT * FROM " + tablaTo, null);
				row = null;
				while (c.moveToNext()) {
					row = new StringBuilder();
					row.append((c.isNull(0)) ? "" : "\"" + c.getString(0)
							+ "\"");
					row.append(separator);
					row.append((c.isNull(1)) ? "" : "\"" + c.getString(1)
							+ "\"");
					row.append(separator);
					row.append((c.isNull(2)) ? "" : "\"" + c.getString(2)
							+ "\"");
					pw.println(row.toString());
					i++;
					publishProgress("" + (int) ((i * 100) / c.getCount()));
				}
				pw.flush();
				if (pw != null)
					pw.close();
			} catch (Exception e) {
				fail = "ERROR:" + e.getMessage();
				Log.e("EXPORTCSV", fail);
			} finally {

				exit();
			}
			return null;
		}

		// DURANTE
		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		// DESPUES
		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_EXPORT_PROGRESS);
		}
	}
}