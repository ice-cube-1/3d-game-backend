package com.demo.websocket.example;



import jakarta.websocket.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerEndpoint extends Endpoint implements MessageHandler.Whole<String>
{
    private static final Logger LOG = LoggerFactory.getLogger(ServerEndpoint.class);
    private Session session;
    private RemoteEndpoint.Async remote;

    @Override
    public void onClose(Session session, CloseReason close)
    {
        super.onClose(session, close);
        this.session = null;
        this.remote = null;
        LOG.info("WebSocket Close: {} - {}", close.getCloseCode(), close.getReasonPhrase());
        World.connections.remove(this);
    }

    @Override
    public void onOpen(Session session, EndpointConfig config)
    {
        this.session = session;
        this.remote = this.session.getAsyncRemote();
        LOG.info("WebSocket Open: {}", session);
        session.addMessageHandler(this);
        this.remote.sendText(World.messages);
        World.connections.add(this);
    }

    @Override
    public void onError(Session session, Throwable cause)
    {
        super.onError(session, cause);
        LOG.warn("WebSocket Error", cause);
    }

    @Override
    public void onMessage(String message)
    {
        LOG.info("Added [{}]", message);
        World.messages = World.messages+"<br/>"+message;
        for (ServerEndpoint endpoint : World.connections) {
            endpoint.remote.sendText(World.messages);
        }
    }
}