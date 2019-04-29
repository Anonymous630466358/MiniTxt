package cn.ksb.minitxt.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
/**
 * 配置文件的加载及读取
 * @author surface
 *
 */
public class Init {
	private static Properties pros=new Properties();
	
	static {
		FileInputStream fis=null;
		
		try {
			fis=new FileInputStream(new File("config\\server.properties"));
			pros.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String getPro(String key) {
		return pros.getProperty(key);
	}
	
}
