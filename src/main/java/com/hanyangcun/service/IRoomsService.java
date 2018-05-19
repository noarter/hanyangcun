package com.hanyangcun.service;

import com.github.pagehelper.PageInfo;
import com.hanyangcun.model.Rooms;

import java.util.List;

public interface IRoomsService {

    PageInfo<Rooms> getPagedList(Integer pageIndex, Integer pageSize, Rooms rooms);

    void update(Rooms rooms);

    Rooms get(Long id);

    void updateBatch(Rooms rooms, List<Long> ids);

    void insert(Rooms rooms);

}
