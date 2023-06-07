package org.example;

public class Main {
    public static Settings Settings = new Settings();

    public static void main(String[] args) {
        Test testingModule = new Test();

//        testingModule.commandTesting();
        testingModule.viewTesting();

        var commandExecutor = new CommandExecutor();
    }
}

