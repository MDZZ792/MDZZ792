package com.test.Service.Impl;

import com.test.Dao.InfluxdbDao;
import com.test.Service.InfluxdbService;

import javax.inject.Inject;
import java.util.List;

public class InfluxdbServiecImpl implements InfluxdbService {
    @Inject
    InfluxdbDao influxdbDao;

    @Override
    public List<String> GetData() {
        return influxdbDao.GetData();
    }

    @Override
    public List<String> GetTimeData(String time1, String time2) {
        return influxdbDao.GetTimeData(time1, time2);
    }

    @Override
    public String GetNum() {
        return influxdbDao.GetNum();
    }

    @Override
    public List<String> GetLimit(String... param) {
        return influxdbDao.GetLimit(param);
    }
}
