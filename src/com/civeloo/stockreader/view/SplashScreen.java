package com.civeloo.stockreader.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.civeloo.stockreader.R;
import com.civeloo.stockreader.data.InventoryDao;
import com.civeloo.stockreader.logic.Inventory;

/** PANTALLA DE CARGA AL INICIO DE LA APLICACION **/
public class SplashScreen extends Activity {
	private final int SPLASH_TIME = 5000;
/** AL INICIO **/
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.splash_layout);
		/*
		 * Handler que quitará el splash screen después del SPLASH_TIME y creará
		 * un intent de la actividad principal.
		 */
		new Handler().postDelayed(new Runnable() {
			// @Override
			public void run() {
				/*
				 * Creamos un Intent que lanzará nuestra Actividad principal (en
				 * nuestro caso Main.java)
				 */
				Intent miIntent = new Intent(SplashScreen.this,
						MainActivity.class);
				SplashScreen.this.startActivity(miIntent);
				SplashScreen.this.finish();
			}
		}, SPLASH_TIME);
		//
		Inventory inventario = new Inventory();
		InventoryDao invService;
		invService = new InventoryDao(this);
		inventario = invService.find();
		inventario.exist();
		//
	}
}