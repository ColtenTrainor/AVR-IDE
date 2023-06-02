package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class FileActions{
    private JMenuItem newBtn;
    private JMenuItem openBtn;
    private JMenuItem saveBtn;
    private JMenuItem saveAsBtn;
    private JFrame frame;
    private File currentFile;

    public FileActions(JMenuItem newBtn, JMenuItem openBtn, JMenuItem saveBtn, JMenuItem saveAsBtn, JFrame frame){
        this.newBtn = newBtn;
        this.openBtn = openBtn;
        this.saveBtn = saveBtn;
        this.saveAsBtn = saveAsBtn;
        this.frame = frame;

        this.setActions();
    }

    public void setActions(){
        // TODO: New, save, & saveAs file
        // newBtn action
        newBtn.setText("New");
        // openBtn action
        Action openAction = new openFileAction();
        openBtn.setAction(openAction);
        openBtn.setText("Open");
        // saveBtn action
        saveBtn.setText("Save");
        // exportBtn action
        saveAsBtn.setText("Save As");
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
