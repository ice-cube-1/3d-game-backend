package com.demo.websocket.example;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerInfo {
    public String position = "0, 0, 0, 0, 0";
    public String weaponChoice = "0, 0, 0, 0, 0";
    public String armourChoice = "0, 0, 0, 0, 3";
    public String name = "unknown";
    public String password = "password";
    public String hp = "40";
    public String color = "255, 255, 255, 255";
    public String kills = "0";

    private ArrayList<ArrayList<String>> getAllInfo() {
        ArrayList<String> currentPlayers = files.read("players.txt");
        ArrayList<ArrayList<String>> out = new ArrayList<>();
        for (String i : currentPlayers) {
            ArrayList<String> temp = new ArrayList<>();
            for (String j: i.split(" - "))
            {
                temp.add(j.substring(j.indexOf(':')+1));
            }
            out.add(temp);
        }
        return out;
    }

    public String readStats(String name, String password) {
        ArrayList<ArrayList<String>> allInfo = getAllInfo();
        for (ArrayList<String> player: allInfo) {
            if (Objects.equals(player.get(2), name)) {
                if  (Objects.equals(player.get(6), password)) {
                    System.out.println();
                    this.position = player.get(0);
                    this.weaponChoice = player.get(1);
                    this.name = player.get(2);
                    this.hp = player.get(3);
                    this.armourChoice = player.get(4);
                    this.color = player.get(5);
                    this.password = player.get(6);
                    this.kills = player.get(7);
                    return "correct";
                }
                return "incorrect";
            }
        }
        return "new";
    }
    public void writeStats(String name) {
        ArrayList<String> currentPlayers = files.read("players.txt");
        for (int i = 0; i<currentPlayers.size(); i++) {
            String[] data = currentPlayers.get(i).split(" - ");
            if (data[2].substring(5).equals(name)) {
                currentPlayers.set(i,this.sendAll());
                files.write("players.txt",currentPlayers);
                return;
            }
        }
        currentPlayers.add(this.sendAll());
        files.write("players.txt",currentPlayers);
    }


    public String sendAll() {
            return "position:"+ position+" - weaponChoice:"+weaponChoice+" - name:"+name+" - hp:"+hp+" - weaponChoice:"+armourChoice +" - color:"+color+" - password:"+password+" - kills:"+kills;
    }
}
