package com.hanyangcun.service;

import com.github.pagehelper.PageInfo;
import com.hanyangcun.model.Activity;

public interface IActivityService {

    PageInfo<Activity> getList(Integer pageIndex, Integer pageSize, Activity activity);

    Activity insert(Activity activity);

    Boolean outOfStock(Long id);
}
