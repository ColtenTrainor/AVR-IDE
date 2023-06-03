package org.example;

import java.io.File;

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
    public static void main(String[] args) {


        System.out.println("---" + OperatingSystem);
        File currentDirectory = new File("");
        System.out.println("Abs path: " + currentDirectory.getAbsolutePath());

        // Windows
        CommandExecutor commandExecutor = new CommandExecutor();
//        commandExecutor.runCommand(new File(currentDirectory + "avra"), "avra.exe", "test.asm");
//        commandExecutor.runCommand("dir");
//        commandExecutor.runCommand("ipconfig", "/all");

        // Linux
//        commandExecutor.runCommand("pwd");
    }
}
