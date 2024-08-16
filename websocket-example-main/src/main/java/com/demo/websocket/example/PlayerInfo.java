package com.demo.websocket.example;

public class PlayerInfo {
    public String position = "0, 0, 0, 0, 0";
    public String weaponChoice = "0, 0, 0, 0";
    public String name = "unknown";
    public String password = "password";
    public String sendAll() {
        if (position!=null && weaponChoice!=null) {
            return "position:"+ position+" - weaponChoice:"+weaponChoice+" - name:"+name;
        }
        return "";
    }
    public Boolean checkLogin(String checkUsername, String checkPassword) {
        if (checkPassword == password && checkUsername == name) {
            return true;
        }
        return false;
    }
}
