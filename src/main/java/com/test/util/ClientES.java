package com.test.util;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class ClientES {
    private static TransportClient client;
    //加载properties文件
    private  static Properties properties = PropertiesUtil.loadProperties("ESUrl.properties");


    //连接ES数据库
    public static TransportClient connectEs() {
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(properties.getProperty("ElasticSearch.url")), 9300));
//            System.out.println("Connection Es success!");
            return client;
        } catch (UnknownHostException e) {
            System.out.println("Connection Es failed：");
            e.printStackTrace();
            return client;
        }

    }

    public static int closeEs(){
        try{
            client.close();
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    public static void main(String[] agrs){
        connectEs();
    }

}
