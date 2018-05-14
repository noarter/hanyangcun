package com.hanyangcun.dao;

import com.hanyangcun.model.Account;
import com.hanyangcun.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IAccountDao {
    @Insert("insert into t_account (id, name, phone, sex, create_time, update_time) " +
            "values (#{id}, #{name}, #{phone}, #{sex}, #{createTime}, #{updateTime})")
    void insert(Account account);

    @Update("<script>" +
            "update t_account " +
            "<set>" +
            "<if test=\"name != null\">name=#{name},</if>" +
            "<if test=\"phone != null\">phone=#{phone},</if>" +
            "<if test=\"sex != null\">sex=#{sex}</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime}</if>" +
            "</set>" +
            " where id=#{id}" +
            "</script>")
    void update(Account account);

    @Select("select * from t_account where phone = #{phone}")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    User getUserByUsername(Integer phone);

    @Select("<script>" +
            "select * from t_account " +
            "<where>" +
            "<if test=\"name != null and name != '' \"> and name = #{name}</if>" +
            "<if test=\"phone != null and phone != '' \"> and phone = #{phone}</if>" +
            "<if test=\"sex != null and sex != '' \"> and sex = #{sex}</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<Account> getList(Account account);
}
