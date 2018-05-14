package com.hanyangcun.dao;

import com.hanyangcun.model.Activity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IActivityDao {

    @Insert("insert into t_activity (id, name, content, start_time, end_time, discount_price, use_count, status, create_time, update_time) " +
            "values (#{id}, #{name}, #{content}, #{startTime}, #{endTime}, #{discountPrice}, #{useCount}, #{status}, #{createTime}, #{updateTime})")
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
    Activity getActivityById(@Param("id") Long id);

    @Select("<script>" +
            "select * from t_activity " +
            "<where>" +
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
