package com.test.Dao.Impl;

import com.test.Dao.InfluxdbDao;
import com.test.infulxdb.Util.InfluxdbUtil;
import com.test.util.PropertiesUtil;
import net.sf.json.JSONObject;
import org.influxdb.dto.QueryResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class InfluxdbDaoImpl implements InfluxdbDao {


    private  static Properties properties = PropertiesUtil.loadProperties("InfluxdbUrl.properties");
    private String username = properties.getProperty("username");
    private String password = properties.getProperty("password");
    private String openurl = properties.getProperty("openurl");
    private String database = properties.getProperty("database");


    public List<String> GetData(){
        InfluxdbUtil influxdbUtil = InfluxdbUtil.setUp(username, password, openurl, database);
        String command = "select * from TestData2";
        List<String> analyze = influxdbUtil.Analyze(command, null);
        influxdbUtil.CloseDown();
        return analyze;
    }

    public List<String> GetTimeData(String time1,String time2){
        InfluxdbUtil influxdbUtil = InfluxdbUtil.setUp(username, password, openurl, database);
        String command = "select * from TestData where time > "+time1+" and time < "+time2;
        List<String> analyze = influxdbUtil.Analyze(command, null);
        influxdbUtil.CloseDown();
        return analyze;
    }

    public String GetNum(){
        String command = "select * from TestData";
        InfluxdbUtil influxdbUtil = InfluxdbUtil.setUp(username, password, openurl, database);
        QueryResult query = influxdbUtil.query(command);
        List<QueryResult.Result> results = query.getResults();
        List<QueryResult.Series> series = null;
        List<String> columns = null;
        List<List<Object>> values = null;
        List<Object> value = null;
        String TimeVale = "";
        String NumValue = "";
        Map<String, String> ResultMap = new HashMap<String, String>();
        if(results != null){
            for(QueryResult.Result qr : results){
                series = qr.getSeries();
            }
            for(int i=0;i<series.size();i++){
                columns = series.get(i).getColumns();
                values = series.get(i).getValues();
            }
            for(int i=0;i<values.size();i++){
                value = values.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                for(int b=0;b<value.size();b++){
                    String c =  columns.get(b);
                    if(c.equals("time")){
                        TimeVale = TimeVale+value.get(b);
                    }
                    if(c.equals("Num")){
                        NumValue = NumValue+value.get(b)+",";
                    }
                }
            }
        }
        String TimeResult = TimeVale.replace("T"," ").replace("Z",",").substring(0,TimeVale.length()-1);
        String NumResult = NumValue.substring(0,NumValue.length()-1);
        ResultMap.put("TimeResult",TimeResult);
        ResultMap.put("NumResult",NumResult);
        JSONObject jsonObject = JSONObject.fromObject(ResultMap);
        influxdbUtil.CloseDown();
        return jsonObject.toString();
    }

    public List<String> GetLimit(String... param){
        String string = "";
        List<String> analyze = null;
        for(String s : param ){
            string = string + s + ",";
        }
        if(param.length > 0){
            InfluxdbUtil influxdbUtil = InfluxdbUtil.setUp(username, password, openurl, database);
            String command= "select "+string.substring(0,string.length()-1)+" from TestData3";
            analyze = influxdbUtil.Analyze(command, null);
        }
        return analyze;
    }

    public void GetTime(String t1, String t2){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            long time1 = sdf.parse("").getTime();
            long time2 = sdf.parse("").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
