package cn.ksb.minitxt.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ServiceFactory {
	private static final String path=Init.getProperty("client.config.service");
	
	private static Map<String, String> map=new HashMap<String, String>();
	
	static {
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		DocumentBuilder db=null;
		Document doc=null;
		
		try {
			db=dbf.newDocumentBuilder();
			doc=db.parse(new FileInputStream(path));
			
			NodeList serverNode=doc.getElementsByTagName("service");
			for(int i=0;i<serverNode.getLength();i++) {
				Node node=serverNode.item(i);
				map.put(node.getAttributes().getNamedItem("key").getNodeValue().trim(),
						node.getFirstChild().getNodeValue().trim());
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static <T extends Serializable> Service<T> getService(String key) {
		String className=map.get(key);
		if(className==null) {
			throw new RuntimeException("无效的关键字");
		}
		try {
			return (Service<T>)Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
