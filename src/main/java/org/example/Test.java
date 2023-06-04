package org.example;


import impl.Controller;
import impl.Model;
import impl.View;
import interfaces.IModel;
import interfaces.IView;

import java.io.File;

public class Test {
    public static void main(String[] args) {

    }

    public void viewTesting(){
        final String text = "XvX";
        IView view = new View("Testing.");
        IModel model = new Model();

        Controller controller = new Controller(view, model);
        controller.runView();

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
