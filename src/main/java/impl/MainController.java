package impl;

import impl.actions.MenuActions;
import interfaces.IMainModel;
import interfaces.IMainView;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class MainController implements PropertyChangeListener {
    IMainView view;
    IMainModel model;
    MenuActions fileActions;
    DebugMode debugMode;

    public MainController(IMainView view, IMainModel model){
        this.view = view;
        this.model = model;
        this.debugMode = new DebugMode(this.view);
        this.fileActions = new MenuActions(this.view, this.model);

        // Set up and initialize stuff
        setUpMenuListeners();
        setUpLabels();

        // Controller listens to model
        this.model.addPropertyChangeListener(this);
        this.debugMode.addPropertyChangeListener(this);
    }
    public void runView(){
        this.view.setDefaultFrame();
        this.debugMode.run();
    }

    private void setUpLabels(){
        //TODO:
        this.view.getNewFileButton().setText("New");
        this.view.getOpenButton().setText("Open");
        this.view.getSaveButton().setText("Save");
        this.view.getSaveAsButton().setText("Save As");
        this.view.getExportButton().setText("Export");
        this.view.getCompileButton().setText("Compile");
        this.view.getUploadButton().setText("Upload");
    }

    private void setUpMenuListeners(){
        this.view.getNewFileButton().setAction(this.fileActions.NEW.apply("New File"));
        this.view.getOpenButton().setAction(this.fileActions.OPEN.apply("Open File"));
        this.view.getSaveButton().setAction(this.fileActions.SAVE.apply("Save File"));
        this.view.getSaveAsButton().setAction(this.fileActions.SAVEAS.apply("Save File As"));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if (propertyName.equalsIgnoreCase("file")) {
            view.getMainFrame().setTitle(model.getCurrentFilePath());
        }
        else if (propertyName.equalsIgnoreCase("content")){
            view.getTextArea().setText(model.getContent());
        }
        else if (propertyName.equalsIgnoreCase("state")){
           view.getTextArea().setText(model.getContent());
        }
    }// propertyChange()

}
