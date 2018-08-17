package com.hanyangcun.util.xml;

import com.alibaba.fastjson.JSONObject;

/**
 * 常用工具类:XML工具
 * 
 * @author Jie
 * @date 2015-2-14
 */
public class XMLUtils {
	/**
	 * 将xml转换成JSON
	 * 
	 * @param xml
	 *            xml文件
	 * @return
	 * @throws DocumentException
	 * @author Jie
	 * @date 2015年11月18日
	 */
	/**  
	    * JsonObject转换成xml  
	    *   
	    * @param json  
	    * @return  
	    */  
	    public static String JsonBean2XmlString(JSONObject json){  
	        System.out.println("JsonBean转换成xml:");  
	        System.out.println("json:"+json);  
	        StringBuffer sb = new StringBuffer("<xml>");  
	        for(Object key : json.keySet()){  
	            sb.append("<").append(key).append(">"); 
	            sb.append(json.get(key));
	            sb.append("</").append(key).append(">");  
	        }  
	        sb.append("</xml>");
	        return sb.toString();  
	    }
}
