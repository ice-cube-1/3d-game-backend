package com.demo.websocket.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
    public static List<String> messages = Collections.synchronizedList(new ArrayList<>());
    public static List<ServerEndpoint> connections = Collections.synchronizedList(new ArrayList<>());
}
