package com.hanyangcun.dao;

import com.hanyangcun.model.PayRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IPayRecordDao {

    void insert(PayRecord payRecord);
}
