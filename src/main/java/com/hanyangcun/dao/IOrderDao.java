package com.hanyangcun.dao;

import com.hanyangcun.model.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IOrderDao {
    @Insert("insert into t_order (id, order_no, order_type, from_the_time, rooms_number, nights, people, link_name, link_phone, guests, guests_phone, order_total, discount_price, actual_amount, order_status, order_time, update_time) " +
            "values (#{id}, #{orderNo}, #{orderType}, #{fromTheTime}, #{roomsNumber}, #{nights}, #{people}, #{linkName}, #{linkPhone}, #{guests}, #{guestsPhone}, #{orderTotal}, #{discountPrice}, #{actualAmount}, #{orderStatus}, #{orderTime}, #{updateTime})")
    void insert(Order order);

    @Update("<script>" +
            "update t_order " +
            "<set>" +
            "<if test=\"orderStatus != null\">order_status=#{orderStatus},</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime}</if>" +
            "</set>" +
            " where id=#{id} or order_no={orderNo}" +
            "</script>")
    void update(Order order);

    @Select("select * from t_order where id = #{id}")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "order_type", property = "orderType"),
            @Result(column = "from_the_time", property = "fromTheTime"),
            @Result(column = "rooms_number", property = "roomsNumber"),
            @Result(column = "link_name", property = "linkName"),
            @Result(column = "link_phone", property = "linkPhone"),
            @Result(column = "guests_phone", property = "guestsPhone"),
            @Result(column = "order_total", property = "orderTotal"),
            @Result(column = "discount_price", property = "discountPrice"),
            @Result(column = "actual_amount", property = "actualAmount"),
            @Result(column = "order_status", property = "orderStatus"),
            @Result(column = "order_time", property = "orderTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    Order getOrderDetailById(@Param("id") Long id);

    @Select("select * from t_order where order_no = #{orderNo} and link_phone=#{linkPhone}")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "order_type", property = "orderType"),
            @Result(column = "from_the_time", property = "fromTheTime"),
            @Result(column = "rooms_number", property = "roomsNumber"),
            @Result(column = "link_name", property = "linkName"),
            @Result(column = "link_phone", property = "linkPhone"),
            @Result(column = "guests_phone", property = "guestsPhone"),
            @Result(column = "order_total", property = "orderTotal"),
            @Result(column = "discount_price", property = "discountPrice"),
            @Result(column = "actual_amount", property = "actualAmount"),
            @Result(column = "order_status", property = "orderStatus"),
            @Result(column = "order_time", property = "orderTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    Order getOrderDetailByOrderNo(@Param("orderNo") String orderNo);

    @Select("<script>" +
            "select * from t_rooms " +
            "<where>" +
            "<if test=\"orderNo != null and orderNo != '' \"> and order_no = #{orderNo}</if>" +
            "<if test=\"orderType != null and orderType != '' \"> and order_type = #{orderType}</if>" +
            "<if test=\"linkName != null and linkName != '' \"> and link_name = #{linkName}</if>" +
            "<if test=\"linkPhone != null and linkPhone != '' \"> and link_phone = #{linkPhone}</if>" +
            "<if test=\"guests != null and guests != '' \"> and guests = #{guests}</if>" +
            "<if test=\"guestsPhone != null and guestsPhone != '' \"> and guests_phone = #{guestsPhone}</if>" +
            "<if test=\"orderStatus != null and orderStatus != '' \"> and order_status = #{orderStatus}</if>" +
            "<if test=\"orderTime != null and orderTime != '' \"> and order_time = #{orderTime}</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "order_type", property = "orderType"),
            @Result(column = "from_the_time", property = "fromTheTime"),
            @Result(column = "rooms_number", property = "roomsNumber"),
            @Result(column = "link_name", property = "linkName"),
            @Result(column = "link_phone", property = "linkPhone"),
            @Result(column = "guests_phone", property = "guestsPhone"),
            @Result(column = "order_total", property = "orderTotal"),
            @Result(column = "discount_price", property = "discountPrice"),
            @Result(column = "actual_amount", property = "actualAmount"),
            @Result(column = "order_status", property = "orderStatus"),
            @Result(column = "order_time", property = "orderTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<Order> getList(Order order);
}
