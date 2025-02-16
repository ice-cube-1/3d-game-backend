package com.demo.websocket.example;



import jakarta.websocket.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.String.valueOf;


public class ServerEndpoint extends Endpoint implements MessageHandler.Whole<String>
{
    private static final Logger LOG = LoggerFactory.getLogger(ServerEndpoint.class);
    private Session session;
    private RemoteEndpoint.Async remote;
    public PlayerInfo stats = new PlayerInfo();
    private String id;

    @Override
    public void onClose(Session session, CloseReason close)
    {
        super.onClose(session, close);
        LOG.info("WebSocket Close: {} - {}", close.getCloseCode(), close.getReasonPhrase());
        this.stats.writeStats(this.stats.name);
        World.messages.add(this.stats.name+" has left");
        sendMessageToOthers(this.stats.name+" has left", "message", id);
        sendMessageToOthers("a","remove",this.id);
        World.connections.remove(this);

    }

    @Override
    public void onOpen(Session session, EndpointConfig config)
    {
        this.session = session;
        this.remote = this.session.getAsyncRemote();
        this.id = valueOf(World.connections.size());
        LOG.info("WebSocket Open: {}", session);
        session.addMessageHandler(this);
        sendMessage(this.id, "id", this.id);
        if (World.messages.size() <= 40) {
            sendMessageListToClient(World.messages,"message", this.id);
        } else {
            sendMessageListToClient(World.messages.subList(World.messages.size() - 40, World.messages.size()),"message", this.id);
        }
        for (ServerEndpoint player: World.connections) {
            sendMessage(player.stats.sendAll(), "playerStats", player.id);
        }
        sendMessageToOthers(this.stats.sendAll(), "playerStats", this.id);
        sendMessageListToClient(Terrain.readBlocks(),"blocks", this.id);
        sendMessageListToClient(Terrain.readWeapon(),"weapon",this.id);
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
        String[] parts = message.split(": ", 3); // 0: type, 1: content
        switch (parts[1]) {
            case "message":
                handleMessage(parts[2], parts[0]);
                break;
            case "position":
                updatePosition(parts[2], parts[0]);
                break;
            case "weaponPickup":
                updateWeapon(parts[2], parts[0]);
                break;
            case "zspeed":
                sendMessageToOthers(parts[2], "zspeed", parts[0]);
                break;
            case "weaponPos":
                sendMessageToOthers(parts[2], parts[1], parts[0]);
                break;
            case "login":
                handleLogin(parts[2], parts[0]);
                break;
            case "hp":
                sendMessageToOthers(parts[2], "hp", parts[0]);
                this.stats.hp = parts[2];
                break;
            case "moveItem":
                moveItem(parts[2]);
                break;
            case "color":
                this.stats.color = parts[2];
                sendMessageToOthers(parts[2], parts[1], parts[0]);
                break;
            case "kills":
                this.stats.kills = parts[2];
                sendMessageToOthers(parts[2], parts[1], parts[0]);
                break;
            default:
                break;
        }
    }
    public void updatePosition(String position, String id) {
        this.stats.position = position;
        sendMessageToOthers(position, "position", id);
    }
    public void updateWeapon(String weaponInfo, String id) {
        String[] parts = weaponInfo.split(" - ", 2);
        Terrain.removeWeapon(parts[0]);
        Terrain.addWeapon(parts[1]);
        if (parts[0].substring(parts.length - 1).equals("3")) {
            this.stats.armourChoice = parts[0];
        } else {
            this.stats.weaponChoice = parts[0];
        }
        sendMessageToOthers(weaponInfo, "weaponPickup", id);
    }
    public void handleMessage(String message, String id) {
        World.messages.add(message);
        sendMessageToOthers(message, "message", id);
    }

    public void handleLogin(String loginInfo, String id) {
        List<String> userPass = List.of(loginInfo.split(" "));
        if (userPass.size() == 1) {
            sendMessage("no password", "login", "0");
            return;
        }
        if (!Objects.equals(this.stats.name, "unknown")) {
            sendMessage("logged in", "login", "0");
            return;
        }
        String result = this.stats.readStats(userPass.get(0),userPass.get(1));
        switch (result) {
            case "correct":
                sendMessage(this.id, "login", this.id);
                sendMessage(this.stats.sendAll(), "playerStats", "-1");
                sendMessageToOthers(this.stats.name, "namechange", this.id);
                break;
            case "incorrect":
                sendMessage("incorrect password", "login", "0");
                break;
            case "new":
                this.stats.name = userPass.getFirst();
                this.stats.password = userPass.get(1);
                sendMessage(this.stats.sendAll(), "playerStats", "-1");
                sendMessageToOthers(this.stats.name, "namechange", this.id);
                this.stats.writeStats(this.stats.name);
                break;
        }
    }

    public void moveItem(String currentCoords) {
        String out = "0: moveItem: "+currentCoords+" - "+Terrain.emptySquare();
        for (ServerEndpoint endpoint: World.connections) {
            endpoint.remote.sendText(out);
        }

    }
    public void sendMessageToOthers(String message, String type, String id)
    {
        for (ServerEndpoint endpoint: World.connections) {
            if (endpoint.remote != this.remote) {
                endpoint.remote.sendText(id+": "+type+": "+message);
            }
        }
    }
    public void sendMessageListToClient(List<String> messages, String type, String id) {
        for (String message: messages) {
            this.remote.sendText(id+": "+type+": "+message);
        }
    }
    public void sendMessage(String message, String type, String id) {
        if (message!="") {
            this.remote.sendText(id + ": " + type + ": " + message);
        }
    }
}