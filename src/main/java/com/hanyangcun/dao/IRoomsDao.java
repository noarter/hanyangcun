package com.hanyangcun.dao;

import com.hanyangcun.model.Rooms;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IRoomsDao {
    @Insert("insert into t_rooms (id, type, room_number, used_room, remained_room, room_price, discount_price, discount_rate, week_date, status, create_time, update_time) " +
            "values (#{id}, #{type}, #{roomNumber}, #{usedRoom}, #{remainedRoom}, #{roomPrice}, #{discountPrice}, #{discountRate}, #{weekDate}, #{status}, #{createTime}, #{updateTime})")
    void insert(Rooms rooms);

    @Update("<script>" +
            "update t_rooms " +
            "<set>" +
            "<if test=\"type != null\">type=#{type},</if>" +
            "<if test=\"roomNumber != null\">room_number=#{roomNumber},</if>" +
            "<if test=\"roomPrice != null\">room_price=#{roomPrice},</if>" +
            "<if test=\"discountPrice != null\">discount_price=#{discountPrice},</if>" +
            "<if test=\"discountRate != null\">discount_rate=#{discountRate},</if>" +
            "<if test=\"weekDate != null\">week_date=#{weekDate},</if>" +
            "<if test=\"status != null\">status=#{status},</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime}</if>" +
            "</set>" +
            " where id=#{id}" +
            "</script>")
    void update(Rooms rooms);

    @Select("select * from t_rooms where id = #{id}")
    @Results({
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
    Rooms getRoomsById(@Param("id") Long id);

    @Select("<script>" +
            "select * from t_rooms " +
            "<where>" +
            "<if test=\"type != null and type != '' \"> and type = #{type}</if>" +
            "<if test=\"status != null and status != '' \"> and status = #{status}</if>" +
            "</where>" +
            "</script>")
    @Results({
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
