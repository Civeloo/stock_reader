package com.civeloo.stockreader.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/** CLASE FUNCIONES REUTILIZABLES **/
public class Util {

	/** CALCULO DE UNIDADES DE PAQUETES **/
	public static double pkgToUnidad(Item a, double cantidad) {
		double res = 0;
		if (a != null && a.getCod_pkg() != null) {
			res = a.getUnidades_x_pack() * cantidad;
		}
		return res;
	}

	/** CALULO DE UNIDADES DE PAQUETES INERTIDO **/
	public static double unidadToPkg(Item a, double cantidad) {
		double res = 0;
		if (cantidad > 0 && a != null && a.getUnidades_x_pack() > 0
				&& a.getCod_pkg() != null) {
			res = a.getUnidades_x_pack() / cantidad;
		}
		return res;
	}

	/** FUNCION PARA COPIAR UN ARCHIVO **/
	public static void copyFile(File src, File dst) throws IOException {
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

}