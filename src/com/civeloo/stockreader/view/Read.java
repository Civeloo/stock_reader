package com.civeloo.stockreader.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.civeloo.stockreader.R;
import com.civeloo.stockreader.data.InventoryDao;
import com.civeloo.stockreader.data.ItemDao;
import com.civeloo.stockreader.data.LocationDao;
import com.civeloo.stockreader.data.SerieDao;
import com.civeloo.stockreader.data.SettingDao;
import com.civeloo.stockreader.logic.Inventory;
import com.civeloo.stockreader.logic.Item;
import com.civeloo.stockreader.logic.Location;
import com.civeloo.stockreader.logic.Serie;
import com.civeloo.stockreader.logic.Setting;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

public class Read extends Activity {
	// VARIABLES
	private Handler  handler = new Handler();
	Inventory inventory = new Inventory();
	InventoryDao invService;
	Serie serie = new Serie();
	SerieDao serService;
	Item item = new Item();
	ItemDao iteService;
	Location location = new Location();
	LocationDao locService;
	EditText etxCode, etxCount;
	TextView lbHistoryCountValue, tvDescr, tvUnidades_x_pack, tvPrecio, sUnit_for_pack;
	private ImageButton btSave;//, btClear, btUpdate;
	private ImageButton btIncrease, btDecrease, ibRead, ibQuery;
	ImageView ivMessage;
	CheckBox cbSum;
	String vLastCode, vLocation;
	Spinner spinner;
	ArrayAdapter<String> aaAdapter;
	String[] asItems;
	Boolean vExist, vItemExist=false, vOnLoad=true;


