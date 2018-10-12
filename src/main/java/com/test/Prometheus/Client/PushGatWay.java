package com.test.Prometheus.Client;

import com.test.ReadJSON.MDZZ_job.Bean;
import io.prometheus.client.*;
import io.prometheus.client.exporter.PushGateway;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;


public class PushGatWay {
    //Gauge仪表盘
    public static void Gauge() throws Exception {
        CollectorRegistry registry = new CollectorRegistry();
//        Gauge.Timer durationTimer = null;
        //Gauge属性的值可加可减,类似仪表盘
        Gauge gauge = Gauge.build()/*建表.name("表名").help("注解").labelNames("字段名")*/
                .name("MDZZ_gauge").help("This is a MDZZ test.").labelNames("One").labelNames("One_01").register(registry);
        try {
            // Your code here.
            //设定labelValues，字段1的值加100
            gauge.labels("T_One_T","T_One_01_T").set(21546);
            gauge.labels("T_One").inc(10);
            //字段1的值减26
            gauge.labels("T_One").dec(260);
            gauge.labels("T_One").dec(100);
            gauge.labels("T_One_01").dec(30);
            gauge.labels("T_One_01").inc(50);
//            durationTimer = gauge.startTimer();
            // This is only added to the registry after success,
            // so that a previous success in the Pushgateway isn't overwritten on failure.
            Gauge lastSuccess = Gauge.build()
                    .name("MDZZ_gauge2").help("Last time my batch job succeeded, in unixtime.").labelNames("Two").register(registry);
//            lastSuccess.setToCurrentTime();
            lastSuccess.labels("T_Two").dec(50);
            lastSuccess.labels("T_Two").inc(10);
        } finally {
//            durationTimer.setDuration();
            PushGateway pg = new PushGateway("127.0.0.1:9091");
            //真正的库名
            pg.pushAdd(registry, "gauge_job");

            System.out.println("--------------------------------"+pg.instanceIPGroupingKey());
            System.out.println("Push Gauge success!");

        }
    }

    //Histogram柱状图
    public static void Histogram() throws IOException, InterruptedException {
        // Histogram属性，类似柱状图
        Histogram histogram = Histogram.build().name("MDZZ_histogram").help("Test Histogram").labelNames("Test_host","Test_timestamp","Test_lbmname","Test_instance").buckets(50d, 100d, 500d, 1000d, 5000d).register();

        PushGateway pg = new PushGateway("localhost:9091");
        Map<String,String> map=new HashMap<String,String>();
//        map.put("a","2");
            int i=0;
            while (i<30) {
                Thread.sleep(200);
                histogram.labels("MDZZ", "2018-09-14", "LBM0001","Test_job")
                        .observe(new Random().nextInt(537));
                i++;
            pg.pushAdd(histogram, "histogram_job");
            System.out.println("Push Histogram success!");
        }
    }

    //Summary概要
    public static void Summary() throws InterruptedException, IOException {
        //同上
       Summary summary = Summary.build().name("MDZZ_summary").help("Test Summary").labelNames("Test_L01","Test_L02","Test_L03").register();
       PushGateway pg = new PushGateway("localhost:9091");
       Map<String, String> map = new HashMap<String, String>();
       map.put("WHISTHIS","OVER");
       for(int i=0;i<10;i++){
           Thread.sleep(200);
           summary.labels("TEST","TESTT","TESTTT").observe(new Random().nextInt(9875));
       }
       pg.pushAdd(summary,"summary_job",map);
       System.out.println("Push Summary success!");
   }

   //Counter计数器,只能加
   public static void Counter() throws IOException {
       CollectorRegistry registry = new CollectorRegistry();
       Counter counter = Counter.build().name("MDZZ_counter").help("Test Counter").labelNames("TestC01").labelNames("TestC02").register(registry);
       PushGateway pushGatWay = new PushGateway("localhost:9091");
       Map<String, String> map = new HashMap<String, String>();
       counter.labels("T_testC01").inc(52);
       counter.labels("T_testC02").inc(741);

       pushGatWay.pushAdd(registry,"counter_job");
       System.out.println("Push Counter success!");
   }

    //没什么卵用的删除
   public static void Delete() throws IOException {
       PushGateway PG = new PushGateway("127.0.0.1:9091");
       PG.delete("MDZZ_job");
   }


    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    //对于传回来的数据的解析
    public static void X(String s){
        JSONObject jsonObject = JSONObject.fromObject(s);
        System.out.println(jsonObject.toString());
        String data = jsonObject.getString("data");
        JSONObject jsonObject1 = JSONObject.fromObject(data);
//        System.out.println(jsonObject1.toString());
        //根据resultType判断json串的格式
        String resultType = jsonObject1.getString("resultType");
        System.out.println(resultType);
        JSONArray result = jsonObject1.getJSONArray("result");
//        System.out.println(result.toString());
        for(int a=0;a<result.size();a++){
            Bean bean = new Bean();
            JSONObject jsonObject2 = JSONObject.fromObject(result.get(a));
            String metric = jsonObject2.getString("metric");
//            System.out.println("前:"+metric);
            JSONObject JB = JSONObject.fromObject(metric);
            bean.setOne_01(JB.getString("One"));
            bean.setExported_job(JB.getString("exported_job"));
            bean.set__name__(JB.getString("__name__"));
            bean.setInstance(JB.getString("instance"));
            bean.setJob(JB.getString("job"));
            JSONArray value = jsonObject2.getJSONArray("value");
            Double d = Double.parseDouble(value.get(0).toString());
            bean.setValue(Double.parseDouble(value.get(1).toString()));


//            JSONObject jsonObject3 = JSONObject.fromObject(metric);
//            JSONObject.toBean(jsonObject3, Bean.class);
//            System.out.println("后:"+bean);
            System.out.println("----------------------------------------"+bean);
        }

//        System.out.println(jsonObject1);
    }

