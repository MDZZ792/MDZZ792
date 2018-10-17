package com.test.resource;

import com.test.Dao.ESDao;
import com.test.Service.InfluxdbService;
import com.test.Service.PrometheusService;
import com.test.Service.TestService;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Path("TestRest")
public class TestRest {

    static final Gauge g = Gauge.build().name("gauge").help("blah").register();
    static final Counter c = Counter.build().name("counter").help("meh").register();
    static final Summary s = Summary.build().name("summary").help("meh").register();
    static final Histogram h = Histogram.build().name("histogram").help("meh").register();
    static final Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();


    static final Gauge Exec_Time = Gauge.build().name("Exec_Time").help("test").labelNames("Host_IP").register();
    static final Gauge Host_CpuUsage = Gauge.build().name("Host_CpuUsage").labelNames("Host_IP").help("test").register();
    static final Gauge Host_MemUsage = Gauge.build().name("Host_MemUsage").labelNames("Host_IP").help("test").register();
    static final Gauge Host_DiskUsage = Gauge.build().name("Host_DiskUsage").help("test").labelNames("Host_IP").register();
    static final Gauge Host_NetInRate = Gauge.build().name("Host_NetInRate").help("test").labelNames("Host_IP").register();
    static final Gauge Host_NetOutRate = Gauge.build().name("Host_NetOutRate").help("test").labelNames("Host_IP").register();
    static final Gauge Api_Exec_Time = Gauge.build().name("Api_Exec_Time").help("test").labelNames("Host_IP").register();
    static final Gauge Api_Exec_Result = Gauge.build().name("Api_Exec_Result").help("test").labelNames("Host_IP").register();
    static final Gauge Api_Exec_Rx = Gauge.build().name("Api_Exec_Rx").help("test").labelNames("Host_IP").register();
    static final Gauge Api_Exec_Tx = Gauge.build().name("Api_Exec_Tx").help("test").labelNames("Host_IP").register();
    static final Gauge Api_Node = Gauge.build().name("Api_Node").help("test").labelNames("Host_IP").register();
    static final Gauge Service_Exec_Time = Gauge.build().name("Service_Exec_Time").help("test").labelNames("Host_IP").register();
    static final Gauge Service_Exec_Result = Gauge.build().name("Service_Exec_Result").help("test").labelNames("Host_IP").register();
    static final Gauge Service_Exec_Rx = Gauge.build().name("Service_Exec_Rx").help("test").labelNames("Host_IP").register();
    static final Gauge Service_Exec_Tx = Gauge.build().name("Service_Exec_Tx").help("test").labelNames("Host_IP").register();
    static final Gauge Title = Gauge.build().name("Title").help("test").labelNames("Host_IP").register();
    static final Gauge WarnType = Gauge.build().name("WarnType").help("test").labelNames("Host_IP").register();
    static final Gauge WarnTime = Gauge.build().name("WarnTime").help("test").labelNames("Host_IP").register();
    static final Gauge Jvm_Thread_CpuUsage = Gauge.build().name("Jvm_Thread_CpuUsage").labelNames("Host_IP").help("test").register();
    static final Gauge Jvm_Thread_MemUsage = Gauge.build().name("Jvm_Thread_MemUsage").labelNames("Host_IP").help("test").register();


    @Inject
    private TestService testService;

    @Inject
    private ESDao esDao;

    @Inject
    private InfluxdbService influxdbService;

    @Inject
    private PrometheusService prometheusService;

    @GET
    @Path("1")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String stest(){
        esDao.createIndex();
        esDao.defineIndexTypeMapping();
        return "";
    }

    @GET
    @Path("TestAdd")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String  TestAdd(){
        testService.AddEs("indexname","TypeName");
        return "";
    }

    @GET
    @Path("{TestInflux}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String GetAllData( @QueryParam("time1") String time1,
                              @QueryParam("time2") String time2,
                              @PathParam("TestInflux") String TestInflux){
        List<String> strings = influxdbService.GetData();
//        System.out.println(time1+","+time2);
        return strings.toString();
    }

    @GET
    @Path("GetTime1")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String GetTimeData( @QueryParam("time1") String time1,
                               @QueryParam("time2") String time2
                              ){
        List<String> strings = influxdbService.GetTimeData(time1, time2);

        return strings.toString();
    }

    @GET
    @Path("GetNum")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String GetNum(){
        String getNum = influxdbService.GetNum();
        return getNum;
    }