	/** INICIO */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read);

		// OPEN CONNECTIONS TO DATABASES
		invService = new InventoryDao(this);
		iteService = new ItemDao(this);
		locService = new LocationDao(this);

		// Asignamos a cada objeto visual creado a su
		// respectivo elemento de main.xml
		tvDescr = (TextView) findViewById(R.id.tvDescr);
		tvUnidades_x_pack = (TextView) findViewById(R.id.tvUnidades_x_pack);
		tvPrecio = (TextView) findViewById(R.id.tvPrecio);
		lbHistoryCountValue = (TextView) findViewById(R.id.tvHistoryCountValue);
		ivMessage = (ImageView) findViewById(R.id.ivMessage);
		ibQuery = (ImageButton) findViewById(R.id.ibQuery);
		ibRead = (ImageButton) findViewById(R.id.imageButtonRead);
		btSave = (ImageButton) findViewById(R.id.bSave);
		//btClear = (ImageButton) findViewById(R.id.bClear);
		//btUpdate = (ImageButton) findViewById(R.id.bUpdate);
		btIncrease = (ImageButton) findViewById(R.id.bIncrease);
		btDecrease = (ImageButton) findViewById(R.id.bDecrease);
		etxCode = (EditText) findViewById(R.id.etCode);
		etxCount = (EditText) findViewById(R.id.etCount);
		vLocation = "1";
		spinner = (Spinner) findViewById(R.id.spLocation);
		cbSum =(CheckBox) findViewById(R.id.checkBoxSum);
		
		loadForm();		

		// Seleccionar Localidad
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			// @Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				vLocation = String.valueOf(position + 1);
				if (!vOnLoad) clear();
			}

			// @Override
			public void onNothingSelected(AdapterView<?> arg0) {
				vLocation = "1";
			}
		});

		// boton escanear
		ibRead.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				vOnLoad=false;
				clear();
			}
		});

		// boton grabar
		btSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				save(cbSum.isChecked());
			}
		});

		// limpiar
		/*btClear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			 clear();
			}
		});

		// reemplazar
		btUpdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				save(true);
			}
		});*/

		// incrementar cantidad
		btIncrease.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				etxCount.setText(String.valueOf(Integer.parseInt(etxCount
						.getText().toString()) + 1));
			}
		});

		// disminuir cantidad
		btDecrease.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				etxCount.setText(String.valueOf(Integer.parseInt(etxCount
						.getText().toString()) - 1));
				if (Integer.parseInt(etxCount.getText().toString()) < 0)
					etxCount.setText("0");
			}
		});

		// boton leer
		ibQuery.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vOnLoad=false;
				readCode(etxCode.getText().toString());
			}
		});	
	}

	/** AL CARGAR PANTALLA **/
	public void loadForm() {
		// Creamos el cursor
		Cursor c = locService.getDataToSpinner();
		if (c.getCount() > 0) {
			// Creamos la lista
			asItems = new String[c.getCount()];
			int i = 0;
			while (c.moveToNext()) {
				asItems[i] = new String(c.getString(1));
				i++;
			}
		} else {
			asItems = new String[1];
			asItems[0] = this.getString(R.string.sWarehouse);
		}
		c.close();
		aaAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, asItems);
		aaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(aaAdapter);
		setPermission();
		if (!vOnLoad) clear(); else ScanCode();
	}

	// SETEAR LOS PERMISOS Y MOSTRAT U OCULTAR OBJETOS
	public void setPermission() {
		//tvPrecio.setVisibility(View.INVISIBLE);
		try {
			Setting setting = new Setting();
			SettingDao setService;
			setService = new SettingDao(this);
			setting = setService.find();
			//if (setting.getPermission() != null && setting.getPermission().equals(1)) { tvPrecio.setVisibility(View.VISIBLE); }
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// Processing the obtained barcode
	public void readCode(String codigoScaneado) {
        vExist=false;
		inventory = invService.findByPrimaryKey(etxCode.getText()
				.toString(), vLocation);
		if (inventory.exist()) {
			lbHistoryCountValue.setText(inventory.getCantArt().toString());
			vExist=true;
		} else  lbHistoryCountValue.setText("0");

		//etxCode.setText(codigoScaneado);
		item = iteService.findByPrimaryKey(codigoScaneado);
		if (item.exist()) {
            vItemExist=true;
			if (item.getPlu() != null
					&& item.getPlu().equals(codigoScaneado)) {// es plu
				ivMessage.setVisibility(View.INVISIBLE);
				if (vLastCode != codigoScaneado) {
					tvDescr.setText(this.getString(R.string.sItem)+": "+ item.getDescr());
					tvUnidades_x_pack.setText(this.getString(R.string.sUnit_for_pack)+": "+String.valueOf(item.getUnidades_x_pack()));
					tvPrecio.setText(this.getString(R.string.sPrice)+": $" + String.valueOf(item.getPrecio()));
				} else {
					ivMessage.setVisibility(View.VISIBLE);
					tvDescr.setText("Id exist " + item.getDescr());
					tvUnidades_x_pack.setText(this.getString(R.string.sUnit_for_pack)+": "+String.valueOf(item.getUnidades_x_pack()));
					tvPrecio.setText(this.getString(R.string.sPrice)+": $" + String.valueOf(item.getPrecio()));
				}
			} else {// es codpkg
				ivMessage.setVisibility(View.INVISIBLE);
				if (vLastCode != codigoScaneado) {
					tvDescr.setText(this.getString(R.string.sItem)+": " + item.getDescr());
					tvUnidades_x_pack.setText(this.getString(R.string.sUnit_for_pack)+": "+String.valueOf(item.getUnidades_x_pack()));
					tvPrecio.setText(this.getString(R.string.sPrice)+": $"+ String.valueOf(item.getPrecio()));
				} else {
					ivMessage.setVisibility(View.VISIBLE);
					tvDescr.setText(this.getString(R.string.Id_exist) +this.getString(R.string.sItem)+ ": "
							+ item.getDescr());
					tvUnidades_x_pack.setText(this.getString(R.string.sUnit_for_pack)+": "+String.valueOf(item.getUnidades_x_pack()));
					tvPrecio.setText(this.getString(R.string.sPrice)+": $"
							+ String.valueOf(item.getPrecio()));
				}
			}
		} else {
			ivMessage.setVisibility(View.VISIBLE);
			tvDescr.setText(this.getString(R.string.Id_Not_Exist));
            vItemExist=false;
		}
		// ULTIMO CODIGO LEIDO
		vLastCode = etxCode.getText().toString();
		// SI ES SERIALIZABLE ABRIR PANTALLA DE CARGA
		if (item.isSerializable()) {
			Intent i = new Intent(this, ReadSerial.class);
			i.putExtra("key", etxCode.getText().toString());
			startActivityForResult(i, 1);
		}
        if (!vItemExist) {
            wantToAddItem(this.getString(R.string.sThe_item_does_not_exist));
        }
	}

	// Save Inventory
	public void save(Boolean sum) {
		if (!etxCode.getText().toString().equals("")&&!item.isSerializable()) {
			Double c1, c2, cant;
			InventoryDao invService = new InventoryDao(this);
			inventory.setPlu( (item.getPlu() != null && item.getPlu() != "") ? item.getPlu() : etxCode.getText().toString() );
			inventory.setTipCod( (item.getCod_pkg() != null && item.getCod_pkg() != "") ? item.getCod_pkg() : "" );//(item.getCod_pkg());
			inventory.setCodLoc(vLocation);
			c1 = Double.parseDouble(lbHistoryCountValue.getText().toString());
			c2 = Double.parseDouble(etxCount.getText().toString());
			if (item.getCod_pkg() != null
					&& item.getCod_pkg().equals(
							etxCode.getText().toString()))
				c2 = c2 * item.getUnidades_x_pack();
			if (item.isFraccionable())
				c2 = c2 * item.getFactor_conver();
			if ((c1 != null && c1 != 0) && sum) cant = c1 + c2; else  cant = c2;
			inventory.setCantArt(cant);
			//if (c1 == null || c1 == 0) {
			if (vExist) invService.update(inventory); else invService.insert(inventory);
			clear();
		}
	}

	// Save Serial
	public void saveSerial(String[] list) {
		if (list.length > 0) {
			SerieDao serService = new SerieDao(this);
			Double c1, c2, cant;
			InventoryDao invService = new InventoryDao(this);
			inventory.setPlu((item.getPlu() != null && item.getPlu() != "") ? item.getPlu() : etxCode.getText().toString());
			inventory.setTipCod(item.getCod_pkg());
			inventory.setCodLoc(vLocation);
			c1 = Double.parseDouble(lbHistoryCountValue.getText().toString());
			c2 = (double) (list.length);
			if (item.getCod_pkg() != null
					&& item.getCod_pkg().equals(
							etxCode.getText().toString()))
				c2 = c2 * item.getUnidades_x_pack();
			if (item.isFraccionable())
				c2 = c2 * item.getFactor_conver();
			if ((c1 != null && c1 != 0)) {
				cant = c1 + c2;
			} else {
				cant = c2;
			}
			inventory.setCantArt(cant);
			//if (c1 == null || c1 == 0) {
			if (!vExist) {
				invService.insert(inventory);
				for (int i = 0; i < list.length; i++) {
					serie.setCodLoc(vLocation);
					serie.setPlu(etxCode.getText().toString());
					serie.setSerial(list[i]);
					serService.insert(serie);
				}
			} else {
				//graba solo los que faltan y actualiza la cantidad 
				c2=(double) 0;
				for (int i = 0; i < list.length; i++) {
					//serializable=new Serializable();
					serie=serService.findByPrimaryKey(etxCode.getText().toString(), vLocation, list[i]);
					if(!serie.exist()){
						serie.setCodLoc(vLocation);
						serie.setPlu(etxCode.getText().toString());
						serie.setSerial(list[i]);
						serService.insert(serie);
						c2++;
					}
				}
				inventory.setCantArt(c1+c2);	
				invService.update(inventory);
			}
		} 
		clear();
	}

	// Clear Screen
	public void clear() {
		etxCode.setText("");
		etxCount.setText("1");
		tvDescr.setText("");
		tvUnidades_x_pack.setText("");
		tvPrecio.setText("");
		lbHistoryCountValue.setText("0");
		ivMessage.setVisibility(View.INVISIBLE);
		// lastCode = "";
		cbSum.setChecked(true);
		ScanCode();
	}

	// Read BarCode
	public void ScanCode() {
		// setear el ultimo parametro para activar luz
		//IntentIntegrator.initiateScan(Read.this, R.layout.capture, R.id.viewfinder_view, R.id.preview_view, true);
		IntentIntegrator.initiateScan(Read.this, R.layout.capture,
                R.id.viewfinder_view, R.id.preview_view, false);
	}

	/** TRAER PARAMETROS CUANDO VUELVE DE OTRA ACTIVITY **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                final String result = scanResult.getContents();
                if (result != null) {
                    handler.post(new Runnable() {
                        //@Override
                        public void run() {
                        	etxCode.setText(result);
    						readCode(result);
                        }
                    });
                }
                break;
		case (1): {
			if (resultCode == Activity.RESULT_OK) {
				if (data.getStringExtra("operation").equals("2"))
					saveSerial(data.getStringArrayExtra("result"));
			}
			break;
		}
            case (2): {
                if (resultCode == Activity.RESULT_OK) {
                    /*if (!data.getStringExtra("ItemCRUD").equals("")){
                        //etxCode.setText(data.getStringExtra("ItemCRUD"));
                        readCode(etxCode.getText().toString());
                    }*/
                    readCode(etxCode.getText().toString());
                }
                break;
            }
		}
	}
    // add new item
    public void addItem() {
        Intent i = new Intent(this, ItemCRUD.class );
        i.putExtra("vBarCode", etxCode.getText().toString());
        startActivityForResult(i, 2);
    }
    /** Asked if you want add the item
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                final String result = scanResult.getContents();
                if (result != null) {
                    handler.post(new Runnable() {
                        //@Override
                        public void run() {
                            etxBarCode.setText(result);
                        }
                    });
                }
                break;
            case (1): {
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getStringExtra("operation").equals("2"))
                        ;
                }
                break;
            }
        }
    }+/
    /** Ask for add item **/
    public void wantToAddItem(final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(title)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.sYes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            addItem();
                            //readCode(etxCode.getText().toString());
                            }
                        })
                .setNegativeButton(getString(R.string.sNot),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}