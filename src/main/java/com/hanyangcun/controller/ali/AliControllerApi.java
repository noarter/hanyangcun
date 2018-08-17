package com.hanyangcun.controller.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.hanyangcun.config.AlipayConfig;
import com.hanyangcun.util.RequestUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


@RequestMapping("/ali")
@Controller
public class AliControllerApi {

	private static final Logger LOG = LoggerFactory.getLogger(AliControllerApi.class);
	private final String URL_PREFIX = "userId_";
	@Autowired
	private AlipayConfig alConf;

	@ResponseBody
	@RequestMapping(value = URL_PREFIX + "get", method = { RequestMethod.POST, RequestMethod.GET })
	public String postJsonData(String auth_code, HttpServletResponse response2, String callbackUrl) throws IOException {
		LOG.info("code {}", auth_code);

		String userId = null;
		try {
			// 3. 利用authCode获得authToken
			AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
			oauthTokenRequest.setCode(auth_code);
			oauthTokenRequest.setGrantType(alConf.getGrant_type());
			AlipayClient alipayClient = new DefaultAlipayClient(alConf.getGatewayUrl(), alConf.getAppId(), alConf.getGetMerchantPrivateKey(), "json", alConf.getCharset(), alConf.getAlipayPublicKey(), alConf.getSignType()); //获得初始化的AlipayClient
			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);

			// 成功获得authToken
			if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {
				userId = oauthTokenResponse.getUserId();
			} else {
				// 这里仅是简单打印， 请开发者按实际情况自行进行处理
				LOG.error("authCode换取authToken失败");
			}
		} catch (AlipayApiException alipayApiException) {
			// 自行处理异常
			alipayApiException.printStackTrace();
		}

		String result = "<!DOCTYPE html><html>" + "<head>" + "<meta charset=\"utf-8\">"
				+ "<meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\" />"
				+ "</head>";
		/* Cookies.addCookie(response2, ConstantsToken.AUTH_userId, userId); */

		if (userId == null) {
			result = result + "<body style=\"font-size:24px;\">获取支付宝权限失败</body>";
		} else {
			if (callbackUrl.indexOf("?") > 0) {
				callbackUrl = callbackUrl + "&userId=" + userId;
			} else {
				callbackUrl = callbackUrl + "?userId=" + userId;
			}
			result = result + "<body style=\"font-size:24px;\">正在跳转主站中，请稍候...</body>" + "<script>window.location='"
					+ callbackUrl + "';</script></html>";
		}

		LOG.info("result: {}", result);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = URL_PREFIX + "verifySign", method = { RequestMethod.POST, RequestMethod.GET })
	public void verifyAli(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 支付宝响应消息
		String responseMsg = "";

		// 1. 解析请求参数
		Map<String, String> params = RequestUtil.getRequestParams(request);

		// 打印本次请求日志，开发者自行决定是否需要
		LOG.info("支付宝请求串: {}", params.toString());

		try {
			// 2. 验证签名
			this.verifySign(params);

			// 3. 获取业务执行器 根据请求中的 service, msgType, eventType, actionParam 确定执行器
//			ActionExecutor executor = Dispatcher.getExecutor(params);

			// 4. 执行业务逻辑
//			responseMsg = executor.execute();

		} catch (AlipayApiException alipayApiException) {
			// 开发者可以根据异常自行进行处理
			alipayApiException.printStackTrace();

		} catch (Exception exception) {
			// 开发者可以根据异常自行进行处理
			exception.printStackTrace();

		} finally {
			// 5. 响应结果加签及返回
			try {
				// 对响应内容加签
				responseMsg = encryptAndSign(responseMsg, alConf.getAlipayPublicKey(),
						alConf.getGetMerchantPrivateKey(), alConf.getCharset(), false, true,
						alConf.getSignType());

				// http 内容应答
				response.reset();
				response.setContentType("text/xml;charset=GBK");
				PrintWriter printWriter = response.getWriter();
				printWriter.print(responseMsg);
				response.flushBuffer();

				// 开发者自行决定是否要记录，视自己需求
				LOG.info("开发者响应串", responseMsg);

			} catch (AlipayApiException alipayApiException) {
				// 开发者可以根据异常自行进行处理
				alipayApiException.printStackTrace();
			}
		}
	}


	public static CloseableHttpClient createDefault() {
		return HttpClientBuilder.create().build();
	}
	/**
	 * 验签
	 * 
	 * @param ‘
	 * @return
	 */
	private void verifySign(Map<String, String> params) throws AlipayApiException {

		if (!AlipaySignature.rsaCheckV2(params, alConf.getAlipayPublicKey(),
				alConf.getCharset(), alConf.getSignType())) {
			throw new AlipayApiException("verify sign fail.");
		}
	}

	public static String encryptAndSign(String bizContent, String alipayPublicKey, String cusPrivateKey, String charset,
			boolean isEncrypt, boolean isSign, String signType) throws AlipayApiException {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isEmpty(charset)) {
			charset = AlipayConstants.CHARSET_GBK;
		}
		sb.append("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
		if (isEncrypt) {// 加密
			sb.append("<alipay>");
			String encrypted = AlipaySignature.rsaEncrypt(bizContent, alipayPublicKey, charset);
			sb.append("<response>" + encrypted + "</response>");
			sb.append("<encryption_type>AES</encryption_type>");
			if (isSign) {
				String sign = AlipaySignature.rsaSign(encrypted, cusPrivateKey, charset, signType);
				sb.append("<sign>" + sign + "</sign>");
				sb.append("<sign_type>");
				sb.append(signType);
				sb.append("</sign_type>");
			}
			sb.append("</alipay>");
		} else if (isSign) {// 不加密，但需要签名
			sb.append("<alipay>");
			sb.append("<response>" + bizContent + "</response>");
			String sign = AlipaySignature.rsaSign(bizContent, cusPrivateKey, charset, signType);
			sb.append("<sign>" + sign + "</sign>");
			sb.append("<sign_type>");
			sb.append(signType);
			sb.append("</sign_type>");
			sb.append("</alipay>");
		} else {// 不加密，不加签
			sb.append(bizContent);
		}
		return sb.toString();
	}

}
