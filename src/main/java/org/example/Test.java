package org.example;


import org.example.impl.MainController;
import org.example.impl.MainView;
import org.example.impl.MainModel;
import org.example.impl.regAndIns.InstructionRules;
import org.example.interfaces.IMainModel;
import org.example.interfaces.IMainView;

import java.io.File;

public class Test {
    public static void main(String[] args) {

    }

    public void viewTesting(){
        IMainView view = new MainView("Testing.");
        IMainModel model = new MainModel();

        MainController mainController = new MainController(view, model);
        mainController.runView();
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

    public void syntaxTesting(){
        InstructionRules rules = new InstructionRules();
        rules.printRuleMap();
    }

}
