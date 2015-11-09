package com.civeloo.stockreader.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.civeloo.stockreader.R;

public class TextImput extends Activity {
	/** variable **/
	EditText etPlu;
	ImageButton ibSave;
	String key;

	/** AL INICIO */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_imput);
		etPlu = (EditText) findViewById(R.id.etPluImput);
		ibSave = (ImageButton) findViewById(R.id.ibSaveImput);
		// traer datos del formulario llamador
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			key = extras.getString("key");
		//BOTON GRABAR
		ibSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exit();
			}
		});

	}
	/** pasar parametros al salir **/
	public void exit() {
		// En la Activity llamada utilizamos esto para devolver el parametro
		Intent resultIntent;
		resultIntent = new Intent();
		resultIntent.putExtra("result", etPlu.getText().toString());
		resultIntent.putExtra("operation", "3");
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}