package org.example;

import org.example.mvc.MainController;
import org.example.mvc.MainModel;
import org.example.mvc.view.MainView;

public class Main {

    public static void main(String[] args) {

        MainView view = new MainView();
        MainModel model = new MainModel();

        MainController controller = new MainController(view, model);
        controller.runView();
    }
}

