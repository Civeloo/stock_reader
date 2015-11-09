package com.civeloo.stockreader.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.civeloo.stockreader.R;
import com.civeloo.stockreader.data.ItemDao;
import com.civeloo.stockreader.logic.Item;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;

public class ItemCRUD extends Activity {
    //private Handler  handler = new Handler();
    Item item = new Item();
    ItemDao itemService;
    EditText etxBarCode, etxPackage, etxName, etxPrice, etxConvertFactor, etxUnitForPack;
    private ImageButton ibRead, ibSave;
    //ImageView ivMessage;
    CheckBox cbFractionally, cbSerializable, cbPackage;
    Boolean vExist = false;
    Boolean vOnLoad = true;
    String vBarCode;

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

        // OPEN CONNECTIONS TO DATABASES
        itemService = new ItemDao(this);

        // Asignamos a cada objeto visual creado a su
        // respectivo elemento de main.xml
        //ivMessage = (ImageView) findViewById(R.id.ivMessage);
        ibRead = (ImageButton) findViewById(R.id.imageButtonRead);
        ibSave = (ImageButton) findViewById(R.id.imageButtonSave);
        etxBarCode = (EditText) findViewById(R.id.editTextBarcode);
        //etxPackage = (EditText) findViewById(R.id.editTextPackage);
        etxName = (EditText) findViewById(R.id.editTextName);
        etxPrice = (EditText) findViewById(R.id.editTextPrice);
        etxConvertFactor = (EditText) findViewById(R.id.editTextConverFactor);
        etxUnitForPack = (EditText) findViewById(R.id.editTextUnitForPack);
        cbFractionally = (CheckBox) findViewById(R.id.checkBoxFractionally);
        cbSerializable = (CheckBox) findViewById(R.id.checkBoxSerializable);
        cbPackage = (CheckBox) findViewById(R.id.checkBoxPackage);
        loadForm();

        // boton escanear
        ibRead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanCode();
            }
        });

        // boton grabar
        ibSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    /**
     * AL CARGAR PANTALLA *
     */
    public void loadForm() {
//		setPermission();
        // Variable get of other activity
        Bundle bundle = getIntent().getExtras();
        //String vBarCode = bundle.getString("vBarCode");
        etxBarCode.setText(bundle.getString("vBarCode"));
    }

    // Save Item
    public void save() {
        if (!etxBarCode.getText().toString().equals("") || !etxPackage.getText().toString().equals("")) {
            ItemDao itemService = new ItemDao(this);
            if (!cbPackage.isChecked()) item.setPlu(etxBarCode.getText().toString()); else item.setCod_pkg(etxBarCode.getText().toString());
            item.setDescr(etxName.getText().toString());
            if (!etxPrice.getText().toString().equals("")) item.setPrecio(Double.parseDouble(etxPrice.getText().toString()));
            if (!etxConvertFactor.getText().toString().equals("")) item.setFactor_conver(Double.parseDouble(etxConvertFactor.getText().toString()));
            if (!etxUnitForPack.getText().toString().equals("")) item.setUnidades_x_pack(Integer.parseInt(etxUnitForPack.getText().toString()));
            item.setFraccionable(cbFractionally.isChecked());
            item.setSerializable(cbSerializable.isChecked());
            if (!vExist) {
                itemService.insert(item);
            } else {
                itemService.update(item);
            }
            exit();
        }
    }

    // Clear screen
    public void clear() {
        etxBarCode.setText("");
        etxBarCode.setText("");
        etxPackage.setText("");
        etxName.setText("");
        etxPrice.setText("");
        etxConvertFactor.setText("");
        etxUnitForPack.setText("");
        cbSerializable.setChecked(false);
        cbFractionally.setChecked(false);
        //ivMessage.setVisibility(View.INVISIBLE);
        vExist = false;
        ScanCode();
    }

    // Read BarCode
    public void ScanCode() {
        // setear el ultimo parametro para activar luz
        //IntentIntegrator.initiateScan(Read.this, R.layout.capture, R.id.viewfinder_view, R.id.preview_view, true);
        IntentIntegrator.initiateScan(this, R.layout.capture, R.id.viewfinder_view, R.id.preview_view, false);
    }

    /** al salir paso los parametros **/
    public void exit() {
        // En la Activity llamada utilizamos esto para devolver el parametro
        Intent resultIntent;
        resultIntent = new Intent();
        resultIntent.putExtra("result", etxBarCode.getText().toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