    /**
     * [
     *   {
     *     "metric": { "<label_name>": "<label_value>", ... },
     *     "values": [ [ <unix_time>, "<sample_value>" ], ... ]
     *   },
     *   ...
     * ]
     */
    public static void Z(String z){
        JSONObject jsonObject = JSONObject.fromObject(z);
        String data = jsonObject.getString("data");
        JSONObject jsonObject1 = JSONObject.fromObject(data);
        String resultType = jsonObject1.getString("resultType");
        System.out.println(resultType);
        JSONArray result = jsonObject1.getJSONArray("result");
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject2 = JSONObject.fromObject(result.get(i));
            String metric = jsonObject2.getString("metric");
            JSONObject jsonObject3 = JSONObject.fromObject(metric);
            JSONArray values = jsonObject2.getJSONArray("values");
            for(int j=0;j<values.size();j++){
                JSONArray ja = (JSONArray) values.get(j);
                for(int a=0;a<ja.size();a++){
                    Object o = ja.get(a);
                    String format;
                    if(o.toString().indexOf("E")>-1){
                        Double d = Double.parseDouble(o.toString());
                        format = E(d.toString());
                        System.out.println(format);
                    }else
                    System.out.println(ja.get(a));
                }
            }
        }
    }

    //解决时间为科学计数法
    public static String E(String e){
        //保留三位小数点，然并卵
//        new DecimalFormat("0.000").format(e);
        BigDecimal bd = new BigDecimal(e);
        String s1 = bd.toPlainString();

        Long time = new Date().getTime();
        String format = null;
        if(time.toString().length() > s1.length()){
            int i = time.toString().length()- bd.toPlainString().length();
            for(;0<i;i--)
                s1 = s1 + "0";
            Long date = Long.valueOf(s1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            format = sdf.format(new Date(date));
        }else
            format="核对时间位数";
        return format;
    }



    public static void main(String[] agrs){
        try{
//            Delete();
            Gauge();
            Histogram();
            Summary();
            Counter();
/*数据消失原因：代码将数据push到pushgateway中,pushgateway等待primetheus pull.pushgateway相当与内存*/
            /**
             * http://localhost:9090/api/v1/query?query={One_01=~"T_.*"}&start=2018-10-08T09:00:00Z&end=2018-10-08T12:12:12Z
             * 其中, ~"T_.*"为查询所有以T_开头的One_01标签,匹配正则,label之间通过and,or,unless进行组合
             */
            //http://127.0.0.1:9090/api/v1/query?query=MDZZ{exported_job={"MDZZ_job"}
            String s = sendGet("http://127.0.0.1:9090/api/v1/query", "query=MDZZ_gauge{exported_job=\"gauge_job\"}");
            X(s);


            //{"status" : "success","data" : {"resultType" : "matrix","result" : [{"metric" : {"__name__" : "up","job" : "prometheus","instance" : "localhost:9090"},"values" : [[ 1435781430.781, "1" ],[ 1435781445.781, "1" ],[ 1435781460.781, "1" ]]},{"metric" : {"__name__" : "up","job" : "node","instance" : "localhost:9091"},"values" : [[ 1435781430.781, "0" ],[ 1435781445.781, "0" ],[ 1435781460.781, "1" ]]}]}}
      /*      String z = "{\"status\" : \"success\",\"data\" : {\"resultType\" : \"matrix\",\"result\" : [{\"metric\" : {\"__name__\" : \"up\",\"job\" : \"prometheus\",\"instance\" : \"localhost:9090\"},\"values\" : [[ 1435781430.781, \"1\" ],[ 1435781445.781, \"1\" ],[ 1435781460.781, \"1\" ]]},{\"metric\" : {\"__name__\" : \"up\",\"job\" : \"node\",\"instance\" : \"localhost:9091\"},\"values\" : [[ 1435781430.781, \"0\" ],[ 1435781445.781, \"0\" ],[ 1435781460.781, \"1\" ]]}]}}";
            Z(z);*/



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /*CollectorRegistry registry = new CollectorRegistry();
       PushGateway pg = new PushGateway("10.25.175.121:9091");
       Gauge gauge = Gauge.build().name("my_job").help("test").labelNames("host", "logtime", "timecost").create();
       //int i=1;
       // while(true) {

           try {
               Thread.sleep(1000);
               gauge.labels("3", "2", "3");
               pg.push(gauge, "my_job");
             //  i++;
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }
          CollectorRegistry registry = new CollectorRegistry();
            Gauge duration = Gauge.build()
                        .name("my_batch_job_duration_seconds").help("Duration of my batch job in seconds.").register(registry);
            Gauge.Timer durationTimer = duration.startTimer();
            try {
                  // Your code here.
                Thread.sleep(new Random().nextInt(1000));
                // duration.labels("1","2","3").setToCurrentTime();

                  // This is only added to the registry after success,
                 // so that a previous success in the Pushgateway isn't overwritten on failure.
                  Gauge lastSuccess = Gauge.build()
                              .name("my_batch_job_last_success").help("Last time my batch job succeeded, in unixtime.").register(registry);
                  lastSuccess.setToCurrentTime();
                } finally {
                  durationTimer.setDuration();
                  PushGateway pg1 = new PushGateway("10.25.175.121:9091");
                  pg1.pushAdd(duration, "my_batch_job");
               }*/



}
