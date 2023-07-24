package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public final class Settings {

    public enum OS { Windows, Linux, Mac }
    public static final OS OperatingSystem;
    static {
        var os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) OperatingSystem = OS.Windows;
        else if (os.contains("linux")) OperatingSystem = OS.Linux;
        else if (os.contains("mac") || os.contains("osx")) OperatingSystem = OS.Mac;
        else throw new RuntimeException("Failed to identify operating system");
    }

    // Settings fields
    private static File defaultSaveDir;
    public static File getDefaultSaveDir() { return defaultSaveDir; }
    public static void setDefaultSaveDir(File newDir){
        defaultSaveDir = newDir;
        settingsData.put(MapKeys.defaultSaveDir, newDir.getPath());
    }

    // Utility fields
    private static final File settingsFile = new File("settings.cfg");
    private static final HashMap<String, String> settingsData = new HashMap<>();

    static {
        // Initialize settings with defaults
        setDefaultSaveDir(new File("saved"));

        // Handle config file
        parseSettingsData();
        if (settingsFile.exists()) deserialize();
        serialize();

        // Misc other setup
        defaultSaveDir.mkdirs();
    }

    private Settings(){} // Constructor never called, made private to disallow instantiating an instance

    private static void deserialize(){
        try {
            var scanner = new Scanner(settingsFile);
            while (scanner.hasNext()){
                var line = scanner.nextLine();
                var splitLine = line.split("\\s+(?=(?:[^'\"]*['\"][^'\"]*['\"])*[^'\"]*$)");
                if (splitLine.length < 2) continue;
                var key = splitLine[0].replace("\"", "").toLowerCase();
                var value = splitLine[1].replace("\"", "");
                if (settingsData.containsKey(key) && !value.equals("")){
                    settingsData.put(key, value);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        parseSettingsData();
    }

    private static void parseSettingsData(){
        for (String setting : settingsData.keySet()){
            switch (setting){
                case MapKeys.defaultSaveDir -> defaultSaveDir = new File(settingsData.get(MapKeys.defaultSaveDir));
            }
        }
    }

    private static void serialize(){
        try {
            var writer = new FileWriter(settingsFile);
            StringBuilder fullSettingsString = new StringBuilder();
            for (String setting : settingsData.keySet()){
                fullSettingsString.append(setting).append(" \"").append(settingsData.get(setting)).append("\"\n");
            }
            writer.write(fullSettingsString.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Static variables for map keys to avoid string typo errors
    private static class MapKeys{
        public static final String defaultSaveDir = "default_save_dir";
    }
}
