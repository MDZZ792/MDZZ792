package com.test;

import com.google.inject.servlet.GuiceFilter;
import com.test.guice.InitializeGuiceModulesContextListener;
import io.prometheus.client.exporter.HTTPServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.io.IOException;
import java.util.EnumSet;

public class JettyRunner {

    private static Server server;
    private static StatisticsHandler stats;

    //该静态块只为将9100端口暴露给prometheus，以便拉取数据。
    static {
        try {
            new HTTPServer(9100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new JettyRunner().run();
    }

    private void run() throws Exception{
        createServer();
        bindGuiceContextToServer();
        //prometheus收集jetty数据
//        Collect();
        startServer();
        waitForServerToFinnish();

    }

    private void bindGuiceContextToServer() {
        ServletContextHandler context = createRootContext();
        serveGuiceContext(context);
        // Configure StatisticsHandler.
        stats = new StatisticsHandler();
        stats.setHandler(server.getHandler());
        server.setHandler(stats);
        // Register collector.注册普罗米修斯库
//        new JettyStatisticsCollector(stats).register();

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
        //加载监听
        context.addEventListener(new InitializeGuiceModulesContextListener());
        //加载过滤器
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

//    private void Collect(){
//        new JettyStatisticsCollector(stats).collect();
//    }

    private void startServer() throws Exception {
        server.start();
    }

    private void createServer() {
        server = new Server(8080);
    }
}
