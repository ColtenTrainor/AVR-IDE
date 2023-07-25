package org.example;

import org.example.mvc.MainController;
import org.example.mvc.MainModel;
import org.example.mvc.view.MainView;
import org.example.mvc.IMainModel;
import org.example.mvc.view.IMainView;

public class Main {

    public static void main(String[] args) {

        IMainView view = new MainView("AVR Development Environment");
        IMainModel model = new MainModel();

        MainController controller = new MainController(view, model);
        controller.runView();
    }
}

