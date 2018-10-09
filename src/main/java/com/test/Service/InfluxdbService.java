package com.test.Service;

import java.util.List;

public interface InfluxdbService {

    List<String> GetData();

    List<String> GetTimeData(String time1,String time2);

    String GetNum();

    List<String> GetLimit(String... param);

}
