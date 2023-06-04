package org.example.actions;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;

public class OpenFileAction extends AbstractAction {
    private final JFrame frame;

    public OpenFileAction(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Assembly File (.asm)", "asm"));
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            var currentFile = fileChooser.getSelectedFile();
            frame.setTitle("File Selected: " + currentFile.getAbsolutePath());
        } else {
            frame.setTitle("*New File");
        }
    }
}
