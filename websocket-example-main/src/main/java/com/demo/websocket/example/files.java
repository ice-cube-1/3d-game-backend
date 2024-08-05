package com.demo.websocket.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class files {
    public static ArrayList<String> read(String filename) {
        File file = new File(filename);
        ArrayList<String> output = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                output.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
    public static void write(String filename, List<String> lines) {
        File file = new File(filename);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
