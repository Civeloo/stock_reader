package com.civeloo.stockreader.logic;

public class InventoryList {
	String plu;// Codigo de Producto
	String cantArt;// Cantidad	
	/*String tipCod;// Tipo codigo
	String codLoc;// Codigo de Locacion*/

	//public InventoryList(String plu, String cantArt, String tipCod, String codLoc
	public InventoryList(String plu, String cantArt
			) {
		this.plu = plu;
		this.cantArt = cantArt;
	/*	this.tipCod = tipCod;
		this.codLoc = codLoc;*/
	}

	public String getPlu() {
		return plu;
	}

	public void setPlu(String plu) {
		this.plu = plu;
	}
	
	public String getCantArt() {
		return cantArt;
	}

	public void setCantArt(String cantArt) {
		this.cantArt = cantArt;
	}
	
	/*public String getTipCod() {
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
	}*/

}
