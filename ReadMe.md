框架集成
    包括Jersey,Jetty,RestAPI,ES,Prometheus,Influxdb,Etcd,以及一些JSON格式的解析
    
    Prometheus:
       一、query：为普通查询，可查询单个数据，没有时间范围，但是可以指定某一时间进行查询
       eg：http://127.0.0.1:9090/api/v1/query?query=counter&time=2018-10-12T06:37:06Z
           其中问号前的query表示普通查询,counter表示Counter.build().name("counter")中的name。
       二、query_range：为范围查询，可查询一定范围内的数据，step值不可设置过小
       eg：http://127.0.0.1:9090/api/v1/query_range?query=sum(gauge{job="test_save"})&start=2018-10-12T00:00:00Z
           &end=2018-10-12T15:10:52.781Z&step=15s
           其中query_range表示范围查询，gauge同上,{}中为标签，start为起始时间，end为结束时间，step为查询间隔，该参数影响
           查询结果值。