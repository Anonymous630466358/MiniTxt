package cn.ksb.minitxt.common.entity;

import java.io.Serializable;

public class Novel implements Serializable {
	private static final long serialVersionUID = -4274051892709465076L;
	private String name;
	private String author;
	private String desc;
	transient private String filename;
	private Classification cls;
	transient private String preview;

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public Classification getCls() {
		return cls;
	}

	public void setCls(Classification cls) {
		this.cls = cls;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
