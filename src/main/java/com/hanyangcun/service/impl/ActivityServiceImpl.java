package com.hanyangcun.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanyangcun.dao.IActivityDao;
import com.hanyangcun.enums.ActivityStatus;
import com.hanyangcun.model.Activity;
import com.hanyangcun.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements IActivityService {

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
