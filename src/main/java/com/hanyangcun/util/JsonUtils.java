package com.hanyangcun.util;


import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
/**
 * Json 工具类
 * @author yinjihuan
 *
 */
public class JsonUtils {
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static String toString(Object obj){
		return toJson(obj);
	}
	
	public static String toJson(Object obj){
		try{
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			SimpleModule simpleModule = new SimpleModule();
			simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
			simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
			mapper.registerModule(simpleModule);
			StringWriter writer = new StringWriter();
			mapper.writeValue(writer, obj);
			return writer.toString();
		}catch(Exception e){
			throw new RuntimeException("序列化对象【"+obj+"】时出错", e);
		}
	}
	
	public static <T> T toBean(Class<T> entityClass, String jsonString){
		try {
			return mapper.readValue(jsonString, entityClass);
		} catch (Exception e) {
			throw new RuntimeException("JSON【"+jsonString+"】转对象时出错", e);
		}
	}
	
	/**
	 * 
	 */
	public static JSONObject getJsonObject(String jsonString) {
		JSONObject json = JSON.parseObject(jsonString);
//		JsonObject jsonObject = jsonParser.parse(jsonString).getAsJsonObject();
		return json;
	}
	/**
	 * 用于对象通过其他工具已转为JSON的字符形式，这里不需要再加上引号
	 * @param obj
	 * @param isObject
	 */
	public static String getJsonSuccess(String obj, boolean isObject){
		String jsonString = null;
		if(obj == null){
			jsonString = "{\"success\":true}";
		}else{
			jsonString = "{\"success\":true,\"data\":"+obj+"}";
		}
		return jsonString;
	}
	
	public static String getJsonSuccess(Object obj){
		return getJsonSuccess(obj, null);
	}
	
	public static String getJsonSuccess(Object obj, String message) {
		if(obj == null){
			return "{\"success\":true,\"message\":\""+message+"\"}";
		}else{
			try{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", true);
				return "{\"success\":true,"+toString(obj)+",\"message\":\""+message+"\"}";
			}catch(Exception e){
				throw new RuntimeException("序列化对象【"+obj+"】时出错", e);
			}
		}
	}
	
	public static String getJsonError(Object obj){
		return getJsonError(obj, null);
	}
	
	public static String getJsonError(Object obj, String message) {
		if(obj == null){
			return "{\"success\":false,\"message\":\""+message+"\"}";
		}else{
			try{
				obj = parseIfException(obj);
				return "{\"success\":false,\"data\":"+toString(obj)+",\"message\":\""+message+"\"}";
			}catch(Exception e){
				throw new RuntimeException("序列化对象【"+obj+"】时出错", e);
			}
		}
	}
	
	/**
     * 将json转化为实体POJO
     * @param jsonStr
     * @param obj
     * @return
     */
    public static<T> Object JSONToObj(String jsonStr,Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr,
                    obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
     
    /**
     * 将实体POJO转化为JSON
     * @param obj
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static<T> JSONObject objectToJson(T obj) throws JSONException, IOException {
        ObjectMapper mapper = new ObjectMapper(); 
        // Convert object to JSON string 
        String jsonStr = "";
        try {
             jsonStr =  mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw e;
        }
        return getJsonObject(jsonStr);
    }
	
	public static Object parseIfException(Object obj){
		if(obj instanceof Exception){
			return getErrorMessage((Exception) obj, null);
		}
		return obj;
	}
	
	public static String getErrorMessage(Exception e, String defaultMessage){
		return defaultMessage != null ? defaultMessage : null;
	}
	
	public static ObjectMapper getMapper() {
		return mapper;
	}
}
