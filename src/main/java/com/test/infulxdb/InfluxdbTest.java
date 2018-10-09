package com.test.infulxdb;

import com.test.infulxdb.Util.InfluxdbUtil;
import com.test.util.PropertiesUtil;
import net.sf.json.JSONObject;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;

import java.text.SimpleDateFormat;
import java.util.*;

public class InfluxdbTest {

    //加载properties文件
    private  static Properties properties = PropertiesUtil.loadProperties("InfluxdbUrl.properties");
    private String username = properties.getProperty("username");
    private String password = properties.getProperty("password");
    private String openurl = properties.getProperty("openurl");
    private String database = properties.getProperty("database");
    private InfluxdbUtil IdbUtil;

    private void connectDB(){
        IdbUtil = new InfluxdbUtil(username,password,openurl,database);
        IdbUtil.influxDbBuild();
        IdbUtil.createRetentionPolicy();
    }

    /**
     * 向influxdb中添加数据
     */
    public void Add(){
        Map<String, String> Tagmap = new HashMap<String, String>();
        Map<String, Object> Fieldmap = new HashMap<String, Object>();

        Tagmap.put("Montior","news");
        //添加字段与字段值
        Fieldmap.put("Host_IP", "192.168.1.118");
        Fieldmap.put("Host_NetInRate", "1M/s");
        Fieldmap.put("Host_NetOutRate", "2m/s");
        Fieldmap.put("Host_Docker", "77");
        Fieldmap.put("Host_Lxd", "88");
        Fieldmap.put("Host_Usertime", "105311025");
        Fieldmap.put("Host_Nice", "229759");
        Fieldmap.put("Host_SystemTime", "59143289");
        Fieldmap.put("Host_IdleTime", "11055216972");
        Fieldmap.put("TestNULL","");
        //想那个表添加数据
        IdbUtil.insert("Test_measurement",Tagmap,Fieldmap);
        IdbUtil.insert("db_name","Test_t",Tagmap,Fieldmap);
    }

    public void AddData(){
        for(int a=0;a<20;a++){
            Map<String, String> TagMap = new HashMap<String, String>();
            Map<String, Object> FileMap = new HashMap<String, Object>();
            TagMap.put("Test","TestData");
            Random r = new Random();
            //在0-10000中生成随机数
            Integer b = r.nextInt(10000);
            //在0-3中生成随机数
            Integer c = r.nextInt(3);
            String[] StringArray = {"TestGetAPI","TestPostAPI","TestPutAPI","TestDeleteAPI"};
            FileMap.put("Num",b);
            FileMap.put("APIStatus", c.toString());
            FileMap.put("APIName",StringArray[r.nextInt(4)]);
            IdbUtil.insert("TestData3",TagMap,FileMap);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("over");

    }

    public String Get01(){
        String command = "select mean() from TestData";
        List<String> analyze = IdbUtil.Analyze(command, null);

        return analyze.toString();
    }

    public String Get02(){
        String command = "select * from TestData";
        QueryResult query = IdbUtil.query(command);
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
        return jsonObject.toString();
    }



    public static void TestAdd1W(){
        //创建influxdb连接对象
        InfluxDB client = InfluxDBFactory.connect("http://localhost:8086","dxx","12345678");
        int i = 0;//控制循环，批量存储，每10万条数据存储一次
        //声明influxdb批量插入数据对象
        BatchPoints batchPoints = BatchPoints.database("energy_db").consistency(InfluxDB.ConsistencyLevel.ALL).build();
        //遍历sqlserver获取数据
        for(int j=0;j<1000;j++){
            Point point = Point.measurement("Testinfluxdb")
                            //tag属性——只能存储String类型
                            .tag("TAG_TAGID", "tagid"+j)
                            .tag("TAG_ATIME", String.valueOf(new Date().getTime()))
                            //field存储数据
                            .addField("FIELDS_NVALUE", "field"+j)
                            .build();
            //将单条数据存储到集合中
            batchPoints.point(point);
            i++;
            //每读取十万条数据提交到influxdb存储一次
            if(i / 1000 == 1){
                i = 0;
                client.write(batchPoints);
               /* batchPoints = BatchPoints
                        .database("energy_db")
                        .consistency(InfluxDB.ConsistencyLevel.ALL)
                        .build();*/
            }
        }
//        client.write(batchPoints);
        client.close();
        System.out.println("结束时间：" + new Date().getTime() + ",i:"+i);

    }

    public static void Testtttt(String... param){
        System.out.println(param.length);
//        System.out.println(param);
        String s = "";
        for(String a : param){
            s=s+a+",";
            System.out.println(a);
        }
        System.out.println(s);
    }

    public static void Time(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long l = 1538184609507430104l;
        String format = sdf.format(l);
        System.out.println(format);
//        sdf.parse("2020-01-01 01:01:01");
        System.out.println(new Date().getTime());
    }
//        1538184609507430104
//        1538208726880
    public static void main(String[] args) {
//        Time();
//        Testtttt("a","b","c");
        InfluxdbTest influxdbTest = new InfluxdbTest();
        influxdbTest.connectDB();
//        influxdbTest.Add();
//
        influxdbTest.AddData();
//        TestAdd1W();
//        System.out.println(influxdbTest.Get01());
//        System.out.println(influxdbTest.Get02());
        /*Random r =new Random();
        while(true){
            try {
                Thread.sleep(300);
                System.out.println(r.nextInt(4));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }*/
    }

}
