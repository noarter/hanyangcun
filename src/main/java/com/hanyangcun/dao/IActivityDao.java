package com.hanyangcun.dao;

import com.hanyangcun.model.Activity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IActivityDao {

    @Insert("<script>" +
            "insert into t_activity " +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" +
            "id, name, template_code, content, start_time, end_time," +
            "<if test=\"discountPrice != null\"> discount_price,</if>" +
            "<if test=\"useCount != null\"> use_count,</if>" +
            "status, create_time, update_time" +
            "</trim>" +
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
            "#{id}, #{name}, #{templateCode}, #{content}, #{startTime}, #{endTime}," +
            "<if test=\"discountPrice != null\"> #{discountPrice},</if>" +
            "<if test=\"useCount != null\"> #{useCount},</if>" +
            " #{status}, #{createTime}, #{updateTime}" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Activity activity);

    @Select("select * from t_activity where id = #{id}")
    @Results({
            @Result(column = "template_code", property = "templateCode"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "discount_price", property = "discountPrice"),
            @Result(column = "use_count", property = "useCount"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    Activity get(@Param("id") Long id);

    @Select("<script>" +
            "select *,if(unix_timestamp()*1000 &lt; start_time,1,if(unix_timestamp()*1000 &gt; end_time,3,2)) as activityStatus from t_activity " +
            "<where>" +
            "<if test=\"activityStatus == 1 \"> and now() &lt; start_time</if>" +
            "<if test=\"activityStatus == 2 \"> and now() &gt; start_time and now() &lt; end_time</if>" +
            "<if test=\"activityStatus == 3 \"> and now() &gt; end_time</if>" +
            "<if test=\"name != null and name != '' \"> and name = #{name}</if>" +
            "<if test=\"content != null and content != '' \"> and content = #{content}</if>" +
            "<if test=\"startTime != null \"> and start_time &gt; #{startTime}</if>" +
            "<if test=\"endTime != null \"> and end_time &lt; #{endTime}</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "template_code", property = "templateCode"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "discount_price", property = "discountPrice"),
            @Result(column = "use_count", property = "useCount"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<Activity> getList(Activity activity);

    @Update("<script>" +
            "update t_activity " +
            "<set>" +
            "<if test=\"name != null and name != ''\">name=#{name},</if>" +
            "<if test=\"templateCode != null and templateCode != ''\">template_code=#{templateCode},</if>" +
            "<if test=\"content != null and content != ''\">content=#{content},</if>" +
            "<if test=\"startTime != null\">start_time=#{startTime},</if>" +
            "<if test=\"endTime != null\">end_time=#{endTime},</if>" +
            "<if test=\"discountPrice != null\">discount_price=#{discountPrice},</if>" +
            "<if test=\"useCount != null\">use_count=#{useCount},</if>" +
            "<if test=\"status != null\">status=#{status},</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime},</if>" +
            "</set>" +
            " where id = #{id}" +
            "</script>")
    void update(Activity activity);

    @Delete("delete from t_activity where id = #{id}")
    void delete(@Param("id") Long id);
}