    @GET
    @Path("GetParam")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String GetParam( @QueryParam("a1") String a1,
                            @QueryParam("a2") String a2){
        List<String> strings = influxdbService.GetLimit(a1, a2);
        return strings.toString();
    }

    @GET
    @Path("GetTime")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String GetTime(@QueryParam("t1") String t1,
                          @QueryParam("t2") String t2,
                          @FormDataParam("t3") String t3/*?*/){

        String[] IP = {"192.168.159.168","193.168.159.168","194.168.159.168","195.168.159.168","196.168.159.168"};
        while (true) {
            try {
                Thread.sleep(500);

                Exec_Time.labels(IP[RandomNum(IP)]).set(new Random().nextInt(20));
                Host_CpuUsage.labels(IP[RandomNum(IP)]).set(new Random().nextInt(100));
                Host_MemUsage.labels(IP[RandomNum(IP)]).set(new Random().nextInt(100));
                Host_DiskUsage.labels(IP[RandomNum(IP)]).set(new Random().nextInt(100));
                Host_NetInRate.labels(IP[RandomNum(IP)]).set(new Random().nextInt(50000));
                Host_NetOutRate.labels(IP[RandomNum(IP)]).set(new Random().nextInt(50000));
                Api_Exec_Time.labels(IP[RandomNum(IP)]).set(new Random().nextInt(20));
                Api_Exec_Result.labels(IP[RandomNum(IP)]).set(new Random().nextInt(2));
                Api_Exec_Rx.labels(IP[RandomNum(IP)]).set(new Random().nextInt(50));
                Api_Exec_Tx.labels(IP[RandomNum(IP)]).set(new Random().nextInt(50));
                Api_Node.labels(IP[RandomNum(IP)]).set(new Random().nextInt(50));
                Service_Exec_Time.labels(IP[RandomNum(IP)]).set(new Random().nextInt(20));
                Service_Exec_Result.labels(IP[RandomNum(IP)]).set(new Random().nextInt(2));
                Service_Exec_Rx.labels(IP[RandomNum(IP)]).set(new Random().nextInt(50));
                Service_Exec_Tx.labels(IP[RandomNum(IP)]).set(new Random().nextInt(50));
                Title.labels(IP[RandomNum(IP)]).set(new Random().nextInt(50));
                WarnType.labels(IP[RandomNum(IP)]).set(new Random().nextInt(50));
                WarnTime.labels(IP[RandomNum(IP)]).set(0);
                Jvm_Thread_CpuUsage.labels(IP[RandomNum(IP)]).set(new Random().nextInt(100));
                Jvm_Thread_MemUsage.labels(IP[RandomNum(IP)]).set(new Random().nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "BOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOM";
            }

        }

        /*while(true){
            try {
                Thread.sleep(300);
                c.inc(new Random().nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(true){
            try {
                Thread.sleep(300);
                s.observe(new Random().nextInt(10));
                s.startTimer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(true){
            try {
                Thread.sleep(300);
                h.observe(new Random().nextInt(5));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }

    public int RandomNum(String[] Array){
        int index = (int) (Math.random() * Array.length);
        return index;
    }

    @GET
    @Path("TestAPI")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String TestAPI( @QueryParam("start") String start,
                           @QueryParam("end") String end,
                           @QueryParam("step") String step){
        //1.http://127.0.0.1:9090/api/v1/query?query=Jvm_Thread_CpuUsage
        //2.http://127.0.0.1:9090/api/v1/query?query=Jvm_Thread_CpuUsage[5m]
        //3.http://127.0.0.1:9090/api/v1/query?query=Jvm_Thread_CpuUsage offset 4h
        //4.http://127.0.0.1:9090/api/v1/query?query=Jvm_Thread_CpuUsage[5m] offset 4h
        //5.http://127.0.0.1:9090/api/v1/query_range?query=sum(histogram_bucket)&start=2018-10-12T08:00:00Z&end=2018-10-12T15:10:52.781Z&step=15s
        //6.http://127.0.0.1:9090/api/v1/query_range?query=sum(histogram_bucket offset 10m)&start=2018-10-12T08:00:00Z&end=2018-10-12T15:10:52.781Z&step=15s
        //7.http://127.0.0.1:9090/api/v1/query_range?query=count_over_time(Jvm_Thread_CpuUsage{Host_IP="192.168.159.168"} [5m])&start=2018-10-15T00:10:00Z&end=2018-10-15T11:00:00Z&step=15s
        String s = prometheusService.GetJvm_Thread_CpuUsage(start, end, step);
        System.out.println(s);

        return "{\"result\":\""+s+"\"}";
    }
}
