package org.example;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public enum OS { Windows, Linux, Mac }
    public static final OS OperatingSystem;
    static {
        var os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) OperatingSystem = OS.Windows;
        else if (os.contains("linux")) OperatingSystem = OS.Linux;
        else if (os.contains("mac") || os.contains("osx")) OperatingSystem = OS.Mac;
        else throw new RuntimeException("Failed to identify operating system");
    };
    public static File SaveDataDir;
    static {
        var cfg = new File("settings.cfg");
        Gson gson = new Gson();
        try {
            if (cfg.createNewFile()) {
                var writer = new FileWriter(cfg);
                var savepath = FileSystemView.getFileSystemView()
                        .getDefaultDirectory().getAbsolutePath();
                if (OperatingSystem == OS.Linux) savepath += "/Documents";
                savepath += "/AVR-IDE";
                SaveDataDir = new File(savepath);
                writer.write(gson.toJson(SaveDataDir.getAbsolutePath()));
                writer.close();
            } else {
                var scanner = new Scanner(cfg);
                SaveDataDir = new File(gson.fromJson(scanner.nextLine(), String.class));
                scanner.close();
            }
        } catch (IOException e) {
            System.out.println("Failed to read or write settings.cfg");
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        System.out.println(SaveDataDir);
        System.out.println(SaveDataDir.mkdir());

        var commandExecutor = new CommandExecutor();
    }
}
