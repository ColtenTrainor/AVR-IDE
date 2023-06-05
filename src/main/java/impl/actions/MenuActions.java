package impl.actions;

import impl.PopUpWindow;
import interfaces.IMainModel;
import interfaces.IMainView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Function;

public class MenuActions {
    private final IMainView view;
    private final IMainModel model;
    public final Function<String, Action> OPEN, NEW, SAVE, SAVEAS, EXPORT, COMPILE, UPLOAD;
    public MenuActions(IMainView view, IMainModel model){
        this.view = view;
        this.model = model;

        // this.OPEN = command -> openFile(command);
        this.OPEN = this::openFile;
        this.NEW = this::newFile;
        this.SAVE = this::save;
        //TODO:
        this.SAVEAS = null;
        this.EXPORT = null;
        this.COMPILE = null;
        this.UPLOAD = null;
    }

    public Action openFileASM() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Assembly File (.asm)", "asm"));
                int option = fileChooser.showOpenDialog(view.getMainFrame());
                if (option == JFileChooser.APPROVE_OPTION) {
                    var currentFile = fileChooser.getSelectedFile();
                    view.getMainFrame().setTitle("File Selected: " + currentFile.getAbsolutePath());
                } else {
                    view.getMainFrame().setTitle("*New File");
                }
            }
        };
    }
    private Action openFile(String command){
        return new AbstractAction(command) {
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
        };
    }

    private Action save(String command){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        };
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
}
