package org.example;

import impl.PopUpWindow;
import interfaces.IModel;
import interfaces.IView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Controller implements PropertyChangeListener {
    IView view;
    IModel model;

    public Controller(IView view, IModel model){
        this.view = view;
        this.model = model;

        setUpNavBarListeners();
        setUpLabels();
        this.model.addPropertyChangeListener(this);
    }
    public void runView(){
        this.view.setDefaultFrame();
    }

    private void setUpLabels(){
        //TODO:
        this.view.getNewFileButton().setText("New File");
        this.view.getOpenButton().setText("Open File");
        this.view.getSaveButton().setText("Save File");
        this.view.getExportButton().setText("Export File");
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

    }


    private void setUpNavBarListeners(){
        // New Button
        this.view.getNewFileButton().setAction(new AbstractAction() {
            String message = "Current File is not saved, abandon changes?";
            PopUpWindow popUp = new PopUpWindow("WARNING:", message,"Yes", "No" );

            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentOpenedFile() != null){
                    popUp.setVisible();
                    popUp.getOkButton().setAction(new AbstractAction("Yes") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            model.setCurrentFile(null);
                            model.setContent("");
                            popUp.setInvisible();
                        }
                    });
                    popUp.getCancelButton().setAction(new AbstractAction("No") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            popUp.setInvisible();
                        }
                    });
                }
                else{
                    model.setCurrentFile(null);
                    model.setContent("");
                }
            }
        });

        // Open button
        this.view.getOpenButton().setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(view.getMainFrame());
                if(option == JFileChooser.APPROVE_OPTION) {
                    File currentFile = fileChooser.getSelectedFile();
                    try {
                        String text = Files.readString(currentFile.toPath());
                        model.setCurrentFile(currentFile);
                        model.setContent(text);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        // Save Button
        this.view.getSaveButton().setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    if (model.getCurrentOpenedFile() == null) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int option = fileChooser.showOpenDialog(view.getMainFrame());
                        if (option == JFileChooser.APPROVE_OPTION) {
                            File dirToSave = fileChooser.getSelectedFile();

                            // TODO: Add new file's name changeability
                            Path newFile = new File(dirToSave.toString() + "\\New_File.txt").toPath();

                            model.setContent(view.getTextArea().getText());
                            model.setCurrentFile(newFile.toFile());
                            Files.write(newFile, model.getContent().getBytes(), StandardOpenOption.CREATE_NEW);
                        }
                    } else {
                        model.setContent(view.getTextArea().getText());
                        Files.write(model.getCurrentOpenedFile().toPath(), model.getContent().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
                    }
                }catch (IOException ex){
                    throw new RuntimeException("Save Failed.");
                }
            }
        });

        // TODO: Export Button
        this.view.getExportButton().setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
    }
}
