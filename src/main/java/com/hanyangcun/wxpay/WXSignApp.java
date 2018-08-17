package com.hanyangcun.wxpay;

import com.hanyangcun.util.HMACSHA256;
import com.hanyangcun.util.Md5Code;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class WXSignApp {

	public static String getSign(Map<String, Object> map, String key) throws Exception {
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			if (entry.getKey().equals("sign") || entry.getValue() == null || "".equals(entry.getValue()))
				it.remove();// 使用迭代器的remove()方法删除元素
		}
		// 通过TreeMap将无序Map转为有序Map
		Map<String, Object> signMap = new TreeMap<String, Object>();
		signMap.putAll(map);
		
		// 将TreeMap转换为加密前字符串
		String befSign = new StringBuilder(signMap.toString().replace(", ", "&").replace("{", "").replace("}", ""))
				.toString();
		String stringSignTemp =befSign+"&key="+key;
		if(map.get("sign_type").equals("HMAC-SHA256")) {
			return HMACSHA256.sha256_HMAC(stringSignTemp, key).toUpperCase();
		}else {
			return Md5Code.MD5Encode(stringSignTemp, "UTF-8").toUpperCase();
		}
	}
}
