package com.civeloo.stockreader.logic;

public class Setting {
	private String url;
	private Integer permission;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	public boolean exist() {
		return (this.url != null && this.url != "") ? true : false;
	}
}
