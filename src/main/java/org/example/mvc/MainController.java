package org.example.mvc;

import com.fazecast.jSerialComm.SerialPort;
import org.example.mvc.actions.MenuActions;
import org.example.mvc.codeassist.SuggestionManager;
import org.example.mvc.codeassist.SyntaxHighlighter;
import org.example.mvc.view.IMainView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainController implements PropertyChangeListener {
    IMainView view;
    IMainModel model;
    MenuActions fileActions;
    SyntaxHighlighter syntaxHighlighter;
    SuggestionManager suggestionManager;

    public MainController(IMainView view, IMainModel model){
        this.view = view;
        this.model = model;
        this.syntaxHighlighter = new SyntaxHighlighter(this.view, this.model);
        this.suggestionManager = new SuggestionManager(this.view);
        this.fileActions = new MenuActions(this.view, this.model, this);

        // Set up and initialize stuff
        refreshSerialPorts();

        setUpMenuListeners();
        setUpLabels();

        // Controller listens to model, debug
        this.model.addPropertyChangeListener(this);
//        this.debugMode.addPropertyChangeListener(this);
    }
    public void runView(){
        this.view.setDefaultFrame();
        this.syntaxHighlighter.run();
    }

    private void setUpLabels(){
        this.view.getNewFileButton().setText("New");
        this.view.getOpenButton().setText("Open");
        this.view.getSaveButton().setText("Save");
        this.view.getSaveAsButton().setText("Save As");
        this.view.getCompileButton().setText("Compile");
        this.view.getFlashButton().setText("Flash");
    }

    private void setUpMenuListeners(){
        this.view.getNewFileButton().setAction(this.fileActions.NEW.apply("New File"));
        this.view.getOpenButton().setAction(this.fileActions.OPEN.apply("Open File"));
        this.view.getSaveButton().setAction(this.fileActions.SAVE.apply("Save File"));
        this.view.getSaveAsButton().setAction(this.fileActions.SAVEAS.apply("Save File As"));
        this.view.getCompileButton().setAction(this.fileActions.COMPILE.apply("Compile"));
        this.view.getFlashButton().setAction(this.fileActions.FLASH.apply("Upload"));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if (propertyName.equalsIgnoreCase("file")) {
            view.getMainFrame().setTitle(model.getCurrentFilePath());
            view.getTextArea().setText(model.getContent());
            model.setIsSaved(false);
        }
        else if (propertyName.equalsIgnoreCase("content")){
            syntaxHighlighter.addColorHighlighting();
            suggestionManager.activateSuggestionPopUp();
        }
    }

    public SerialPort getSelectedPort(){
        return (SerialPort) view.getPortSelector().getSelectedItem();
    }

    public void refreshSerialPorts(){
        var portSelector = view.getPortSelector();
        var selectedItem = portSelector.getSelectedItem();
        portSelector.removeAllItems();
        for (var port : SerialPort.getCommPorts()) {
            portSelector.addItem(port);
            if (selectedItem != null && selectedItem.equals(port))
                portSelector.setSelectedItem(port);
        }
    }
}
