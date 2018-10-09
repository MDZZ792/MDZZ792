package com.test.Service.Impl;

import com.test.Dao.TestDao;
import com.test.Service.TestService;
import org.elasticsearch.index.query.QueryBuilder;

import javax.inject.Inject;
import java.util.List;

public class TestServiceImpl implements TestService {
    @Inject
    TestDao testDao;

    public void AddEs(String index, String type) {
        testDao.AddEs(index, type);
    }

    public void DelEs(String index, String type, String id) {

    }

    public void UpdateEs(String index, String type, String id) {

    }

    public List<String> GetEs(String index, String type, QueryBuilder query) {
        return null;
    }
}
