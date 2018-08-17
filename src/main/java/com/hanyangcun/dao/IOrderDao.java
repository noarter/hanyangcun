package com.hanyangcun.dao;

import com.hanyangcun.model.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IOrderDao {
    @Insert("<script>" +
            "insert into t_order " +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" +
            "order_no, order_type, in_time, out_time, rooms_number, nights, people, adult_people, child_people, link_name, link_phone, guests, guests_phone, order_total, discount_price, actual_amount, order_status, order_time, update_time" +
            "</trim>" +
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
            "#{orderNo}, #{orderType}, #{inTime}, #{outTime}, #{roomsNumber}, #{nights}, #{people}, #{adultPeople}, #{childPeople}, #{linkName}, #{linkPhone}, #{guests}, #{guestsPhone}, #{orderTotal}, #{discountPrice}, #{actualAmount}, #{orderStatus}, #{orderTime}, #{updateTime}" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void insert(Order order);

    @Update("<script>" +
            "update t_order " +
            "<set>" +
            "<if test=\"orderStatus != null\">order_status=#{orderStatus},</if>" +
            "<if test=\"updateTime != null\">update_time=#{updateTime}</if>" +
            "</set>" +
            "<where>" +
            "<if test=\"id != null\"> and id=#{id}</if>" +
            "<if test=\"orderNo !=null\"> and order_no=#{orderNo}</if>"+
            "</where>" +
            "</script>")
    int update(Order order);

    @Select("select * from t_order where id = #{id}")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "order_type",property = "orderType"),
            @Result(column = "in_time", property = "inTime"),
            @Result(column = "out_time", property = "outTime"),
            @Result(column = "rooms_number", property = "roomsNumber"),
            @Result(column = "adult_people", property = "adultPeople"),
            @Result(column = "child_people", property = "childPeople"),
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

    @Select("<script>" +
            "select * from t_order " +
            "<where>" +
            "<if test=\"orderNo != null and orderNo != ''\"> and order_no like concat('%',#{orderNo},'%')</if>" +
//            "<if test=\"linkPhone != null\"> and link_phone = #{linkPhone}</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "order_type",property = "orderType"),
            @Result(column = "in_time", property = "inTime"),
            @Result(column = "out_time", property = "outTime"),
            @Result(column = "rooms_number", property = "roomsNumber"),
            @Result(column = "adult_people", property = "adultPeople"),
            @Result(column = "child_people", property = "childPeople"),
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
            "select * from t_order " +
            "<where>" +
            "<if test=\"orderNo != null and orderNo != ''\"> and order_no like concat('%',#{orderNo},'%')</if>" +
            "<if test=\"linkName != null and linkName != ''\"> and link_name = #{linkName}</if>" +
            "<if test=\"linkPhone != null\"> and link_phone = #{linkPhone}</if>" +
            "<if test=\"guests != null and guests != '' \"> and guests like concat('%',#{guests},'%')</if>" +
            "<if test=\"guestsPhone != null\"> and guests_phone like concat('%',#{guestsPhone},'%')</if>" +
            "<if test=\"orderStatus != null\"> and order_status = #{orderStatus}</if>" +
            "<if test=\"orderTime != null\"> and order_time = #{orderTime}</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "order_type",property = "orderType"),
            @Result(column = "in_time", property = "inTime"),
            @Result(column = "out_time", property = "outTime"),
            @Result(column = "rooms_number", property = "roomsNumber"),
            @Result(column = "adult_people", property = "adultPeople"),
            @Result(column = "child_people", property = "childPeople"),
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

    @Select("<script>" +
            "select distinct id,order_no, order_type, in_time,out_time, rooms_number, nights, order_total, discount_price, actual_amount, order_status, order_time from t_order " +
            "<where>" +
            "<if test=\"phone != null and phone != ''\"> and (link_phone = #{phone} || guests_phone = #{phone})</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "order_type",property = "orderType"),
            @Result(column = "in_time", property = "inTime"),
            @Result(column = "out_time", property = "outTime"),
            @Result(column = "rooms_number", property = "roomsNumber"),
            @Result(column = "adult_people", property = "adultPeople"),
            @Result(column = "child_people", property = "childPeople"),
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
    List<Order> getOrderListByPhone(@Param("phone") String phone);
}
