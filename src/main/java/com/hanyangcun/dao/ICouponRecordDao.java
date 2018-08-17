package com.hanyangcun.dao;

import com.hanyangcun.model.CouponRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ICouponRecordDao {

    @Insert("<script>" +
            "insert into t_coupon_record (activity_id, phone, coupon_no, create_time, update_time) VALUES" +
            "<foreach collection=\"list\" item=\"item\" index=\"index\"  open=\"(\" separator=\",\" close=\")\">" +
            "#{item.activityId}, #{item.phone}, #{item.couponNo}, #{item.createTime}, #{item.updateTime}" +
            "</foreach>" +
            "</script>")
    void insertBatch(@Param("list") List<CouponRecord> couponRecords);

    @Select("<script>" +
            "select * from t_coupon_record " +
            "<where>" +
            "<if test=\"couponNo != null and couponNo != '' \"> and coupon_no = #{couponNo}</if>" +
            "</where>" +
            " order by create_time desc" +
            "</script>")
    @Results({
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "coupon_no", property = "couponNo"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<CouponRecord> getList(@Param("couponNo") String couponNo);
}
