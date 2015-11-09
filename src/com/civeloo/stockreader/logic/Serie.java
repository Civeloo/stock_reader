package com.civeloo.stockreader.logic;

public class Serie {
	private String plu;// Codigo de Producto
	private String codLoc;// Codigo de Locacion
	private String serial;// Serializables

	/**
	 * USAR LUEGO DE EJECUTAR EL FINDBYPRYMARYKEY
	 * 
	 * @return boolean
	 */

	public boolean exist() {
		return (this.plu != null && this.plu != "" && this.codLoc != null && this.codLoc != "" && this.serial != null && this.serial != "") ? true : false;
	}

	public String getPlu() {
		return plu;
	}

	public void setPlu(String plu) {
		this.plu = plu;
	}

	public String getCodLoc() {
		return codLoc;
	}

	public void setCodLoc(String codLoc) {
		this.codLoc = codLoc;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

}