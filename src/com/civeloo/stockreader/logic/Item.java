package com.civeloo.stockreader.logic;

public class Item {
	private String plu;
	private String cod_pkg;
	private String descr;
	private double precio;
	private double existencia;
	private double factor_conver;
	private int unidades_x_pack;
	private boolean fraccionable;
	private boolean serializable;

	public boolean exist() {
		return (this.descr != null && this.descr != "") ? true : false;
	}

	public String getPlu() {
		return plu;
	}

	public void setPlu(String plu) {
		this.plu = plu;
	}

	public String getCod_pkg() {
		return cod_pkg;
	}

	public void setCod_pkg(String codPkg) {
		cod_pkg = codPkg;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getExistencia() {
		return existencia;
	}

	public void setExistencia(double existencia) {
		this.existencia = existencia;
	}

	public double getFactor_conver() {
		return factor_conver;
	}

	public void setFactor_conver(double factorConver) {
		factor_conver = factorConver;
	}

	public int getUnidades_x_pack() {
		return unidades_x_pack;
	}

	public void setUnidades_x_pack(int unidadesXPack) {
		unidades_x_pack = unidadesXPack;
	}

	public boolean isFraccionable() {
		return fraccionable;
	}

	public void setFraccionable(boolean fraccionable) {
		this.fraccionable = fraccionable;
	}

	public boolean isSerializable() {
		return serializable;
	}

	public void setSerializable(boolean serializable) {
		this.serializable = serializable;
	}

}