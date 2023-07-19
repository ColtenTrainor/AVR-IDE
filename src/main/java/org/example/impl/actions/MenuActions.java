package org.example.impl.actions;

import org.example.impl.PopUpWindow;
import org.example.interfaces.IMainModel;
import org.example.interfaces.IMainView;
import org.example.CommandExecutor;
import org.example.Settings;

import javax.swing.*;
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
    public final Function<String, Action> OPEN, NEW, SAVE, SAVEAS, EXPORT, COMPILE, UPLOAD;

    private final FileNameExtensionFilter AsmFilter = new FileNameExtensionFilter(
            "Assembly File (.asm)", "asm");

    public MenuActions(IMainView view, IMainModel model){
        this.view = view;
        this.model = model;

        // this.OPEN = command -> openFile(command);
        this.OPEN = this::openFile;
        this.NEW = this::newFile;
        this.SAVE = this::save;
        this.SAVEAS = this::saveAs;
        //TODO:
        this.EXPORT = null;
        this.COMPILE = this::compile;
        this.UPLOAD = this::upload;
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
                    try {
                        String text = Files.readString(currentFile.toPath());
                        model.setCurrentFile(currentFile);
                        model.setContent(text);
                        model.setIsSaved(true);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
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
                    saveFile(model.getCurrentOpenedFile());
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

            return saveFile(newFile);
        } else {
            return false;
        }
    }

    private boolean saveFile(File file) {
        model.setContent(view.getTextArea().getText());
        model.setCurrentFile(file);
        String parsedText = parseHtml(model.getContent());
        try {
            Files.write(file.toPath(), parsedText.getBytes(), StandardOpenOption.CREATE_NEW);
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
            final String message = "Current File is not saved, abandon changes?";
            final PopUpWindow popUp = new PopUpWindow("Warning:", message,"Yes", "No" );
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentOpenedFile() != null){
                    newFileWarningWindow();
                }
                else{
                    model.setCurrentFile(null);
                    model.setContent("");
                    model.setIsSaved(false);
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
            if (saveFile(currentFile)) CommandExecutor.Avra.compile(currentFile);
        } else {
            if (saveFileWithDialogue()){
                currentFile = model.getCurrentOpenedFile();
                CommandExecutor.Avra.compile(currentFile);
            }
        }
    }

    private Action upload(String s){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var currentFile = model.getCurrentOpenedFile();
                compileFile(currentFile);
                CommandExecutor.AvrDude.flash(currentFile);
            }
        };
    }
}
