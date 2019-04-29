package cn.ksb.minitxt.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import cn.ksb.minitxt.common.entity.DataTransfer;

public class ServerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ServerMain().startServer();
	}
	
	public <E extends Serializable> void startServer() {
		try {
			ServerSocket sockets=new ServerSocket(Integer.parseInt(Init.getPro("socket.server.port")));
			System.out.println("服务器启动成功");
			
			while(true) {
				Socket socket=sockets.accept();
				System.out.println(socket.getInetAddress()+"获得了连接");
				ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
				
				@SuppressWarnings("unchecked")
				DataTransfer<E> data=(DataTransfer<E>) in.readObject();
				String key=data.getKey();
				System.out.println("客户端："+key);
				Service<E> service=(Service<E>) ServiceFactory.getClass(key);
				service.init(socket, in, out, data.getData());
				new Thread(service).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
