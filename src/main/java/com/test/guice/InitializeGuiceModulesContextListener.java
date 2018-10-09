package com.test.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

//监听
public class InitializeGuiceModulesContextListener extends GuiceServletContextListener {


    protected Injector getInjector() {
        return Guice.createInjector(new BindServicesModule(), new BindJerseyResourceModule());
    }
}
