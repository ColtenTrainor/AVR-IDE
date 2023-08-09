package org.example.mvc.actions;

import org.example.util.CommandExecutor;
import org.example.Settings;
import org.example.mvc.MainController;
import org.example.mvc.view.PopUpWindow;
import org.example.mvc.IMainModel;
import org.example.mvc.view.IMainView;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuActions {
    private final IMainView view;
    private final IMainModel model;
    private final MainController controller;
    public final Function<String, Action> OPEN, NEW, SAVE, SAVEAS, COMPILE, FLASH;
    public final Function<String, PopupMenuListener> OPENPORTSELECTOR;

    private final FileNameExtensionFilter AsmFilter = new FileNameExtensionFilter(
            "Assembly File (.asm)", "asm");

    public MenuActions(IMainView view, IMainModel model, MainController controller){
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
                if (model.getCurrentOpenedFile() == null) {
                    saveFileWithDialogue();
                } else {
                    saveFile(model.getCurrentOpenedFile(), StandardOpenOption.CREATE_NEW);
                }
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

    private boolean saveFileWithDialogue() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(AsmFilter);

        fileChooser.setSelectedFile(new File(Settings.getDefaultSaveDir(), "New_File.asm"));
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

            return saveFile(newFile, StandardOpenOption.CREATE_NEW);
        } else {
            return false;
        }
    }

    private boolean saveFile(File file, StandardOpenOption openOption) {
        model.setContent(view.getTextArea().getText());
        model.setCurrentFile(file);
        String parsedText = parseHtml(model.getContent());
        try {
            Files.write(file.toPath(), parsedText.getBytes(), openOption);
        } catch (IOException e) {
            return false;
        }
        model.setIsSaved(true);
        return true;
    }

    // This might be useful as a public method somewhere else
    private String parseHtml(String html){
        Matcher matcher = Pattern.compile("<p.*?>\\s*(.*?)\\s*</p>").matcher(html);
        StringBuilder parsedText = new StringBuilder();

        while (matcher.find()) {
            var newToken = matcher.group(1);
            parsedText.append(newToken).append("\n");
        }
        return parsedText.toString();
    }

    private Action newFile(String command){
        return new AbstractAction(command){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentOpenedFile() != null){
                    newFileWarningWindow();
                }
                else{
                    model.setCurrentFile(null);
                    model.setContent("");
                }
            }
        };
    }
    private void newFileWarningWindow(){
        String message = "Current file might not be saved, abandon changes?";
        PopUpWindow popUp = new PopUpWindow("Warning:", message,"Yes", "No" );
        popUp.getOkButton().setAction(new AbstractAction("Yes") {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setCurrentFile(null);
                model.setContent("");
                model.setIsSaved(false);
                popUp.setInvisible();
            }
        });
        popUp.getCancelButton().setAction(new AbstractAction("No") {
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
            if (saveFile(currentFile, StandardOpenOption.CREATE_NEW)) CommandExecutor.Avra.compile(currentFile);
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
