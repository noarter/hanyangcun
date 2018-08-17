package com.hanyangcun.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {

	private static final Logger LOG = LoggerFactory.getLogger(IpUtils.class);
	public static String getIpAddr(HttpServletRequest request) {

		String ip = null;

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
			if (ip != null) {
				ip = ip.split(",")[0].trim();
			}
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-real-ip");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	// 获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
	public static InetAddress getAddress() throws UnknownHostException {
		InetAddress address = InetAddress.getLocalHost();
		return address;
	}

	// 获取的是该网站的ip地址，比如我们所有的请求都通过nginx的，所以这里获取到的其实是nginx服务器的IP地
	public static InetAddress getAddressDomain(String domain) throws UnknownHostException {
		InetAddress address = InetAddress.getByName(domain);
		return address;
	}

	// 获取的是该网站的ip地址，比如我们所有的请求都通过nginx的，所以这里获取到的其实是nginx服务器的IP地
	public static InetAddress[] getAddressALLDomain(String domain) throws UnknownHostException {
		InetAddress[] address = InetAddress.getAllByName(domain);
		return address;
	}

	// 192.168.0.121
	public static String getHostAddress(InetAddress address) {
		String hostAddress = address.getHostAddress();// 192.168.0.121
		return hostAddress;
	}

}
