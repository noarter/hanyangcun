package com.hanyangcun.dao;

import com.hanyangcun.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IUserDao {

    @Insert("insert into t_user (id, username, password, salt, locked, create_time, update_time) " +
            "values (#{id}, #{username}, #{password}, #{salt}, #{locked}, #{createTime}, #{updateTime})")
    void insert(User user);

    @Update("<script>" +
            "update t_user " +
            "<set>" +
            "<if test=\"password != null\">password=#{password},</if>" +
            "<if test=\"locked != null\">locked=#{locked},</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime}</if>" +
            "</set>" +
            " where id=#{id}" +
            "</script>")
    void update(User user);

    @Select("<script>" +
            "select id,username,password,salt,locked,create_time,update_time from t_user " +
            "<where>" +
            "<if test=\"id != null and id != '' \"> and id = #{id}</if>" +
            "<if test=\"username != null and username != '' \"> and username = #{username}</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    User getUserByUsernameOrId(User user);

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
