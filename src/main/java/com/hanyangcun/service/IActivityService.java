package com.hanyangcun.service;

import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Activity;

import java.util.List;

public interface IActivityService {
    /**
     * 新增活动
     * @param activity
     * @throws ErrorCodeException
     */
    void insert(Activity activity) throws ErrorCodeException;

    /**
     * 修改活动
     * @param activity
     * @throws ErrorCodeException
     */
    void update(Activity activity)  throws ErrorCodeException;

    /**
     * 复制活动
     * @param id
     * @throws ErrorCodeException
     */
    void copy(Long id) throws ErrorCodeException;

    /**
     * 删除活动
     * @param id
     * @throws ErrorCodeException
     */
    void delete(Long id) throws ErrorCodeException;

    /**
     * 获取活动列表
     * @param activity
     * @return
     * @throws ErrorCodeException
     */
    List<Activity> getList(Activity activity) throws ErrorCodeException;

    /**
     * 获取活动详情
     * @param id
     * @return
     * @throws ErrorCodeException
     */
    Activity getById(Long id) throws ErrorCodeException;

}
