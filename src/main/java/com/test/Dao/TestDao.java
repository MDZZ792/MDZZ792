package com.test.Dao;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;

public interface TestDao {


    /**
     * 增
     */
    void AddEs(String index, String type);

    /**
     * 删
     */
    void DelEs(String index, String type, String id);
    /**
     * 改
     */
    void UpdateEs(String index, String type, String id);
    /**
     * 查
     */
    List<String> GetEs(String index, String type, QueryBuilder query);
}
