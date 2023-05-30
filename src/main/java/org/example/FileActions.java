package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class FileActions{
    private JButton openBtn;
    private JButton saveBtn;
    private JButton exportBtn;
    private JFrame frame;
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
            if(option == JFileChooser.APPROVE_OPTION){
                currentFile = fileChooser.getSelectedFile();
                frame.setTitle("File Selected: " + currentFile.getAbsolutePath());
            }else{
                frame.setTitle("*New File");
            }
        }
    }

}
