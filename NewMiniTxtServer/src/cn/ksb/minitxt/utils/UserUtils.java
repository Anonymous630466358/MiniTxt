package cn.ksb.minitxt.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cn.ksb.minitxt.common.constants.UserConstants;
import cn.ksb.minitxt.common.entity.User;
import cn.ksb.minitxt.server.Init;

public class UserUtils {
	private static final String path=Init.getPro("server.config.user");
	private static Map<String , User> map=new HashMap<>();
	private static Document doc=null;
	
	static {
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		DocumentBuilder db=null;
		
		try {
			db=dbf.newDocumentBuilder();
			doc=db.parse(new FileInputStream(path));
			NodeList usernames=doc.getElementsByTagName("username");
			NodeList passwords=doc.getElementsByTagName("password");
			User user=null;
			for(int i=0;i<usernames.getLength();i++) {
				user=new User();
				user.setUsername(usernames.item(i).getFirstChild().getNodeValue().trim());
				user.setPassword(passwords.item(i).getFirstChild().getNodeValue().trim());
				map.put(user.getUsername(), user);
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
	/**
	 * µÇÂ¼
	 * @param user
	 * @return
	 */
	public static int doLogin(User user) {
		User users=null;
		if((users=map.get(user.getUsername()))!=null) {
			if(users.getPassword().equals(user.getPassword())) {
				return UserConstants.SUCCESS;
			}else
				return UserConstants.PASSWORD_INVALID;
		}else {
			return UserConstants.USERNAME_NOT_EXSITS;
		}
	}
	
	public static boolean exists(String username) {
		return (map.get(username)!=null);
	}
	/**
	 * ×¢²á
	 * @param user
	 * @return
	 */
	public static synchronized int doRegister(User user) {
		if (exists(user.getUsername())) {
			return UserConstants.USERNAME_EXSITS;
		}

		Element newUser = doc.createElement("user");
		Element username = doc.createElement("username");
		newUser.appendChild(username);
		Element password = doc.createElement("password");
		newUser.appendChild(password);
		username.appendChild(doc.createTextNode(user.getUsername()));
		password.appendChild(doc.createTextNode(user.getPassword()));
		doc.getDocumentElement().appendChild(newUser);

		OutputStream fos = null;
		try {
			TransformerFactory tff = TransformerFactory.newInstance();
			tff.setAttribute("indent-number", 4);
			Transformer tf = tff.newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "GBK");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			fos = new FileOutputStream(path);
			tf.transform(new DOMSource(doc), new StreamResult(
					new OutputStreamWriter(fos, "GBK")));

			map.put(user.getUsername(), user);
			return UserConstants.SUCCESS;
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		doc.removeChild(newUser);
		map.remove(user.getUsername());
		return UserConstants.ERROR;
	}
}
