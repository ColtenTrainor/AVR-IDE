package org.example;

import org.example.util.ConfigIOHandler;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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
        System.out.printf("Detected OS: %s\n", OperatingSystem);
    }

    // Settings fields
    private static File defaultSaveDir;
    public static File getDefaultSaveDir() { return defaultSaveDir; }
    public static void setDefaultSaveDir(File newDir){
        defaultSaveDir = newDir;
        settingsData.put(MapKeys.defaultSaveDir, newDir.getPath());
    }

    // Final static fields (here for easy reference, not to be serialized)
    public static final File newFileTemplate = new File("newFileTemplate.asm"); // could make this changeable later
    public static final String programName = "AVR Development Environment";
    public static final Font iconsFont;
    static {
        Font icons;
        try {
            icons = Font.createFont(Font.TRUETYPE_FONT, new File(
                    "icons" +
                            (OperatingSystem == OS.Windows ? "\\" : "/") +
                            "MaterialSymbolsRounded.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(icons);
        } catch (IOException | FontFormatException e) {
            icons = new JLabel().getFont();
        }
        iconsFont = icons;
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
