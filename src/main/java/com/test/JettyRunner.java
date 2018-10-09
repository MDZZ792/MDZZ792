package com.test;

import com.google.inject.servlet.GuiceFilter;
import com.test.Prometheus.Client.JettyStatisticsCollector;
import com.test.guice.InitializeGuiceModulesContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class JettyRunner {

    private static Server server;

    public static void main(String[] args) throws Exception {
        new JettyRunner().run();
    }

    private void run() throws Exception{
        createServer();
        bindGuiceContextToServer();
        startServer();
        waitForServerToFinnish();
    }

    private void bindGuiceContextToServer() {
        ServletContextHandler context = createRootContext();
        serveGuiceContext(context);
        // Configure StatisticsHandler.
        StatisticsHandler stats = new StatisticsHandler();
        stats.setHandler(server.getHandler());
        server.setHandler(stats);
        // Register collector.注册普罗米修斯库
        new JettyStatisticsCollector(stats).register();

    }

    private void serveGuiceContext(ServletContextHandler context) {
        bindGuiceContextAndFilter(context);
        addDefaultServletToContext(context);
    }

    private void addDefaultServletToContext(ServletContextHandler context) {
        /*
         * Jetty requires some servlet to be bound to the path, otherwise request is just skipped. This prevents Guice
         * from handling the request, because it is done through filter.
         */
        context.addServlet(DefaultServlet.class, "/");
    }

    private void bindGuiceContextAndFilter(ServletContextHandler context) {
        //加载普罗米修斯库的监听
        context.addEventListener(new InitializeGuiceModulesContextListener());
        context.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    }

    private ServletContextHandler createRootContext() {
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/test");//webservice路径
        server.setHandler(context);
        return context;
    }

    private void waitForServerToFinnish() throws InterruptedException {
        server.join();
    }

    private void startServer() throws Exception {
        server.start();
    }

    private void createServer() {
        server = new Server(8080);
    }
}
