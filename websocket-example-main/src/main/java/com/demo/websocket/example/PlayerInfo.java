package com.demo.websocket.example;

public class PlayerInfo {
    public String position = "0, 0, 0, 0, 0";
    public String weaponChoice = "0, 0, 0, 0, 0";
    public String armourChoice = "0, 0, 0, 0, 3";
    public String name = "unknown";
    public String password = "password";
    public String hp = "40";
    public String color = "255, 255, 255, 255";
    public String sendAll() {
            return "position:"+ position+" - weaponChoice:"+weaponChoice+" - name:"+name+" - hp:"+hp+" - weaponChoice:"+armourChoice +" - color:"+color;
    }
}
