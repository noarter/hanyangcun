package com.hanyangcun.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanyangcun.dao.IRoomsDao;
import com.hanyangcun.model.Rooms;
import com.hanyangcun.service.IRoomsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class RoomsServiceImpl implements IRoomsService {

    @Autowired
    private IRoomsDao roomsDao;

    @Override
    public PageInfo<Rooms> getPagedList(Integer pageIndex, Integer pageSize, Rooms rooms) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Rooms> list = roomsDao.getList(rooms);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void update(Rooms rooms) {
        rooms.setUpdateTime(new Date());
        roomsDao.update(rooms);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateBatch(Rooms rooms, List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            ids.forEach((Long id) -> {
                Rooms update = new Rooms();
                BeanUtils.copyProperties(rooms, update);
                update.setId(id);
                roomsDao.update(update);
            });
        }
    }

    @Override
    public void insert(Rooms rooms) {
        roomsDao.insert(rooms);
    }

    @Override
    public Rooms get(Long id) {
        return roomsDao.getRoomsById(id);
    }
}
