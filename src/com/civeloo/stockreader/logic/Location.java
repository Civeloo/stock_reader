package com.civeloo.stockreader.logic;

public class Location {
	private String codigo;
	private String descripcion;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean exist() {
		return (this.descripcion != null && this.descripcion != "") ? true
				: false;
	}
}
