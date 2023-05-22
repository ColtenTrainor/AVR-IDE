package org.example;

import java.io.File;
import java.io.IOException;



public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("--Testing git");
        System.out.println("---" + System.getProperty("os.name"));
        File curr = new File(".");
        System.out.println("Abs path: " + curr.getAbsolutePath());

        // Windows
        CommandExecutor exe = new CommandExecutor();
        exe.runCommand("dir");
//        exe.runCommand("ipconfig", "/all");

        // Linux
//        exe.runCommand("pwd");
    }


}