package org.example;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("--Testing git");
        System.out.println("---" + System.getProperty("os.name"));
        File currentDirectory = new File(".");
        System.out.println("Abs path: " + currentDirectory.getAbsolutePath());

        // Windows
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.runCommand("dir");
//        commandExecutor.runCommand("ipconfig", "/all");

        // Linux
//        commandExecutor.runCommand("pwd");
    }


}