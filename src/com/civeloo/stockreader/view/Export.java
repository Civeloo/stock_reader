package com.civeloo.stockreader.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.civeloo.stockreader.R;
import com.civeloo.stockreader.tools.FileSelect;

public class Export extends Activity {
	// variables
	EditText etPath;
	ImageButton bExport;
	TextView tvMsg;
	//String[] items = new String[] { this.getString(R.string.sSd), this.getString(R.string.sCsv) };
	Integer selectedItem;
	Spinner spinner;
	ArrayAdapter<String> adapter;
	AlertDialog alert;
	boolean onLoad = true;

	/** AL INICIO */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exp);
		// Asignamos a cada objeto visual creado a su
		// respectivo elemento de main.xml
		tvMsg = (TextView) findViewById(R.id.tvMsg);
		etPath = (EditText) findViewById(R.id.etPathExport);
		bExport = (ImageButton) findViewById(R.id.bExport);
		// SPINNER DE SELECCION DE DESTINO DE EXPORTACION
		spinner = (Spinner) findViewById(R.id.spExport);
		String[] items = new String[] { this.getString(R.string.sSd), this.getString(R.string.sCsv) };
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		// MENSAJE
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(this.getString(R.string.sExport_completed_successfully))
				.setCancelable(false)
				.setPositiveButton(this.getString(R.string.sAccept),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Export.this.finish();
							}
						});
		alert = builder.create();

		loadForm();
		// AL HACER CLICK EN EL CAMPO DE TEXTO
		etPath.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Llamada a la clase que tiene que devolver un valor
				switch (selectedItem) {
				case 0:
					openFileExplorer(4);
					break;
				case 1:
					openFileExplorer(5);
					break;
				}
			}
		});
		// AL HACER CLICK EN EL SPINNER DE DESTINO DE EXPORTACION
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			// @Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				selectedItem = position;
				etPath.setText("");
				if (!onLoad) {
					switch (selectedItem) {
					case 0:
						etPath.setText("");//openFileExplorer(4);
						break;
					case 1:
						etPath.setText("");//openFileExplorer(5);
						break;
					}
				} else
					onLoad = false;
			}

			// @Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		// BOTON EXPORTAR
		bExport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!etPath.getText().equals("")) {
					switch (selectedItem) {
					case 0:
						exportSdCard();
						break;
					case 1:
						exportCsv();
						break;
					default:
						Toast.makeText(Export.this, 
								getString(R.string.sEnter_an_address_to_Export),
								Toast.LENGTH_LONG).show();
						break;
					}
				}
			}
		});
	}
	
	/** exportSdCard **/
	public void exportSdCard() {
		startProgressDialog(4);
	}

	/** exportCsv **/
	public void exportCsv() {
		startProgressDialog(5);
	}

	/** ABRE LA PANTALLA DE PROGRESO **/
	public void startProgressDialog(Integer operation) {
		if (etPath.getText() != null) {
			// Llamada a la clase que tiene que devolver un valor
			Intent i = new Intent(this, CustomD.class);
			i.putExtra("operation", operation);
			i.putExtra("url", etPath.getText().toString());
			startActivityForResult(i, 1);
		}
	}

	/** ABRIR EL EXPLORADOR DE ARCHIVOS **/
	public void openFileExplorer(Integer o) {
		// abrir el explorador de archivos
		Intent i = new Intent(this, FileSelect.class);
		i.putExtra("operation", o);
		startActivityForResult(i, 1);
	}

	/** devolucion de variables **/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (1): {
			if (resultCode == Activity.RESULT_OK) {
				if (data.getStringExtra("operation").equals("2"))
					onGetPath(data.getStringExtra("result"));
				else
					stopProgressDialog(data.getStringExtra("result"));
			}
			break;
		}
		}
	}

	/** AL FINALIZAR PROGRESO **/
	public void stopProgressDialog(String txt) {
		if (txt.equals(""))
			alert.show();
		else
			tvMsg.setText(txt);
	}

	/** TRAER LA DIRECCION DEL ARCHIVO **/
	public void onGetPath(String txt) {
		if (!txt.equals(""))
			etPath.setText(txt);
	}

	/** LLENAR EL SPINER DE ORIGEN **/
	public void loadForm() {
		etPath.setText("");
	}
}