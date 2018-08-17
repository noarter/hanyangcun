package com.hanyangcun.dao;

import com.hanyangcun.model.Rooms;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IRoomsDao {
    @Insert("<script>" +
            "insert into t_rooms " +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" +
            "id, room_type, room_number, " +
            "<if test=\"usedRoom != null\"> used_room,</if>" +
            "<if test=\"remainedRoom != null\"> remained_room,</if>" +
            "room_price, " +
            "<if test=\"discountPrice != null\"> discount_price,</if>" +
            "<if test=\"discountRate != null\"> discount_rate,</if>" +
            "week_date, status, create_time, update_time" +
            "</trim>" +
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
            "#{id}, #{roomType}, #{roomNumber}," +
            "<if test=\"usedRoom != null\"> #{usedRoom},</if>" +
            "<if test=\"remainedRoom != null\"> #{remainedRoom},</if>" +
            " #{roomPrice}," +
            "<if test=\"discountPrice != null\"> #{discountPrice},</if>" +
            "<if test=\"discountRate != null\"> #{discountRate},</if>" +
            " #{weekDate}, #{status}, #{createTime}, #{updateTime}" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void insert(Rooms rooms);

    @Update("<script>" +
            "update t_rooms " +
            "<set>" +
            "<if test=\"roomType != null and roomType != ''\">room_type=#{roomType},</if>" +
            "<if test=\"roomNumber != null\">room_number=#{roomNumber},</if>" +
            "<if test=\"roomPrice != null\">room_price=#{roomPrice},</if>" +
            "<if test=\"discountPrice != null\">discount_price=#{discountPrice},</if>" +
            "<if test=\"discountRate != null\">discount_rate=#{discountRate},</if>" +
            "<if test=\"weekDate != null and weekDate != ''\">week_date=#{weekDate},</if>" +
            "<if test=\"status != null\">status=#{status},</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime}</if>" +
            "</set>" +
            " where id=#{id}" +
            "</script>")
    void update(Rooms rooms);

    @Select("select * from t_rooms where id = #{id}")
    @Results({
            @Result(column = "room_type", property = "roomType"),
            @Result(column = "room_number", property = "roomNumber"),
            @Result(column = "used_room", property = "usedRoom"),
            @Result(column = "remained_room", property = "remainedRoom"),
            @Result(column = "room_price", property = "roomPrice"),
            @Result(column = "discount_price", property = "discountPrice"),
            @Result(column = "discount_rate", property = "discountRate"),
            @Result(column = "week_date", property = "weekDate"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    Rooms get(@Param("id") Long id);

    @Select("<script>" +
            "select * from t_rooms " +
            "<where>" +
            "<if test=\"roomType != null and roomType != '' \"> and room_type = #{roomType}</if>" +
            "<if test=\"status != null\"> and status = #{status}</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "room_type", property = "roomType"),
            @Result(column = "room_number", property = "roomNumber"),
            @Result(column = "used_room", property = "usedRoom"),
            @Result(column = "remained_room", property = "remainedRoom"),
            @Result(column = "room_price", property = "roomPrice"),
            @Result(column = "discount_price", property = "discountPrice"),
            @Result(column = "discount_rate", property = "discountRate"),
            @Result(column = "week_date", property = "weekDate"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<Rooms> getList(Rooms rooms);
}
