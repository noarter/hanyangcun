package com.hanyangcun.dao;

import com.hanyangcun.model.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IAccountDao {
    @Insert("insert into t_account (name, phone, sex, create_time, update_time) " +
            "values (#{name}, #{phone}, #{sex}, #{createTime}, #{updateTime}) ON DUPLICATE KEY UPDATE name = values(name), sex = values(sex)")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void insert(Account account);

    @Insert("<script>"  +
            "insert into t_account (name, phone, sex, create_time, update_time) VALUES" +
            "<foreach collection=\"list\" item=\"item\" index=\"index\"  open=\"(\" separator=\",\" close=\")\">" +
            "#{item.name}, #{item.phone}, #{item.sex}, #{item.createTime}, #{item.updateTime}" +
            "</foreach>" +
            " ON DUPLICATE KEY UPDATE name = values(name), sex = values(sex)" +
            "</script>")
    void insertBatch(@Param("list") List<Account> accounts);

    @Update("<script>" +
            "update t_account " +
            "<set>" +
            "<if test=\"name != null and name != ''\">name=#{name},</if>" +
            "<if test=\"sex != null\">sex=#{sex},</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime},</if>" +
            "</set>" +
            " where phone=#{phone}" +
            "</script>")
    void update(Account account);

    @Select("select * from t_account where phone = #{phone}")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    Account getByPhone(@Param("phone") Integer phone);

    @Select("select * from t_account where phone = #{phone}")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    Account get(@Param("phone") String phone);

    @Select("<script>" +
            "select * from t_account " +
            "<where>" +
            "<if test=\"name != null and name != ''\"> and name = #{name}</if>" +
            "<if test=\"phone != null\"> and phone = #{phone}</if>" +
            "<if test=\"sex != null\"> and sex = #{sex}</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<Account> getList(Account account);


    @Select("<script>"  +
            "select * from t_account where phone in" +
            "<foreach collection=\"array\" item=\"item\" index=\"index\"  open=\"(\" separator=\",\" close=\")\">" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<Account> batchExport(@Param("phones") String[] phones);
}
