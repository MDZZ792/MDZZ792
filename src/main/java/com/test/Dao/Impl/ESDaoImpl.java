package com.test.Dao.Impl;

import com.test.Dao.ESDao;
import com.test.util.ClientES;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

public class ESDaoImpl implements ESDao {
 /*   TransportClient client;
    //加载properties文件
    Properties properties = PropertiesUtil.loadProperties("ESUrl.properties");


    //连接数据库
    public void connectEs() {
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(properties.getProperty("ElasticSearch.url")), 9300));
            System.out.println("Connection Es success!");
        } catch (UnknownHostException e) {
            System.out.println("Connection Es failed：");
            e.printStackTrace();
        }
    }*/
    TransportClient client = ClientES.connectEs();
    int CloseOK = ClientES.closeEs();


    // 创建索引(建库)
    public void createIndex() {
        client.admin().indices().create(new CreateIndexRequest("indexname"))
                .actionGet();
    }

    // 删除Index下的某个Type(删表)
    public void deleteType(){
        client.prepareDelete().setIndex("IndexName").setType("TypeName").execute().actionGet();
    }

    // 定义索引的映射类型(创建字段)
    public void defineIndexTypeMapping() {
        try {
            XContentBuilder mapBuilder = XContentFactory.jsonBuilder();
            mapBuilder.startObject()
                    .startObject("TypeName")
                    .startObject("_all").field("enabled", false).endObject()
                    .startObject("properties")
                    .startObject("IDFieldName").field("type", "long").endObject()
//                    .startObject("Test_Not_analyzed").field("type", "string").field("index", "not_analyzed").endObject()
//                    .startObject("Test_Analyzed").field("type","String").field("index","analyzed").endObject()
                    .startObject("Test_Text").field("type","text").endObject()
                    .startObject("Test_Keyword").field("type", "keyword").endObject()
                    .startObject("Test_Store").field("type","text").endObject()
                    .startObject("Test_Sort").field("type","text").endObject()
                    .startObject("Test_Store1").field("type","keyword")/*.field("store","yes")*/.endObject()
                    .startObject("Test_Sort1").field("type","keyword")/*.field("sort","yes")*/.endObject()
                    .endObject()
                    .endObject()
                    .endObject();

            PutMappingRequest putMappingRequest = Requests
                    .putMappingRequest("indexname").type("TypeName")
                    .source(mapBuilder);
            client.admin().indices().putMapping(putMappingRequest).actionGet();
        } catch (IOException e) {
//            log.error(e.toString());
        }
    }
}
