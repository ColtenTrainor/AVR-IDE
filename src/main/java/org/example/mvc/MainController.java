package org.example.mvc;

import com.fazecast.jSerialComm.SerialPort;
import org.example.Settings;
import org.example.mvc.actions.MenuActions;
import org.example.mvc.codeassist.SuggestionManager;
import org.example.mvc.codeassist.SyntaxHighlighter;
import org.example.mvc.view.MainView;

import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainController implements PropertyChangeListener {
    MainView view;
    MainModel model;
    MenuActions menuActions;
    SyntaxHighlighter syntaxHighlighter;
    SuggestionManager suggestionManager;

    public MainController(MainView view, MainModel model){
        this.view = view;
        this.model = model;

        this.menuActions = new MenuActions(this.view, this.model, this);

        // Set up and initialize stuff
        view.getEditorPane().setDocument(model.getEditorDocument());
        model.setEditorDefaultAttrSet(((StyledEditorKit)view.getEditorPane().getEditorKit()).getInputAttributes());
        refreshSerialPorts();
        setUpListeners();
        setUpLabels();

        this.syntaxHighlighter = new SyntaxHighlighter((StyledDocument) model.getEditorDocument(), view.getEditorPane());
        this.suggestionManager = new SuggestionManager(this.view);

        // Controller listens to model, debug
        this.model.addPropertyChangeListener(this);
    }
    public void runView(){
        this.view.setDefaultFrame();
//        var syntaxThread = new Thread(syntaxHighlighter);
//        syntaxThread.start();
        model.newFileFromTemplate(Settings.newFileTemplate);
    }

    private void setUpLabels(){
        this.view.getNewFileButton().setText("New");
        this.view.getOpenButton().setText("Open");
        this.view.getSaveButton().setText("Save");
        this.view.getSaveAsButton().setText("Save As");
        this.view.getCompileButton().setText("Compile");
        this.view.getFlashButton().setText("Flash");
    }

    private void setUpListeners(){
        this.view.getNewFileButton().setAction(this.menuActions.NEW.apply("New File"));
        this.view.getOpenButton().setAction(this.menuActions.OPEN.apply("Open File"));
        this.view.getSaveButton().setAction(this.menuActions.SAVE.apply("Save File"));
        this.view.getSaveAsButton().setAction(this.menuActions.SAVEAS.apply("Save File As"));

        this.view.getPortSelector().addPopupMenuListener(
                this.menuActions.OPENPORTSELECTOR.apply("Open Port Selector"));
        this.view.getCompileButton().setAction(this.menuActions.COMPILE.apply("Compile"));
        this.view.getFlashButton().setAction(this.menuActions.FLASH.apply("Upload"));

        this.view.getCodeEditor().setDocumentListener(
                this.menuActions.EDITORDOCUMENTLISTENER.apply("Editor Document Listener"));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("is_saved"))
            view.getMainFrame().setTitle(Settings.programName + " - " +
                    model.getCurrentFilePath() + (model.getIsSaved() ? "" : "*"));
    }

    public SerialPort getSelectedPort(){
        return (SerialPort) view.getPortSelector().getSelectedItem();
    }

    public void refreshSerialPorts(){
        var portSelector = view.getPortSelector();
        SerialPort selectedItem = (SerialPort) portSelector.getSelectedItem();
        portSelector.removeAllItems();
        for (var port : SerialPort.getCommPorts()) {
            portSelector.addItem(port);
            if (selectedItem != null && selectedItem.getSystemPortName().equals(port.getSystemPortName()))
                portSelector.setSelectedItem(port);
        }
    }

    public SyntaxHighlighter getSyntaxHighlighter() { return syntaxHighlighter; }

    public SuggestionManager getSuggestionManager() {return suggestionManager; }
}
