package com.test.Dao.Impl;

import com.test.Dao.TestDao;
import com.test.util.ClientES;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


public class TestDaoImpl implements TestDao {


    TransportClient client = ClientES.connectEs();
    int CloseOK = ClientES.closeEs();


    public void AddEs(String index, String type) {
        IndexResponse response = null;
        System.out.println(index);
        System.out.println(type);
        try {
            response = client.prepareIndex(index, type)
                    .setSource(jsonBuilder()
                                    .startObject()
//                            .field("ClientTime", "2018-08-20 15:20:18")
//                            .field("UploadTime", "2018-08-20 15:20:18")
//                            .field("FileName", "1534756440")
//                            .field("FileSuffix", "ts")
//                            .field("UploadRootPath", "/Volumes/hd/home/develop")
//                            .field("FileSize", "123465")
//                            .field("MetaInfo", "10000000000000008")
                                    .field("IDFieldName", 123456789L)
                                    .field("Test_Text", "555555555555555")
                                    /*.field("info_time", "1212121212121")
                                    .field("file_path", "1313131313131")
                                    .field("data_center", "3")*/
                                    .endObject()
                    )
                    .get();
//            long version = response.getVersion();
            System.out.println(CloseOK);
            System.out.println("Add to Es success!");
        } catch (IOException e) {
            System.out.println("Add to Es failure:");
            e.printStackTrace();
        }

    }

    public void DelEs(String index, String type, String id) {
        DeleteResponse response = client.prepareDelete(index, type, id)
                .get();
        long version = response.getVersion();
        System.out.println("Deleted information:"+"index:"+index + " type:"+ type + " id:"+id +" version:"+ version);
    }

    public void UpdateEs(String index, String type, String id) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index);
        updateRequest.type(type);
        updateRequest.id(id);
        try {
            updateRequest.doc(jsonBuilder()
                    .startObject()
                    // 对没有的字段添加, 对已有的字段替换
                    .field("info_time", "123456")
                    .field("file_path", "789456")
                    .endObject());
            UpdateResponse response = client.update(updateRequest).get();
            long version = response.getVersion();
            System.out.println("Updated information:"+"index:"+index +" type:"+ type +" id:"+ id+" version:"+ version);
        } catch (Exception e) {
            System.out.println("Update Es failure:");
            e.printStackTrace();
        }
    }

    public List<String> GetEs(String index, String type, QueryBuilder query) {
//        GetResponse response = client.prepareGet(index, type, nationalId)
//                .get();
        List list =new ArrayList();
        SearchResponse response = client.prepareSearch(index)// 索引名称
                .setTypes(type)// type名称
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH) // 设置查询类型，精确查询
                .setQuery(query)// 设置查询关键词
                .setFrom(0)// 分页起始条数
                .setSize(10000)// 分页数量
                .setExplain(true)// 设置是否按查询匹配度排序
                .execute().actionGet();
//        GetResponse response = client.prepareGet("twitter", "tweet", "1")
//                .setOperationThreaded(false)    // 线程安全
//                .get();
//        System.out.println("Get information:"+response);
        list =showResult(response);
        return list;
    }

    public List<String> GetSomEs(String index, String type,QueryBuilder query) {
        List list =new ArrayList();
        SearchResponse response = client.prepareSearch(index)// 索引名称
                .setTypes(type)// type名称
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH) // 设置查询类型，精确查询
                .setQuery(QueryBuilders.matchAllQuery())//查询所有
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("HostName", "yunzhongnxin"))//查询uquestion为139的
                        .must(QueryBuilders.matchQuery("Time", "2018-09-07 15:20:18")))//查询省份为江苏的
                .setFrom(0)// 分页起始条数
                .setSize(10000)// 分页数量
                .setExplain(true)// 设置是否按查询匹配度排序
                .execute().actionGet();
        list =showResult(response);
        return list;
    }

    /**
     *格式化查询结果
     */
    private List<String> showResult(SearchResponse response) {
        List list =new ArrayList();
        SearchHits searchHits = response.getHits();
//        float maxScore = searchHits.getMaxScore();  // 查询结果中的最大文档得分
//        System.out.println("maxScore: " + maxScore);
//        long totalHits = searchHits.getTotalHits(); // 查询结果记录条数
//        System.out.println("totalHits: " + totalHits);
        SearchHit[] hits = searchHits.getHits();    // 查询结果
        System.out.println("Number of queries: " + hits.length);
        for (SearchHit hit : hits) {
//            String id = hit.getId();
//            String index = hit.getIndex();
//            String type = hit.getType();
//            float score = hit.getScore();
//            System.out.println("===================================================");
            String source = hit.getSourceAsString();
//            System.out.println("id: " + id);
//            System.out.println("index: " + index);
//            System.out.println("type: " + type);
//            System.out.println("score: " + score);
//            System.out.println(source);
//            JSONObject jsonObject = JSONObject.fromObject(source);
//            String b =jsonObject.getString("FileName");
            list.add(source);

        }
        return list;
    }



}
