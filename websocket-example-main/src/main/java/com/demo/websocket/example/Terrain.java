package com.demo.websocket.example;

import java.io.File;
import java.util.*;

public class Terrain {
    // Static fields for global state
    static List<String> weapons = new ArrayList<>();
    static List<String> blocks = new ArrayList<>();
    static Random random = new Random();
    static int GRIDSIZE = 200;

    public Terrain() {
        File file = new File("players.txt");
        if (!file.exists()) {
            initializeTerrain();
            files.write("players.txt",new ArrayList<>());
            files.write("weapons.txt", weapons);
            files.write("blocks.txt", blocks);
        }
    }

    ArrayList<ArrayList<Integer>> columns = new ArrayList<>();

    // Method to initialize the terrain
    private void initializeTerrain() {
        for (int i = 0; i < GRIDSIZE / 2; i += 1) {
            columns.add(new ArrayList<>());
            for (int j = 0; j < GRIDSIZE / 2; j += 1) {
                columns.get(i).add(0);
            }
        }
        for (int i = 0; i < 500; i++) {
            Integer[] seed = {(int) Math.floor(random.nextDouble() * GRIDSIZE/2), (int) Math.floor(random.nextDouble() * GRIDSIZE/2)};
            columns = addMore(columns, seed);
        }
        for (int i = 0; i < GRIDSIZE / 2; i += 1) {
            for (int j = 0; j < GRIDSIZE / 2; j += 1) {
                for (int k = 0; k <= columns.get(i).get(j); k++) {
                    blocks.add((i*2-GRIDSIZE/2) + ", " + k*2 + ", " + (j*2-GRIDSIZE/2));
                }
                if (random.nextDouble() < 0.01) {
                    addWeapon(i*2-GRIDSIZE/2, columns.get(i).get(j)*2 + 2, j*2-GRIDSIZE/2);
                }
            }
        }
    }
    private void addWeapon(int i,int z,int j) {
        int rarity = 0;
        if (random.nextDouble() > 0.5) {
            if (random.nextDouble() > 0.5) {
                if (random.nextDouble() > 0.5) {
                    if (random.nextDouble() > (double) 2/3) {
                        rarity = 4;
                    } else {
                        rarity = 3;
                    }
                } else {
                    rarity = 2;
                }
            } else {
                rarity = 1;
            }
        }
        int type = (int) Math.floor(random.nextDouble()*7);
        type = switch (type) {
            case 5 -> 3;
            case 6 -> 4;
            default -> type;
        };
        weapons.add(i + ", " + z + ", " + j + ", " + rarity+", "+type);
    }
    private ArrayList<ArrayList<Integer>> addMore(ArrayList<ArrayList<Integer>> columns, Integer[] seed) {
        if (seed[1] < 0 || seed[1] >= GRIDSIZE/2 || seed[0] < 0 || seed[0] >= GRIDSIZE/2) {
            return columns;
        }
        int height = columns.get(seed[0]).get(seed[1]);
        columns.get(seed[0]).set(seed[1], height+1);
        if (random.nextDouble() < 0.02+((10-height)*0.02)) {
            columns = addMore(columns, new Integer[]{seed[0]+1,seed[1]});
        } if (random.nextDouble() < 0.02+((10-height)*0.02)) {
            columns = addMore(columns, new Integer[]{seed[0]-1,seed[1]});
        } if (random.nextDouble() < 0.02+((10-height)*0.02)) {
            columns = addMore(columns, new Integer[]{seed[0],seed[1]+1});
        } if (random.nextDouble() < 0.02+((10-height)*0.02)) {
            columns = addMore(columns, new Integer[]{seed[0],seed[1]-1});
        } if (random.nextDouble() < 0.02) {
            columns = addMore(columns, new Integer[]{seed[0],seed[1]});
        }
        return columns;
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
        newCoords.add((int) Math.floor(random.nextDouble() * GRIDSIZE - (double) GRIDSIZE / 2));
        newCoords.add((int) Math.floor(random.nextDouble() * GRIDSIZE - (double) GRIDSIZE / 2));
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
