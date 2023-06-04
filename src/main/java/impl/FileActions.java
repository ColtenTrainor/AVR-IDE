package impl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileActions{
    private final JButton openBtn;
    private final JButton saveBtn;
    private final JButton exportBtn;
    private final JFrame frame;
    private File currentFile;

    public FileActions(JButton openBtn, JButton saveBtn, JButton exportBtn, JFrame frame){
        this.openBtn = openBtn;
        this.saveBtn = saveBtn;
        this.exportBtn = exportBtn;
        this.frame = frame;

        this.setActions();
    }

    public void setActions(){
        Action openAction = new openFileAction();
        openBtn.setAction(openAction);
        openBtn.setText("Open File");

        // TODO: Save & export file
        // saveBtn action
        saveBtn.setText("Save File");
        // exportBtn action
        exportBtn.setText("Export File");
    }


    private class openFileAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                try {
                    String text = new String(Files.readAllBytes(currentFile.toPath()), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            frame.setTitle(getCurrentFilePath());
        }
    }

    private String getCurrentFilePath(){
        if (currentFile != null)
            return currentFile.getAbsolutePath();
        else return "*New File";
    }

}
