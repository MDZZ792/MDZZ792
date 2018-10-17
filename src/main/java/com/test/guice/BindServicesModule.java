package com.test.guice;


import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletScopes;
import com.test.Dao.ESDao;
import com.test.Dao.Impl.ESDaoImpl;
import com.test.Dao.Impl.InfluxdbDaoImpl;
import com.test.Dao.Impl.PrometheusDaoImpl;
import com.test.Dao.Impl.TestDaoImpl;
import com.test.Dao.InfluxdbDao;
import com.test.Dao.PrometheusDao;
import com.test.Dao.TestDao;
import com.test.Service.Impl.InfluxdbServiecImpl;
import com.test.Service.Impl.PrometheusServiceImpl;
import com.test.Service.Impl.TestServiceImpl;
import com.test.Service.InfluxdbService;
import com.test.Service.PrometheusService;
import com.test.Service.TestService;

public class BindServicesModule extends AbstractModule {

//添加Service到Dao的映射关系
    protected void configure() {
        bind(TestDao.class).to(TestDaoImpl.class).in(ServletScopes.REQUEST);
        bind(TestService.class).to(TestServiceImpl.class).in(ServletScopes.REQUEST);
        bind(ESDao.class).to(ESDaoImpl.class).in(ServletScopes.REQUEST);
        bind(InfluxdbDao.class).to(InfluxdbDaoImpl.class).in(ServletScopes.REQUEST);
        bind(InfluxdbService.class).to(InfluxdbServiecImpl.class).in(ServletScopes.REQUEST);
        bind(PrometheusDao.class).to(PrometheusDaoImpl.class).in(ServletScopes.REQUEST);
        bind(PrometheusService.class).to(PrometheusServiceImpl.class).in(ServletScopes.REQUEST);
    }
}
