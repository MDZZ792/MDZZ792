package com.test.infulxdb.Util;

import net.sf.json.JSONObject;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfluxdbUtil {

    private static String openurl;//连接地址
    private static String username;//用户名
    private static String password;//密码
    private static String database;//数据库
    private static String measurement;//表名

    private InfluxDB influxDB;

    public InfluxdbUtil(String username, String password, String openurl, String database){
        this.username = username;
        this.password = password;
        this.openurl = openurl;
        this.database = database;
    }

    public static InfluxdbUtil setUp(){
        //创建 连接
        InfluxdbUtil influxDbUtil = new InfluxdbUtil(username, password, openurl, database);

        influxDbUtil.influxDbBuild();

        influxDbUtil.createRetentionPolicy();

//   influxDB.deleteDB(database);
//   influxDB.createDB(database);

        return influxDbUtil;
    }


    public static InfluxdbUtil setUp(String username, String password, String openurl, String database){
        //创建 连接
        InfluxdbUtil influxDbUtil = new InfluxdbUtil(username, password, openurl, database);

        influxDbUtil.influxDbBuild();

        influxDbUtil.createRetentionPolicy();

//   influxDB.deleteDB(database);
//   influxDB.createDB(database);
        return influxDbUtil;
    }

    public void CloseDown(){
        influxDB.close();
    }

    /**连接时序数据库；获得InfluxDB**/
    public InfluxDB influxDbBuild(){
        if(influxDB == null){
            influxDB = InfluxDBFactory.connect(openurl, username, password);
            influxDB.createDatabase(database);
        }
        return influxDB;
    }

    /**
     * 设置数据保存策略
     * defalut 策略名 /database 数据库名/ 30d 数据保存时限30天/ 1 副本个数为1/ 结尾DEFAULT 表示 设为默认的策略
     */
    public void createRetentionPolicy(){
        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                "defalut", database, "30d", 1);
        this.query(command);

    }

    /**
     * 查询
     * @param command 查询语句
     * @return
     */
    public QueryResult query(String command){
        return influxDB.query(new Query(command, database));
    }

    /**
     * 指定数据库查询
     * @param command 查询语句
     * @param database 数据库
     * @return
     */
    public QueryResult query(String command, String database){
        return influxDB.query(new Query(command, database));
    }

    /**
     * 插入
     * @param tags 标签
     * @param fields 字段
     */
    public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields){
        Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);

        influxDB.write(database, "", builder.build());
    }

    /**
     * 指定数据库插入
     * @param database 数据库
     * @param measurement 表
     * @param tags 标签
     * @param fields 字段
     */
    public void insert(String database, String measurement, Map<String, String> tags, Map<String, Object> fields){
        Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);

        influxDB.write(database, "", builder.build());
    }

    /**
     * 删除
     * @param command 删除语句
     * @return 返回错误信息
     */
    public String deleteMeasurementData(String command){
        QueryResult result = influxDB.query(new Query(command, database));
        return result.getError();
    }

    /**
     * 指定数据库删除
     * @param command 删除语句
     * @param database 数据库
     * @return 返回错误信息
     */
    public String deleteMeasurementData(String command, String database){
        QueryResult result = influxDB.query(new Query(command, database));
        return result.getError();
    }

    /**
     * 创建数据库
     * @param dbName
     */
    public void createDB(String dbName){
        influxDB.createDatabase(dbName);
    }

    /**
     * 删除数据库
     * @param dbName
     */
    public void deleteDB(String dbName){
        influxDB.deleteDatabase(dbName);
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenurl() {
        return openurl;
    }

    public void setOpenurl(String openurl) {
        this.openurl = openurl;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * 将查询出来的结果转化为List
     * @param command 查询SQL
     * @param database 数据库名（填null为配置文件默认）
     * @return List
     */
    public List<String> Analyze(String command, String database){
        QueryResult command1 = null;
        if(database == null || database.trim().equals("")){
            command1 = query(command);
        }else{
            command1 = query(command,database);
        }
        List<QueryResult.Result> results = command1.getResults();
        List<QueryResult.Series> series = null;
        List<String> columns = null;
        List<List<Object>> values = null;
        List<Object> value = null;
        List<String> goon = new ArrayList<String>();
//        List<Map<String,Object>> dataListMap = new ArrayList<Map<String,Object>>();
        if (results != null){
            for(QueryResult.Result qr : results){
                series = qr.getSeries();
                System.out.println(series);
                //series格式：[name=..., tags=..., columns=[...],values[...]]
            }
            for(int i=0;i<series.size();i++){
                columns = series.get(i).getColumns();
                values = series.get(i).getValues();
            }
            for(int i=0;i<values.size();i++){

                value = values.get(i);
                Map<String,Object> map = new HashMap<String, Object>();
                for (int j=0;j<value.size();j++){
                   /* if(value.get(j) == null || value.get(j).equals("")){
                        map.put(columns.get(j).toString(),null);
                    }else {
                        map.put(columns.get(j).toString(), value.get(j).toString());
                    }*/
                    map.put(columns.get(j).toString(), value.get(j)==null||value.get(j).equals("")?null:value.get(j).toString());
                }
//                dataListMap.add(map);
//                System.out.println(map.get("time"));
                JSONObject jsonObject = JSONObject.fromObject(map);
                goon.add(jsonObject.toString());
            }
        }
//        goon.forEach(x-> System.out.println(x));
        return goon;
    }
}
