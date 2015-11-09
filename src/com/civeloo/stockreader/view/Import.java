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
import com.civeloo.stockreader.data.ItemDao;
import com.civeloo.stockreader.data.LocationDao;
import com.civeloo.stockreader.data.SettingDao;
import com.civeloo.stockreader.logic.Item;
import com.civeloo.stockreader.logic.Location;
import com.civeloo.stockreader.logic.Setting;
import com.civeloo.stockreader.tools.FileSelect;

public class Import extends Activity {
	/** variable **/
	EditText etPath;
	ImageButton bImport;
	TextView tvMsg;

	Integer selectedItem;
	Integer selectedTable = 0;
	Spinner spinner, spTable;
	ArrayAdapter<String> adapter;
	AlertDialog alert;
	String url;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imp);
		// Asignamos a cada objeto visual creado a su
		// respectivo elemento de main.xml
		tvMsg = (TextView) findViewById(R.id.tvMsg);
		etPath = (EditText) findViewById(R.id.etPathImport);
		bImport = (ImageButton) findViewById(R.id.bImport);
		spinner = (Spinner) findViewById(R.id.spImport);
		spTable = (Spinner) findViewById(R.id.spTable);
		// SPINNER DE SELECCION DE ORIGEN DE IMPORTACION
		String[] items = new String[] { this.getString(R.string.sWeb), this.getString(R.string.sSd), this.getString(R.string.sCsv) };
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		// MENSAJE
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(this.getString(R.string.sImport_completed_successfully))
				.setCancelable(false)
				.setPositiveButton(this.getString(R.string.sAccept),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Import.this.finish();
							}
						});
		alert = builder.create();
		// Spinner tabla a importar x csv
		String[] tabla = new String[] { this.getString(R.string.sItem), this.getString(R.string.sLocation),
				this.getString(R.string.sSetting) };
		ArrayAdapter<String> adapterTable = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, tabla);
		adapterTable
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spTable.setAdapter(adapterTable);

		setUrl();
		loadForm();

		spTable.setVisibility(View.GONE);
		// AL HACER CLICK EN EL CAMPO DE TEXTO
		etPath.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Llamada a la clase que tiene que devolver un valor
				switch (selectedItem) {
				case 0: // openFileExplorer(1); //web
					break;
				case 1:
					openFileExplorer(2); // db
					break;
				case 2:
					openFileExplorer(3); // csv
					break;
				}
			}
		});
		// AL HACER CLICK EN EL SPINNER DE ORIGEN DE IMPORTACION
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			// @Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// items[0] = this.getString(R.string.sWeb);
				selectedItem = position;
				// WEB
				if (position == 0) {
					spTable.setVisibility(View.GONE);
					etPath.setText(url);
				}
				// SD
				else if (position == 1) {
					etPath.setText("");
					spTable.setVisibility(View.GONE);
					openFileExplorer(2);
				}
				// CSV
				else {
					etPath.setText("");
					spTable.setVisibility(View.VISIBLE);
					openFileExplorer(3);
				}
			}

			// @Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		// AL HACER CLICK EN EL SPINNER DE TABLA PARA IMPORTAR X CSV
		spTable.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				selectedTable = position;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		// BOTON IMPORTAR
		bImport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!etPath.getText().equals("")) {
					if (selectedItem == 0)
						importWeb();
					else if (selectedItem == 1)
						importSdCard();
					else if (selectedItem == 2)
						importCsv();
				} else
					Toast.makeText(Import.this,
							getString(R.string.sEnter_an_address_to_Import),
							Toast.LENGTH_LONG).show();
			}
		});
	}

	/** importar de Web **/
	public void importWeb() {
		startProgressDialog(1);
	}

	/** importar de SdCard **/
	public void importSdCard() {
		startProgressDialog(2);
	}

	/** importar de Csv **/
	public void importCsv() {
		startProgressDialog(3);
	}

	/** ABRE LA PANTALLA DE PROGRESO **/
	public void startProgressDialog(Integer operation) {
		if (etPath.getText() != null) {
			// Llamada a la clase que tiene que devolver un valor
			Intent i = new Intent(this, CustomD.class);
			i.putExtra("operation", operation);
			i.putExtra("table", selectedTable);
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
				switch (Integer.parseInt(data.getStringExtra("operation"))) {
				case 1:
					stopProgressDialog(data.getStringExtra("result"));
					break;
				case 2:
					onGetPath(data.getStringExtra("result"));
					break;
				}
			}
			break;
		}
		}
	}

	/** VERIFICAR QUE LA TABLA ESTE BIEN **/
	public Boolean verify() {
		try {
			switch (selectedTable) {
			case 0: {
				Item articulo = new Item();
				ItemDao artService = new ItemDao(this);
				articulo = artService.find();
				if (articulo.exist())
					return true;
				else
					return false;
			}
			case 1: {
				Location locacion = new Location();
				LocationDao locService = new LocationDao(this);
				locacion = locService.find();
				if (locacion.exist())
					return true;
				else
					return false;
			}
			case 2: {
				Setting setting = new Setting();
				SettingDao setService = new SettingDao(this);
				setting = setService.find();
				if (setting.exist())
					return true;
				else
					return false;
			}
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	/** AL FINALIZAR PROGRESO **/
	public void stopProgressDialog(String txt) {
		if (txt.equals("")) {
			if (!verify())
				tvMsg.setText("base de datos defectuosa, intentelo de nuevo");
			else
				alert.show();
		} else
			tvMsg.setText(txt);
	}

	/** TRAER LA DIRECCION DEL ARCHIVO **/
	public void onGetPath(String txt) {
		if (!txt.equals(""))
			etPath.setText(txt);
	}

	/** LLENAR EL SPINER DE ORIGEN **/
	public void loadForm() {
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	/** TRAER URL POR DEFECTO **/
	public void setUrl() {
		try {
			Setting setting = new Setting();
			SettingDao setService;
			setService = new SettingDao(this);
			setting = setService.find();
			if (setting.getUrl() != null && !setting.getUrl().equals(""))
				url = setting.getUrl();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}