package org.example.interfaces;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;

public interface IMainView {
    JFrame getMainFrame();
//    JEditorPane getTextArea();
    JTextPane getTextArea();
    JMenuItem getOpenButton();
    JMenuItem getSaveButton();
    JMenuItem getSaveAsButton();
    JMenuItem getExportButton();
    JMenuItem getNewFileButton();
    JComboBox<SerialPort> getPortSelector();
    JButton getCompileButton();
    JButton getFlashButton();
    void setDefaultFrame();
}
