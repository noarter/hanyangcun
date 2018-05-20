package com.hanyangcun.dao;

import com.hanyangcun.model.Activity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IActivityDao {

    @Insert("<script>" +
            "insert into t_activity " +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" +
            "id, name, content, start_time, end_time," +
            "<if test=\"discountPrice != null and discountPrice != '' \"> discount_price,</if>" +
            "<if test=\"useCount != null and useCount != '' \"> use_count,</if>" +
            "status, create_time, update_time" +
            "</trim>" +
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
            "#{id}, #{name}, #{content}, #{startTime}, #{endTime}," +
            "<if test=\"discountPrice != null and discountPrice != '' \"> #{discountPrice},</if>" +
            "<if test=\"useCount != null and useCount != '' \"> #{useCount},</if>" +
            " #{status}, #{createTime}, #{updateTime}" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Activity activity);

    @Select("select * from t_activity where id = #{id}")
    @Results({
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "discount_price", property = "discountPrice"),
            @Result(column = "use_count", property = "useCount"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    Activity getById(@Param("id") Long id);

    @Select("<script>" +
            "select *,if(now() &lt; start_time,1,if(now() &gt; end_time,3,2)) as activityStatus from t_activity " +
            "<where>" +
            "<if test=\"activityStatus == 1 \"> and now() &lt; start_time</if>" +
            "<if test=\"activityStatus == 2 \"> and now() &gt; start_time and now() &lt; end_time</if>" +
            "<if test=\"activityStatus == 3 \"> and now() &gt; end_time</if>" +
            "<if test=\"username != null and username != '' \"> and username = #{username}</if>" +
            "<if test=\"content != null and content != '' \"> and content = #{content}</if>" +
            "<if test=\"startTime != null and startTime != '' \"> and start_time &gt; #{startTime}</if>" +
            "<if test=\"endTime != null and endTime != '' \"> and end_time &lt; #{endTime}</if>" +
            "</where>" +
            "</script>")
    @Results({
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
            "<if test=\"name != null\">name=#{name},</if>" +
            "<if test=\"content != null\">content=#{content},</if>" +
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
