package com.test.Dao;

import java.util.List;

public interface InfluxdbDao {

    List<String> GetData();

    List<String> GetTimeData(String time1,String time2);

    String GetNum();

    List<String> GetLimit(String... param);
}
