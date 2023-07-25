package org.example.mvc.view;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;

public interface IMainView {
    JFrame getMainFrame();
//    JEditorPane getTextArea();
    JTextPane getTextArea();
    JLabel getSideBarLabel();
    JScrollPane getScrollPane();
    JMenuItem getOpenButton();
    JMenuItem getSaveButton();
    JMenuItem getSaveAsButton();
    JMenuItem getNewFileButton();
    JComboBox<SerialPort> getPortSelector();
    JButton getCompileButton();
    JButton getFlashButton();
    void setDefaultFrame();
}
