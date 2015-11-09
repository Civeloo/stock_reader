package com.civeloo.stockreader.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.civeloo.stockreader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSelect extends ListActivity {
	private List<String> item = null;
	private List<String> path = null;
	private String root = "/mnt/sdcard/";
	private TextView myPath;
	ImageButton select;
	public Integer operation;
	String ext = "db";
	String result = "";
	String folder = "Por favor seleccione una carpeta";
	Boolean dir = false;

	/** AL INICIO */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_explorer);
		// traer datos del formulario llamador
		Bundle extras = getIntent().getExtras();
		//CONFIGURA LA EXTENCION DE ACUEDO AL PARAMETRO TRAIDO
		if (extras != null) {
			operation = extras.getInt("operation");
			switch (operation) {
			case 2:
				ext = "db";
				break;
			case 3:
				ext = "csv";
				break;
			default:
				dir = (operation == 4 || operation == 5) ? true : false;
				break;
			}
		}
		myPath = (TextView) findViewById(R.id.path);
		select = (ImageButton) findViewById(R.id.ibSave);
		getDir(root);

		// boton seleccionar directorio
		select.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//SI LA OPERACION REQUIERE DIRECTORIO LO PASA
				if (dir)
					alert(myPath.getText().toString());
			}
		});
	}
	/** MENSAJE EN PANTALLA**/
	public void alert(final String title) {
		new AlertDialog.Builder(this)
				.setIcon(R.drawable.icon)
				.setTitle(title)
				.setPositiveButton("Aceptar",
						new DialogInterface.OnClickListener() {
							// @Override
							public void onClick(DialogInterface dialog,
									int which) {
								// enviar directorio al salir
								result = myPath.getText().toString();
								if (!title.equals(folder))
									exit();
							}
						}).show();
	}
	/** LLENO LA LISTA **/
	private void getDir(String dirPath) {
		myPath.setText(dirPath);
		item = new ArrayList<String>();
		path = new ArrayList<String>();
		File f = new File(dirPath);
		File[] files = f.listFiles();
		if (!dirPath.equals(root)) {
			item.add(root);
			path.add(root);
			item.add("../");
			path.add(f.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			path.add(file.getPath());
			if (file.isDirectory())
				item.add(file.getName() + "/");
			else
				item.add(file.getName());
		}
		ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
				R.layout.row, item);
		setListAdapter(fileList);
	}
	/** AL HACER CLICK EN UN ITEM DE LA LISTA **/
	@Override
	protected void onListItemClick(ListView l, View v, final int position,
			long id) {
		final File file = new File(path.get(position));
		if (file.isDirectory()) {
			if (file.canRead())
				getDir(path.get(position));
			else {
				new AlertDialog.Builder(this)
						.setIcon(R.drawable.icon)
						.setTitle(
								"(" + file.getName()
										+ ") La carpeta no se puede leer!")
						.setPositiveButton("Aceptar",
								new DialogInterface.OnClickListener() {
									// @Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								}).show();
			}
		} else {
			//SI NO QUIERO OBTENER EL DIRECTORIO
			if (!dir) {
				// File file = new File(fileList.get(position));
				Uri selectedUri = Uri.fromFile(file);
				String fileExtension = MimeTypeMap
						.getFileExtensionFromUrl(selectedUri.toString());
				//CHEQUEO QUE CORRESPONA LA EXTENCION DEL ARCHIVO
				if (fileExtension.equals(ext)) {
					new AlertDialog.Builder(this)
							.setIcon(R.drawable.icon)
							.setTitle(file.getName())
							.setPositiveButton("Aceptar",
									new DialogInterface.OnClickListener() {
										// @Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// enviar directorio de archivo al
											// salir
											result = path.get(position);
											exit();
										}
									}).show();
				} 
				//MUESTRO MENSAJE DE ERROR CUANDO NO CORRESPONDE LA EXTENSION
				else {
					new AlertDialog.Builder(this)
							.setIcon(R.drawable.icon)
							.setTitle(
									"[" + file.getName()
											+ "] La extensión debe ser ." + ext)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										// @Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
										}
									}).show();
				}
			} else
				alert(folder);
		}
	}
	/** al salir paso los parametros **/
	public void exit() {
		// En la Activity llamada utilizamos esto para devolver el parametro
		Intent resultIntent;
		resultIntent = new Intent();
		resultIntent.putExtra("result", result);
		resultIntent.putExtra("operation", "2");
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}