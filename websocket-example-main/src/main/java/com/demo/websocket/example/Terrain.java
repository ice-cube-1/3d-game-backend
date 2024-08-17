package com.demo.websocket.example;

import java.io.File;
import java.util.*;

public class Terrain {
    // Static fields for global state
    static List<String> weapons = new ArrayList<>();
    static List<String> blocks = new ArrayList<>();
    static Random random = new Random();
    static int GRIDSIZE = 80;
    public Terrain() {
        initializeTerrain();
        files.write("weapons.txt",weapons);
        files.write("blocks.txt",blocks);
    }

    // Method to initialize the terrain
    private void initializeTerrain() {
        for (int i = -GRIDSIZE / 2; i < GRIDSIZE / 2; i += 2) {
            for (int j = -GRIDSIZE / 2; j < GRIDSIZE / 2; j += 2) {
                addMore(i, 0, j);
            }
        }
    }

    private void addMore(int i, int z, int j) {
        double x = random.nextDouble();
        blocks.add(i + ", " + z + ", " + j);
        if (x < 0.1) {
            addMore(i, z + 2, j); // Recursive call
        } else if (x < 0.15) {
            weapons.add(i + ", " + z+2 + ", " + j + ", " + Math.floor(random.nextDouble() * 5)+", "+Math.floor(random.nextDouble() * 5));
        }
    }
    public static List<String> readWeapon() {
        return files.read("weapons.txt");
    }
    public static List<String> readBlocks() {
        return files.read("blocks.txt");
    }
    public static void addWeapon(String weapon) {
        List<String> weapons = files.read("weapons.txt");
        weapons.add(weapon);
        files.write("weapons.txt", weapons);
    }
    public static void removeWeapon(String weapon) {
        List<String> weapons = files.read("weapons.txt");
        weapons.remove(weapon);
        files.write("weapons.txt", weapons);
    }
    public static String emptySquare() {
        ArrayList<Integer> newCoords = new ArrayList<>();
        newCoords.add((int) Math.floor(random.nextDouble() * GRIDSIZE - GRIDSIZE / 2));
        newCoords.add((int) Math.floor(random.nextDouble() * GRIDSIZE - GRIDSIZE / 2));
        int z = 0;
        for (String block : blocks) {
            List<Integer> coords = Arrays.stream(block.split(", ")).map(Integer::parseInt).toList();
            if (Objects.equals(coords.get(0), newCoords.get(0)) && Objects.equals(coords.get(1), newCoords.get(1))) {
                z = coords.get(2);
            }
        }
        return newCoords.get(0) +", "+z+2+", "+newCoords.get(1);
    }
}
