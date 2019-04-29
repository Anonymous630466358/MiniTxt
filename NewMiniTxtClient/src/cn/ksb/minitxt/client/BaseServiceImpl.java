package cn.ksb.minitxt.client;

public abstract class BaseServiceImpl<T extends java.io.Serializable> implements Service<T> {
	protected String OUTPUT_TEXT_SERVERERROR = "服务器故障，请重试！";
	protected String OUTPUT_TEXT_ERROR = "系统存在错误，服务终止！";
	protected String OUTPUT_TEXT_INVALIDINPUT = "你的输入有误，请重新输入！";
	private T inputData;
	
	@Override
	public void setInputData(T inputData) {
		this.inputData = inputData;
	}
	
	public T getInputData() {
		return inputData;
	}

}
