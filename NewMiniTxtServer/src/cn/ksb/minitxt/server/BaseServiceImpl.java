package cn.ksb.minitxt.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public abstract class BaseServiceImpl<T extends Serializable> implements Service<T> {
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private T data;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				destory();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void init(Socket socket, ObjectInputStream in, ObjectOutputStream out, T data) {
		// TODO Auto-generated method stub
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.data = data;
	}

	@Override
	public void destory() throws IOException {
		// TODO Auto-generated method stub
		if (out != null) {
			out.close();
		}
		if (in != null) {
			in.close();
		}
		if (socket != null) {
			socket.close();
		}
	}

	protected abstract void execute() throws IOException;

}
