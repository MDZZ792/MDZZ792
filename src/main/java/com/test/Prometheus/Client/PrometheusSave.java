package com.test.Prometheus.Client;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;

import java.util.Random;

public class PrometheusSave implements Runnable{

    String name;
    static final Gauge g = Gauge.build().name("gauge").help("blah").register();
    static final Counter c = Counter.build().name("counter").help("meh").register();
    static final Summary s = Summary.build().name("summary").help("meh").register();
    static final Histogram h = Histogram.build().name("histogram").help("meh").register();
    static final Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();

    static final Gauge Exec_Time = Gauge.build().name("Exec_Time").help("test").register();
    static final Gauge Host_IP = Gauge.build().name("Host_IP").help("test").register();
    static final Gauge Host_CpuUsage = Gauge.build().name("Host_CpuUsage").help("test").register();
    static final Gauge Host_MemUsage = Gauge.build().name("Host_MemUsage").help("test").register();
    static final Gauge Host_DiskUsage = Gauge.build().name("Host_DiskUsage").help("test").register();
    static final Gauge Host_NetInRate = Gauge.build().name("Host_NetInRate").help("test").register();
    static final Gauge Host_NetOutRate = Gauge.build().name("Host_NetOutRate").help("test").register();
    static final Gauge Api_Exec_Time = Gauge.build().name("Api_Exec_Time").help("test").register();
    static final Gauge Api_Exec_Result = Gauge.build().name("Api_Exec_Result").help("test").register();
    static final Gauge Api_Exec_Rx = Gauge.build().name("Api_Exec_Rx").help("test").register();
    static final Gauge Api_Exec_Tx = Gauge.build().name("Api_Exec_Tx").help("test").register();
    static final Gauge Api_Node = Gauge.build().name("Api_Node").help("test").register();
    static final Gauge Service_Exec_Time = Gauge.build().name("Service_Exec_Time").help("test").register();
    static final Gauge Service_Exec_Result = Gauge.build().name("Service_Exec_Result").help("test").register();
    static final Gauge Service_Exec_Rx = Gauge.build().name("Service_Exec_Rx").help("test").register();
    static final Gauge Service_Exec_Tx = Gauge.build().name("Service_Exec_Tx").help("test").register();
    static final Gauge Title = Gauge.build().name("Title").help("test").register();
    static final Gauge WarnType = Gauge.build().name("WarnType").help("test").register();
    static final Gauge WarnTime = Gauge.build().name("WarnTime").help("test").register();
    static final Gauge Jvm_Thread_CpuUsage = Gauge.build().name("Jvm_Thread_CpuUsage").help("test").register();
    static final Gauge Jvm_Thread_MemUsage = Gauge.build().name("Jvm_Thread_MemUsage").help("test").register();

    public PrometheusSave(String name) {
        this.name=name;
    }

    public void Gauge(){
        try {

            while(true){
                Thread.sleep(300);
                g.set(new Random().nextInt(50));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void Counter() {
        while(true){
            try {
                Thread.sleep(300);
                c.inc(new Random().nextInt(50));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void Summary() {
        while(true){
            try {
                Thread.sleep(300);
                s.observe(new Random().nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void Histogram(){

    }

    @Override
    public void run() {

    }

    public static void main(String[] args) throws Exception {
        /*new HTTPServer(1234);
        g.set(1);
        c.inc(2);
        s.observe(3);
        h.observe(4);
        l.labels("foo").inc(5);*/

    }
}
