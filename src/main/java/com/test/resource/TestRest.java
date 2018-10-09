package com.test.resource;

import com.test.Dao.ESDao;
import com.test.Service.InfluxdbService;
import com.test.Service.TestService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("TestRest")
public class TestRest {
    @Inject
    TestService testService;

    @Inject
    ESDao esDao;

    @Inject
    InfluxdbService influxdbService;

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
                              @QueryParam("time2") String time2){
        List<String> strings = influxdbService.GetData();
//        System.out.println(time1+","+time2);
        return strings.toString();
    }

    @GET
    @Path("GetTime")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String GetTimeData( @QueryParam("time1") String time1,
                               @QueryParam("time2") String time2){
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
    public String GetTime( @QueryParam("t1") String t1,
                           @QueryParam("t2") String t2){
        return "";
    }
}
