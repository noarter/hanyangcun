package com.hanyangcun.component;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class SmsComponent {

    @Autowired
    private IAcsClient acsClient;

    /**
     * 单条短信发送api
     * @param request
     * @return
     * @throws ClientException
     */
    public SendSmsResponse sendSms(SendSmsRequest request) throws ClientException {
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }

    /**
     * 批量短信发送api
     * @param request
     * @return
     * @throws ClientException
     */
    public SendBatchSmsResponse SendBatchSms(SendBatchSmsRequest request) throws ClientException {
        //请求失败这里会抛ClientException异常
        SendBatchSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }


    /**
     * 短信查询api
     * @param request
     * @return
     * @throws ClientException
     */
    public QuerySendDetailsResponse querySendDetails(QuerySendDetailsRequest request) throws ClientException {
        //请求失败这里会抛ClientException异常
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    public static void main(String[] args) throws ClientException, InterruptedException {

        SmsComponent smsComponent = new SmsComponent();

        //发短信
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        //必填:待发送手机号
        sendSmsRequest.setPhoneNumbers("15000000000");
        //必填:短信签名-可在短信控制台中找到
        sendSmsRequest.setSignName("云通信");
        //必填:短信模板-可在短信控制台中找到
        sendSmsRequest.setTemplateCode("SMS_1000000");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        sendSmsRequest.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        sendSmsRequest.setOutId("yourOutId");
        SendSmsResponse response = smsComponent.sendSms(sendSmsRequest);
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());

        Thread.sleep(3000L);

        //查明细
        if(response.getCode() != null && response.getCode().equals("OK")) {
            //组装请求对象
            QuerySendDetailsRequest querySendDetailsRequest = new QuerySendDetailsRequest();
            //必填-号码
            querySendDetailsRequest.setPhoneNumber("15000000000");
            //可选-流水号
            querySendDetailsRequest.setBizId(response.getBizId());
            //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
            SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
            querySendDetailsRequest.setSendDate(ft.format(new Date()));
            //必填-页大小
            querySendDetailsRequest.setPageSize(10L);
            //必填-当前页码从1开始计数
            querySendDetailsRequest.setCurrentPage(1L);
            QuerySendDetailsResponse querySendDetailsResponse = smsComponent.querySendDetails(querySendDetailsRequest);
            System.out.println("短信明细查询接口返回数据----------------");
            System.out.println("Code=" + querySendDetailsResponse.getCode());
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
            {
                System.out.println("SmsSendDetailDTO["+i+"]:");
                System.out.println("Content=" + smsSendDetailDTO.getContent());
                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
            }
            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
        }

        SendBatchSmsRequest sendBatchSmsRequest = new SendBatchSmsRequest();
        //使用post提交
        sendBatchSmsRequest.setMethod(MethodType.POST);
        //必填:待发送手机号。支持JSON格式的批量调用，批量上限为100个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        sendBatchSmsRequest.setPhoneNumberJson("[\"1500000000\",\"1500000001\"]");
        //必填:短信签名-支持不同的号码发送不同的短信签名
        sendBatchSmsRequest.setSignNameJson("[\"云通信\",\"云通信\"]");
        //必填:短信模板-可在短信控制台中找到
        sendBatchSmsRequest.setTemplateCode("SMS_1000000");
        //必填:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        sendBatchSmsRequest.setTemplateParamJson("[{\"name\":\"Tom\", \"code\":\"123\"},{\"name\":\"Jack\", \"code\":\"456\"}]");
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCodeJson("[\"90997\",\"90998\"]");

        SendBatchSmsResponse sendBatchSmsResponse = smsComponent.SendBatchSms(sendBatchSmsRequest);
        if(sendBatchSmsResponse.getCode() != null && sendBatchSmsResponse.getCode().equals("OK")) {
            //请求成功
        }
    }
}
