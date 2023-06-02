package org.example;

import java.io.File;

public class Test {
    public static void main(String[] args) {

    }

    public void viewTesting(){
        final String text = "XvX";
        EditorView ex = new EditorView("Testing.");
        ex.setWindowDefault();
    }



    public void commandTesting(){
        System.out.println("---" + System.getProperty("os.name"));
        File currentDirectory = new File("");
        System.out.println("Abs path: " + currentDirectory.getAbsolutePath());

        // Windows
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.runCommand(new File(currentDirectory + "gavrasm"), "gavrasm.exe", "instr.asm");
//        commandExecutor.runCommand("dir");
//        commandExecutor.runCommand("ipconfig", "/all");

        // Linux
//        commandExecutor.runCommand("pwd");
    }

}
