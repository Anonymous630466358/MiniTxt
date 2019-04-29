package cn.ksb.minitxt.server;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ServiceFactory {
	private static String path = Init.getPro("server.config.service");
	private static Map<String, String> map = new HashMap<String, String>();
	private static Document doc;

	static {
		SAXReader sax = new SAXReader();
		try {
			doc = sax.read(new File(path));
			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			for (Element ele : list) {
				String value = ele.getText();
				String key = ele.attribute("key").getText();
				map.put(key, value);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Service<Serializable> getClass(String key) {
		Service<Serializable> service = null;
		String className = map.get(key);
		if (className == null) {
			throw new RuntimeException("无效的关键字！");
		}
		try {
			service = (Service<Serializable>) Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return service;
	}

}
