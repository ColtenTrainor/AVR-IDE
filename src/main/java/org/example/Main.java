package org.example;

public class Main {
    public enum OS { Windows, Linux, Mac }
    public static final OS OperatingSystem;
    static {
        var os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) OperatingSystem = OS.Windows;
        else if (os.contains("linux")) OperatingSystem = OS.Linux;
        else if (os.contains("mac") || os.contains("osx")) OperatingSystem = OS.Mac;
        else throw new RuntimeException("Failed to identify operating system");
    }

    public static Settings Settings = new Settings();

    public static void main(String[] args) {
        Test testingModule = new Test();

//        testingModule.commandTesting();
        testingModule.viewTesting();

        var commandExecutor = new CommandExecutor();
    }
}
