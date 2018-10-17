package com.test.Dao.Impl;

import com.test.Dao.PrometheusDao;

import static com.test.util.SendRequest.sendGet;

/**
 * @ClassName PrometheusDaoImpl
 * @Atuhor MDZZ_792 (｢･ω･)｢ 嘿
 * @Time 上午9:53 18-10-15
 */
public class PrometheusDaoImpl implements PrometheusDao {

    public String GetJvm_Thread_CpuUsage(String... time){
        String start = "&start=";
        String end = "&end=";
        String step = "&step=";
        String offset = "offset ";
        String TimeRange = "";
        String query = "query";
        if(time.length==0){
            start="";
            end="";
            step="";
            offset="";
            TimeRange="";
        }else if(time.length==3){
            offset = "";
            query = query+"_range";
            start = start+ time[0];
            end = end + time[1];
            step = step + time[2];
        }else if(time.length==4){
            query = query+"_range";
            offset = offset+time[0];
            start = start+ time[1];
            end = end + time[2];
            step = step + time[3];
        }
        String GetJvm_Thread_CpuUsage = sendGet("http://127.0.0.1:9090/api/v1/"+query, "query=Jvm_Thread_CpuUsage"+TimeRange+offset+start+end+step);
        System.out.println(GetJvm_Thread_CpuUsage);
        return GetJvm_Thread_CpuUsage;
    }
}
