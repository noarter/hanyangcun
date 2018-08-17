package com.hanyangcun.service.impl;

import com.hanyangcun.constant.SysConstants;
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
        activity.setStatus(SysConstants.STATUS_ONLINE);
        activity.setCreateTime(System.currentTimeMillis());
        activity.setUpdateTime(activity.getCreateTime());
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
        activity.setUpdateTime(activity.getCreateTime());
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
        Activity activity = activityDao.get(id);
        activity.setCreateTime(System.currentTimeMillis());
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
        return activityDao.get(id);
    }
}
