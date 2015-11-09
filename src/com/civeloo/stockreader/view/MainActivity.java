package com.civeloo.stockreader.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.civeloo.stockreader.R;
import com.civeloo.stockreader.tools.Query;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class MainActivity extends Activity{
//public final class MainActivity extends Activity {
	private ImageButton bRead, bImport, bExport, bQuery;

	/** AL INICIO DEL ACTIVITY */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	    // Lookup R.layout.main
	    LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
	      
	    // Create the adView
	    // Please replace MY_BANNER_UNIT_ID with your AdMob Publisher ID
	    AdView adView = new AdView(this, AdSize.BANNER, "a152647ca7cce9d");
	  
	    // Add the adView to it
	    layout.addView(adView);
	     
	    // Initiate a generic request to load it with an ad
	    AdRequest request = new AdRequest();
	    //request.setTesting(true);

	    adView.loadAd(request);

		// componentes del layout
		bRead = (ImageButton) findViewById(R.id.imageButtonRead);
		bImport = (ImageButton) findViewById(R.id.ibImport);
		bExport = (ImageButton) findViewById(R.id.ibExport);
		bQuery = (ImageButton) findViewById(R.id.ibQuery);

		// boton leer codigo de barras
		bRead.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Read.class);
				startActivity(intent);
			}
		});
		// boton importar base de datos
		bImport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Import.class);
				startActivity(intent);
			}
		});
		// boton exportar base de datos
		bExport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Export.class);
				startActivity(intent);
			}
		});
		// INVENTARIO
		bQuery.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Query.class);
                startActivity(intent);
			}
		});
	}

	@Override
	public void onDestroy() {
//		adView.destroy();
		super.onDestroy();
	}

}