package com.hanyangcun.dao;

import com.hanyangcun.model.OrderItems;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IOrderItemsDao {

    @Select("<script>" +
            "select ti.*,tr.room_type as roomType from t_order_items ti,t_rooms tr " +
            "<where>" +
            "ti.rooms_id = tr.id" +
            "<if test=\"orderId != null\"> and order_id = #{orderId}</if>" +
            "</where>" +
            " order by ti.create_time desc" +
            "</script>")
    @Results({
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "rooms_id", property = "roomsId"),
            @Result(column = "rooms_price", property = "roomsPrice"),
            @Result(column = "pre_rooms", property = "preRooms"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<OrderItems> getList(@Param("orderId") Long orderId);

    @Insert("<script>" +
            "insert into t_order_items (order_id, rooms_id, rooms_price, pre_rooms, create_time, update_time) VALUES" +
            "<foreach collection=\"list\" item=\"item\" index=\"index\"  open=\"(\" separator=\",\" close=\")\">" +
            "#{item.orderId}, #{item.roomsId}, #{item.roomsPrice}, #{item.preRooms}, #{item.createTime}, #{item.updateTime}" +
            "</foreach>" +
            "</script>")
    void batchInsert(List<OrderItems> orderItemsList);
}
