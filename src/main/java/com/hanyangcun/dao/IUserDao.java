package com.hanyangcun.dao;

import com.hanyangcun.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IUserDao {

    @Insert("<script>" +
            "insert into t_user " +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" +
            "id, username, password, salt, locked, create_time, update_time" +
            "</trim>" +
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
            "#{id}, #{username}, #{password}, #{salt}, #{locked}, #{createTime}, #{updateTime}" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void insert(User user);

    @Update("<script>" +
            "update t_user " +
            "<set>" +
            "<if test=\"password != null and password != ''\">password=#{password},</if>" +
            "<if test=\"locked != null\">locked=#{locked},</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime}</if>" +
            "</set>" +
            " where id=#{id}" +
            "</script>")
    void update(User user);

    @Select("<script>" +
            "select id,username,password,salt,locked,create_time,update_time from t_user " +
            "<where>" +
            "<if test=\"id != null\"> and id = #{id}</if>" +
            "<if test=\"username != null and username != '' \"> and username = #{username}</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    User get(User user);

    @Select("select id,username,password,salt,locked,create_time,update_time from t_user where id = #{id} and locked = 0")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    User getById(@Param("id") Long id);

    @Select("select id,username,password,salt,locked,create_time,update_time from t_user where username = #{username} and locked = 0")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    User getByName(String username);

    @Select("<script>" +
            "select id,username,password,salt,locked,create_time,update_time from t_user " +
            "<where>" +
            "<if test=\"username != null and username != '' \"> and username = #{username}</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    List<User> getList(User user);

}
