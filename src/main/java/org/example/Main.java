package org.example;

import org.example.impl.MainController;
import org.example.impl.MainModel;
import org.example.impl.MainView;
import org.example.interfaces.IMainModel;
import org.example.interfaces.IMainView;

public class Main {

    public static void main(String[] args) {

        IMainView view = new MainView("AVR Development Environment");
        IMainModel model = new MainModel();

        MainController controller = new MainController(view, model);
        controller.runView();
    }
}

