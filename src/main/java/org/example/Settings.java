package org.example;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Settings {

    // Settings fields
    private File defaultSaveDir;
    public File getDefaultSaveDir() { return defaultSaveDir; }
    public void setDefaultSaveDir(File newDir){
        defaultSaveDir = newDir;
        settingsData.put(MapKeys.defaultSaveDir, newDir.getPath());
    }

    // Utility fields
    private static final File settingsFile = new File("settings.cfg");
    private final HashMap<String, String> settingsData = new HashMap<>();

    public Settings(){
        // Initialize settings with defaults
        var savepath = FileSystemView.getFileSystemView()
                .getDefaultDirectory().getAbsolutePath();
        if (Main.OperatingSystem == Main.OS.Linux) savepath += "/Documents"; // may need to add documents in windows too, untested
        savepath += "/AVR-IDE";
        settingsData.put(MapKeys.defaultSaveDir, savepath);

        try {
            if (!settingsFile.createNewFile()){
                deserialize();
            }
            serialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deserialize(){
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

    private void parseSettingsData(){
        for (String setting : settingsData.keySet()){
            switch (setting){
                case MapKeys.defaultSaveDir -> defaultSaveDir = new File(settingsData.get(MapKeys.defaultSaveDir));
            }
        }
    }

    private void serialize(){
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
