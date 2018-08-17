package com.hanyangcun.dao;

import com.hanyangcun.model.PayRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IPayRecordDao {

    @Insert("<script>" +
            "insert into t_pay_record " +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" +
            "order_no, type, pay_no, trade_no, create_time, pay_state, spbill_create_ip" +
            "</trim>" +
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
            "#{orderNo}, #{type}, #{payNo}, #{tradeNo}, #{createTime}, #{payState}, #{spbillCreateIp}" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insert(PayRecord payRecord);

    @Update("<script>" +
            "update t_pay_record " +
            "<set>" +
            "<if test=\"tradeNo != null\">trade_no=#{tradeNo},</if>" +
            "<if test=\"payState != null\">pay_state=#{payState},</if>" +
            "<if test=\"outRequestNo != null\">out_request_no=#{outRequestNo},</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime}</if>" +
            "</set>" +
            " where pay_no=#{payNo}" +
            "</script>")
    int update(PayRecord payRecord);

    @Select("select id,order_no orderNo,type,pay_no payNo,trade_no tradeNo,create_time createTime,update_time updateTime," +
            "pay_state payState,spbill_create_ip spbillCreateIp   from t_pay_record where pay_no =#{payNo}")
    PayRecord getPayDetailByPayNo(@Param("payNo") String payNo);

    @Select("select id,order_no orderNo,type,pay_no payNo,trade_no tradeNo,create_time createTime,update_time updateTime," +
            "pay_state payState,spbill_create_ip spbillCreateIp   from t_pay_record where order_no =#{orderNo}")
    List<PayRecord> getPayDetailByOrderNo(@Param("orderNo") String orderNo);
}
