package com.civeloo.stockreader.logic;

public class Inventory {
	private String plu;// Codigo de Producto
	private String tipCod;// Tipo codigo
	private String codLoc;// Codigo de Locacion
	private Double cantArt;// Cantidad

	/**
	 * USAR LUEGO DE EJECUTAR EL FINDBYPRYMARYKEY
	 * 
	 * @return boolean
	 */

	public boolean exist() {
		return (this.plu != null && this.plu != "") ? true : false;
	}

	public String getPlu() {
		return plu;
	}

	public void setPlu(String plu) {
		this.plu = plu;
	}

	public String getTipCod() {
		return tipCod;
	}

	public void setTipCod(String tipCod) {
		this.tipCod = tipCod;
	}

	public String getCodLoc() {
		return codLoc;
	}

	public void setCodLoc(String codLoc) {
		this.codLoc = codLoc;
	}

	public Double getCantArt() {
		return cantArt;
	}

	public void setCantArt(Double cantArt) {
		this.cantArt = (cantArt == null) ? 0 : cantArt;
	}
}