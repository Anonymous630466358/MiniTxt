package cn.ksb.minitxt.common.entity;

import java.io.Serializable;

public class Classification implements Serializable {
	private static final long serialVersionUID = 4576571035306911757L;
	private String classname;
	transient private String catalog;
	transient private String config;

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}
}
