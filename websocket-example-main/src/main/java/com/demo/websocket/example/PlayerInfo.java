package com.demo.websocket.example;

public class PlayerInfo {
    public String position = "0, 0, 0, 0, 0";
    public String weaponChoice = "0, 0, 0, 0";
    public String sendAll() {
        if (position!=null && weaponChoice!=null) {
            return "position:"+ position+" - weaponChoice:"+weaponChoice;
        }
        return "";
    }
}
