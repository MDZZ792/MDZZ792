package com.test.Dao;

public interface ESDao {

  /*
    void connectEs();*/

    /**
     * 创建索引(建库)
     */
    void createIndex();

    /**
     * 删除Index下的某个Type(删表)
     */
    void deleteType();

    /**
     * 定义索引的映射类型(创建字段)
     */
    void defineIndexTypeMapping();
}
