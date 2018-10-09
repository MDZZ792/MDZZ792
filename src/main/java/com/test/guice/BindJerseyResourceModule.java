package com.test.guice;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.Set;

public class BindJerseyResourceModule extends ServletModule {


    protected void configureServlets(){
        bindResources();
        serveBoundResources();
    }

    private void bindResources() {
        for (Class<?> resource : lookupResources()) {
            bind(resource);
        }
    }

    private Set<Class<?>> lookupResources() {
        //接口包名
        PackagesResourceConfig resourceConfig = new PackagesResourceConfig("com.test.resource");
//        register(MultiPartFeature.class);
        return resourceConfig.getClasses();
    }

    private void serveBoundResources() {
        serve("/*").with(GuiceContainer.class);
    }
}
