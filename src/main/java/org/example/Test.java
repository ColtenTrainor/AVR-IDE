package org.example;


import impl.Controller;
import impl.MainView;
import impl.Model;
import interfaces.IModel;
import interfaces.IMainView;

import java.io.File;

public class Test {
    public static void main(String[] args) {

    }

    public void viewTesting(){
        IMainView view = new MainView("Testing.");
        IModel model = new Model();

        Controller controller = new Controller(view, model);
        controller.runView();

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
