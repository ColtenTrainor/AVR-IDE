package org.example;


import org.example.impl.MainController;
import org.example.impl.MainModel;
import org.example.impl.MainView;
import org.example.interfaces.IMainModel;
import org.example.interfaces.IMainView;

public class Test {
    public static void main(String[] args) {

    }

    public void viewTesting(){
        IMainView view = new MainView("Testing.");
        IMainModel model = new MainModel();

        MainController mainController = new MainController(view, model);
        mainController.runView();
    }
}
