package com.test.ReadJSON.MDZZ_job;

public class Bean {
    private String One_01;
    private String __name__;
    private String exported_job;
    private String instance;
    private String job;
    private Double value;


    public String getOne_01() {
        return One_01;
    }
    //注意这里的参数大小写可能会导致JSON转Bean时该字段的为null
    public void setOne_01(String one_01) {
        One_01 = one_01;
    }

    public String get__name__() {
        return __name__;
    }

    public void set__name__(String __name__) {
        this.__name__ = __name__;
    }

    public String getExported_job() {
        return exported_job;
    }

    public void setExported_job(String exported_job) {
        this.exported_job = exported_job;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "One_01='" + One_01 + '\'' +
                ", __name__='" + __name__ + '\'' +
                ", exported_job='" + exported_job + '\'' +
                ", instance='" + instance + '\'' +
                ", job='" + job + '\'' +
                ", value=" + value +
                '}';
    }
}
