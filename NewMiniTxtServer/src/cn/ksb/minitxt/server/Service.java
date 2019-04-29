package cn.ksb.minitxt.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public interface Service<T extends Serializable> extends Runnable {
	public void init(Socket socket, ObjectInputStream in, ObjectOutputStream out, T data);
	public void destory() throws IOException;
}
