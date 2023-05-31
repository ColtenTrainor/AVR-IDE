package org.example;

import java.io.File;

public class Main {

    public static final String OperatingSystem = System.getProperty("os.name");

    public static void main(String[] args) {
        System.out.println("---" + OperatingSystem);
        File currentDirectory = new File("");
        System.out.println("Abs path: " + currentDirectory.getAbsolutePath());

        // Windows
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.runCommand(new File(currentDirectory + "avra"), "avra.exe", "test.asm");
//        commandExecutor.runCommand("dir");
//        commandExecutor.runCommand("ipconfig", "/all");

        // Linux
//        commandExecutor.runCommand("pwd");
    }
}
