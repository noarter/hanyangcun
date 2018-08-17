package com.hanyangcun.util.xml;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 利用dom4j进行XML编程
 * 
 * @author cuiweibing
 * @since 2007.8.10
 */
public class Dom4jReadExmple {
	/**
	 * 遍历整个XML文件，获取所有节点的值与其属性的值，并放入HashMap中
	 * 
	 * @param filename
	 *            String 待遍历的XML文件（相对路径或者绝对路径）
	 * @param hm
	 *            HashMap
	 *            存放遍历结果，格式：<nodename,nodevalue>或者<nodename+attrname,attrvalue>
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> iterateWholeXML(String xml) throws DocumentException {
		Map<String, String> hm = new ConcurrentHashMap<String, String>();
//		SAXReader saxReader = new SAXReader();
//		Document document = saxReader.read(new File(filename));
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		// 用于记录学生编号的变量
		// 遍历根结点（students）的所有孩子节点（肯定是student节点）
		for (Iterator iter = root.elementIterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			// 获取person节点的age属性的值
			hm.put(element.getName(), element.getStringValue());
		}
		return hm;
	}
}
