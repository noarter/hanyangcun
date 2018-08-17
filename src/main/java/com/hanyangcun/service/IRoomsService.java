package com.hanyangcun.service;

import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Rooms;

import java.util.List;

public interface IRoomsService {

    List<Rooms> getList(Rooms rooms) throws ErrorCodeException;

    Rooms get(Long id) throws ErrorCodeException;

    void insert(Rooms rooms) throws ErrorCodeException;

    void update(Rooms rooms) throws ErrorCodeException;

    void updateBatch(String weekDate, List<Long> ids) throws ErrorCodeException;

}
