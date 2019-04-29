package cn.ksb.minitxt.common.entity;

import java.io.Serializable;

public class DataTransfer<T extends Serializable> implements java.io.Serializable {
	private static final long serialVersionUID = 5562410977740619140L;
	private String key;
	private T data;
	private int result;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
