package impl.actions;

import impl.PopUpWindow;
import interfaces.IMainModel;
import interfaces.IMainView;
import org.example.Main;

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
        this.COMPILE = null;
        this.UPLOAD = null;
    }

    private Action openFile(String command){
        return new AbstractAction(command) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(AsmFilter);
                fileChooser.setCurrentDirectory(Main.Settings.getDefaultSaveDir());
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
                        System.out.println("--Saved as new.");
                        saveFileWithDialogue();
                    } else {
                        System.out.println("--Saved as existing file.");
                        saveFile(model.getCurrentOpenedFile(), StandardOpenOption.TRUNCATE_EXISTING);
                    }
                }catch (IOException ex){
                    throw new RuntimeException("---Save Failed.");
                }
            }
        };
    }

    private Action saveAs(String command){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveFileWithDialogue();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

    private void saveFileWithDialogue() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(AsmFilter);

    // removing this allows choosing a file to overwrite, which users may want
//        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        fileChooser.setSelectedFile(new File(Main.Settings.getDefaultSaveDir(), "New_File.asm"));
        int option = fileChooser.showSaveDialog(view.getMainFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            String saveDirectory = selectedFile.getParent();
            String fileName = selectedFile.getName();

            if (fileChooser.getFileFilter().equals(AsmFilter) && !fileName.contains(".asm")) {
                fileName += ".asm";
            }
            File newFile = new File(saveDirectory, fileName);

            saveFile(newFile, StandardOpenOption.CREATE_NEW);
        }
    }

    private void saveFile(File file, java.nio.file.StandardOpenOption option) throws IOException {
        model.setContent(view.getTextArea().getText());
        model.setCurrentFile(file);
        String parsedText = parseHtml(model.getContent());
        Files.write(file.toPath(), parsedText.getBytes(), option);
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
