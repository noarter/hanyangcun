package com.hanyangcun.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanyangcun.dao.IActivityDao;
import com.hanyangcun.enums.ActivityStatus;
import com.hanyangcun.model.Activity;
import com.hanyangcun.dao.IActivityDao;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Activity;
import com.hanyangcun.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import java.util.List;

@Service
public class ActivityServiceImpl implements IActivityService {

    @Autowired
    private IActivityDao activityDao;

    /**
     * 新增活动
     *
     * @param activity
     * @throws ErrorCodeException
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void insert(Activity activity) throws ErrorCodeException {
        activityDao.insert(activity);
    }

    /**
     * 修改活动
     *
     * @param activity
     * @throws ErrorCodeException
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void update(Activity activity) throws ErrorCodeException {
        activityDao.update(activity);
    }

    /**
     * 复制活动
     *
     * @param id
     * @throws ErrorCodeException
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void copy(Long id) throws ErrorCodeException {
        Activity activity = activityDao.getById(id);
        activity.setCreateTime(new Date());
        activity.setUpdateTime(activity.getCreateTime());
        activityDao.insert(activity);
    }

    /**
     * 删除活动
     *
     * @param id
     * @throws ErrorCodeException
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void delete(Long id) throws ErrorCodeException {
        activityDao.delete(id);
    }

    /**
     * 获取活动列表
     *
     * @param activity
     * @return
     * @throws ErrorCodeException
     */
    @Override
    public List<Activity> getList(Activity activity) throws ErrorCodeException {
        return activityDao.getList(activity);
    }

    /**
     * 获取活动详情
     *
     * @param id
     * @return
     * @throws ErrorCodeException
     */
    @Override
    public Activity getById(Long id) throws ErrorCodeException {
        return activityDao.getById(id);
    }

    @Autowired
    private IActivityDao activityDao;

    @Override
    public PageInfo<Activity> getList(Integer pageIndex, Integer pageSize, Activity activity) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Activity> list = activityDao.getList(activity);
        return new PageInfo<>(list);
    }

    @Override
    public Activity insert(Activity activity) {
        activityDao.insert(activity);
        return activity;
    }

    @Override
    public Boolean outOfStock(Long id) {
        Activity activity = new Activity();
        activity.setId(id);
        activity.setStatus(ActivityStatus.OUT_OF_STOCK.getStatus());
        activityDao.update(activity);
        return true;
    }
}
