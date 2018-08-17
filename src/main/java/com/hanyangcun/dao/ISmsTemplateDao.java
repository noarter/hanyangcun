package com.hanyangcun.dao;

import com.hanyangcun.model.SmsTemplate;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ISmsTemplateDao {
    @Select("<script>" +
            "select * from t_sms_template " +
            "<where>" +
            "<if test=\"name != null and name != '' \"> and name = #{name}</if>" +
            "</where>" +
            "order by update_time desc" +
            "</script>")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    List<SmsTemplate> getList(SmsTemplate smsTemplate);

    @Insert("<script>" +
            "insert into t_sms_template " +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" +
            "name, code, content, create_time, update_time" +
            "</trim>" +
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
            "#{name}, #{code}, #{content}, #{createTime}, #{updateTime}" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void insert(SmsTemplate smsTemplate);

    @Update("<script>" +
            "update t_sms_template " +
            "<set>" +
            "<if test=\"name != null and name != ''\">name=#{name},</if>" +
            "<if test=\"code != null and code != ''\">code=#{code},</if>" +
            "<if test=\"content != null and content != ''\">content=#{content},</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime}</if>" +
            "</set>" +
            " where id=#{id}" +
            "</script>")
    void update(SmsTemplate smsTemplate);

    @Select("<script>" +
            "select * from t_sms_template where name=#{name}" +
            "</script>")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    SmsTemplate get(@Param("name") String name);
}
