package org.example;

import java.io.File;
import java.util.HashMap;

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
    private static final File settingsFile = new File("cfg/settings.cfg");
    private static final HashMap<String, String> settingsData = new HashMap<>();

    static {
        // Initialize settings with defaults
        setDefaultSaveDir(new File("saved"));

        // Handle config file
        new File("cfg").mkdir();
        var configHandler = new ConfigIOHandler(settingsFile, settingsData);

        parseSettingsData();
        if (settingsFile.exists()) configHandler.deserialize();
        parseSettingsData();
        configHandler.serialize();

        // Misc other setup
        defaultSaveDir.mkdirs();
    }

    private Settings(){} // Constructor never called, made private to disallow instantiating an instance

    private static void parseSettingsData(){
        for (String setting : settingsData.keySet()){
            switch (setting){
                case MapKeys.defaultSaveDir -> defaultSaveDir = new File(settingsData.get(MapKeys.defaultSaveDir));
            }
        }
    }

    // Static variables for map keys to avoid string typo errors
    private static class MapKeys{
        public static final String defaultSaveDir = "default_save_dir";
    }
}
