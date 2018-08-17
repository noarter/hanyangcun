package com.hanyangcun.service.impl;

import com.hanyangcun.dao.IRoomsDao;
import com.hanyangcun.exception.ErrorCodeException;
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
    public List<Rooms> getList(Rooms rooms) throws ErrorCodeException {
        return roomsDao.getList(rooms);
    }

    @Override
    public Rooms get(Long id) throws ErrorCodeException {
        return roomsDao.get(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void insert(Rooms rooms) throws ErrorCodeException {
        rooms.setCreateTime(System.currentTimeMillis());
        rooms.setUpdateTime(rooms.getCreateTime());
        roomsDao.insert(rooms);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void update(Rooms rooms) throws ErrorCodeException {
        rooms.setUpdateTime(System.currentTimeMillis());
        roomsDao.update(rooms);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateBatch(String weekDate, List<Long> ids) throws ErrorCodeException {
        if (!CollectionUtils.isEmpty(ids)) {
            ids.forEach((Long id) -> {
                Rooms room = new Rooms();
                room.setWeekDate(weekDate);
                room.setId(id);
                room.setUpdateTime(System.currentTimeMillis());
                roomsDao.update(room);
            });
        }
    }

}
