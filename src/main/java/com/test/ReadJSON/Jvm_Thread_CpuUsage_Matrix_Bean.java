package com.test.ReadJSON;

/**
 * @ClassName Jvm_Thread_CpuUsage_Matrix_Bean
 * @Atuhor MDZZ_792 (｢･ω･)｢ 嘿
 * @Time 上午10:01 18-10-16
 */
public class Jvm_Thread_CpuUsage_Matrix_Bean {
    private String Host_IP;
    private String __name__;
    private String instance;
    private String job;

    public String getHost_IP() {
        return Host_IP;
    }

    public void setHost_IP(String Host_IP) {
        this.Host_IP = Host_IP;
    }

    public String get__name__() {
        return __name__;
    }

    public void set__name__(String __name__) {
        this.__name__ = __name__;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "Jvm_Thread_CpuUsage_Matrix_Bean{" +
                "Host_IP='" + Host_IP + '\'' +
                ", __name__='" + __name__ + '\'' +
                ", instance='" + instance + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
