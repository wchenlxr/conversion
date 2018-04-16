//package com.adtech.cn.utils;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
///**
// * 读取Properties综合类
// *
// * @author Administrator
// *
// */
//public class PropertiesUtil {
//	// 配置文件的路径
//	private String configPath = "sysconfig.properties";
//
//	/**
//	 * 配置文件对象
//	 */
//	private Properties props = null;
//
//	public PropertiesUtil() {
//		try {
//			InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(configPath);
//			props = new Properties();
//			props.load(in);
//			// 关闭资源
//			in.close();
//		} catch (IOException e) {
//			System.out.println("属性文件读取错误" + e.getMessage());
//		}
//	}
//
//	/**
//	 * 根据key值读取配置的值
//	 *
//	 * @param key
//	 *            key值
//	 * @return key 键对应的值
//	 * @throws IOException
//	 */
//	public String readValue(String key) throws IOException {
//		return props.getProperty(key);
//	}
//
//	/**
//	 * 读取properties的全部信息
//	 *
//	 * @throws FileNotFoundException
//	 *             配置文件没有找到
//	 * @throws IOException
//	 *             关闭资源文件，或者加载配置文件错误
//	 *
//	 */
//	public Map<String, String> readAllProperties() throws FileNotFoundException, IOException {
//		// 保存所有的键值
//		Map<String, String> map = new HashMap<String, String>();
//		Enumeration en = props.propertyNames();
//		while (en.hasMoreElements()) {
//			String key = (String) en.nextElement();
//			String Property = props.getProperty(key);
//			map.put(key, Property);
//		}
//		return map;
//	}
//
//	/**
//	 * 设置某个key的值,并保存至文件。
//	 *
//	 * @param key
//	 *            key值
//	 * @return key 键对应的值
//	 * @throws IOException
//	 */
//	public void setValue(String key, String value) throws IOException {
//		Properties prop = new Properties();
//		InputStream fis = new FileInputStream(this.configPath);
//		prop.load(fis);
//		OutputStream fos = new FileOutputStream(this.configPath);
//		prop.setProperty(key, value);
//		prop.store(fos, "last update");
//		// 关闭文件
//		fis.close();
//		fos.close();
//	}
//
//}
