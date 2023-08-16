package org.example.mvc.actions;

import org.example.Settings;
import org.example.mvc.MainController;
import org.example.mvc.MainModel;
import org.example.mvc.view.MainView;
import org.example.mvc.view.PopUpWindow;
import org.example.util.CommandExecutor;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.function.Function;

public class MenuActions {
    private final MainView view;
    private final MainModel model;
    private final MainController controller;
    public final Function<String, Action> OPEN, NEW, SAVE, SAVEAS, COMPILE, FLASH;
    public final Function<String, PopupMenuListener> OPENPORTSELECTOR;

    private final FileNameExtensionFilter AsmFilter = new FileNameExtensionFilter(
            "Assembly File (.asm)", "asm");

    public MenuActions(MainView view, MainModel model, MainController controller){
        this.view = view;
        this.model = model;
        this.controller = controller;

        this.OPEN = this::openFile;
        this.NEW = this::newFile;
        this.SAVE = this::save;
        this.SAVEAS = this::saveAs;
        this.COMPILE = this::compile;
        this.FLASH = this::flash;

        this.OPENPORTSELECTOR = this::openPortSelector;
    }

    private Action openFile(String command){
        return new AbstractAction(command) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(AsmFilter);
                fileChooser.setCurrentDirectory(Settings.getDefaultSaveDir());
                int option = fileChooser.showOpenDialog(view.getMainFrame());
                if(option == JFileChooser.APPROVE_OPTION) {
                    File currentFile = fileChooser.getSelectedFile();
                    model.setCurrentFile(currentFile);
                    model.setIsSaved(true);
                }
            }
        };
    }

    private Action save(String command){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        };
    }

    private Action saveAs(String command){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileWithDialogue();
            }
        };
    }

    private boolean saveFile(){
        if (model.getCurrentOpenedFile() == null) {
            return saveFileWithDialogue();
        } else {
            return saveFileNoDialogue(model.getCurrentOpenedFile(), StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    private boolean saveFileWithDialogue() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(AsmFilter);

        fileChooser.setSelectedFile(new File(Settings.getDefaultSaveDir(), "new_file.asm"));
        fileChooser.setCurrentDirectory(Settings.getDefaultSaveDir());
        int option = fileChooser.showSaveDialog(view.getMainFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            String saveDirectory = selectedFile.getParent();
            String fileName = selectedFile.getName();

            if (fileChooser.getFileFilter().equals(AsmFilter) && !fileName.contains(".asm")) {
                fileName += ".asm";
            }
            File newFile = new File(saveDirectory, fileName);

            return saveFileNoDialogue(newFile, StandardOpenOption.CREATE_NEW);
        } else {
            return false;
        }
    }

    private boolean saveFileNoDialogue(File file, StandardOpenOption openOption) {
        try {
            String text = view.getTextArea().getDocument().getText(0, view.getTextArea().getDocument().getLength());

            model.setContent(text);
            Files.write(file.toPath(), text.getBytes(), openOption);
            model.setCurrentFile(file);

        } catch (IOException | BadLocationException e) {
            return false;
        }
        model.setIsSaved(true);
        return true;
    }

    private Action newFile(String command){
        return new AbstractAction(command){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getIsSaved()){
                    model.newFileFromTemplate(Settings.newFileTemplate);
                }
                else{
                    newFileWarningWindow();
                }
            }
        };
    }
    private void newFileWarningWindow(){
        String message = "Current file is not saved, save before closing?";
        PopUpWindow popUp = new PopUpWindow("Warning:", message,
                "Save Then Close", "Close Without Saving", "Cancel");

        var saveFirstButton = popUp.getButton(0);
        var noSaveButton = popUp.getButton(1);
        var cancelButton = popUp.getButton(2);

        saveFirstButton.setAction(new AbstractAction(saveFirstButton.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (saveFile())
                    model.newFileFromTemplate(Settings.newFileTemplate);
                popUp.setInvisible();
            }
        });
        noSaveButton.setAction(new AbstractAction(noSaveButton.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.newFileFromTemplate(Settings.newFileTemplate);
                popUp.setInvisible();
            }
        });
        cancelButton.setAction(new AbstractAction(cancelButton.getText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                popUp.setInvisible();
            }
        });
        popUp.setVisible();
    }

    private Action compile(String s) {
        return new AbstractAction(s) {
            @Override
            public void actionPerformed(ActionEvent e) {
                compileFile(model.getCurrentOpenedFile());
            }
        };
    }

    private void compileFile(File currentFile){
        if (model.getIsSaved()){
            CommandExecutor.Avra.compile(currentFile);
        } else if (currentFile != null && currentFile.exists()) {
            if (saveFileNoDialogue(currentFile, StandardOpenOption.CREATE_NEW)) CommandExecutor.Avra.compile(currentFile);
        } else {
            if (saveFileWithDialogue()){
                currentFile = model.getCurrentOpenedFile();
                CommandExecutor.Avra.compile(currentFile);
            }
        }
    }

    private Action flash(String s){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var currentFile = model.getCurrentOpenedFile();
                compileFile(currentFile);
                System.out.println(controller.getSelectedPort());
                CommandExecutor.AvrDude.flash(currentFile, controller.getSelectedPort());
            }
        };
    }

    private PopupMenuListener openPortSelector(String s){
        return new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                controller.refreshSerialPorts();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        };
    }
}
