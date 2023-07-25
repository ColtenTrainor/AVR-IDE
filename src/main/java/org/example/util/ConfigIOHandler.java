package org.example.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class ConfigIOHandler {
    private final File configFile;
    private final HashMap<String, String> configData;
    public HashMap<String, String> getConfigData(){ return configData; }

    public ConfigIOHandler(File configFile){
        this.configFile = configFile;
        configData = new HashMap<>();
    }

    public ConfigIOHandler(File configFile, HashMap<String, String> configData){
        this.configFile = configFile;
        this.configData = configData;
    }

    public void deserialize(){
        try {
            var scanner = new Scanner(configFile);
            while (scanner.hasNext()){
                var line = scanner.nextLine();
                var splitLine = line.split("\\s+(?=(?:[^'\"]*['\"][^'\"]*['\"])*[^'\"]*$)");
                if (splitLine.length < 2) continue;
                var key = splitLine[0].replace("\"", "").toLowerCase();
                var value = splitLine[1].replace("\"", "");
                if (!value.equals("")){
                    configData.put(key, value);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void serialize(){
        try {
            var writer = new FileWriter(configFile);
            StringBuilder fullFile = new StringBuilder();
            for (String key : configData.keySet()){
                fullFile.append(key).append(" \"").append(configData.get(key)).append("\"\n");
            }
            writer.write(fullFile.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
