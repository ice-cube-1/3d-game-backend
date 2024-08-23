package com.demo.websocket.example;

import jakarta.websocket.server.ServerEndpointConfig;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;

import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class Server {
    public static void main(String[] args) throws Exception
    {
        new Terrain();
        org.eclipse.jetty.server.Server server = newServer(8080);
        server.start();
        server.join();
    }
    public static org.eclipse.jetty.server.Server newServer(int port)
    {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(new InetSocketAddress("0.0.0.0", port));
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);
        JakartaWebSocketServletContainerInitializer.configure(servletContextHandler, (_, container) ->
        {
            ServerEndpointConfig serverConfig = ServerEndpointConfig.Builder.create(ServerEndpoint.class, "/socket").build();
            container.addEndpoint(serverConfig);
        });
        URL urlStatics = Thread.currentThread().getContextClassLoader().getResource("server-root/index1.html");
        Objects.requireNonNull(urlStatics, "Unable to find index1.html in classpath");
        String urlBase = urlStatics.toExternalForm().replaceFirst("/[^/]*$", "/");
        ServletHolder defHolder = new ServletHolder("default", new DefaultServlet());
        defHolder.setInitParameter("resourceBase", "C:\\git\\webgl-random\\web");
        defHolder.setInitParameter("dirAllowed", "true");
        defHolder.setInitParameter("cacheControl", "no-store, no-cache, must-revalidate, max-age=0");
        defHolder.setInitParameter("useFileMappedBuffer", "false");
        servletContextHandler.addServlet(defHolder, "/");
        return server;
    }
}
