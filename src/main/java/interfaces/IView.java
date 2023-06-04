package interfaces;

import javax.swing.*;

public interface IView {
    JFrame getMainFrame();
    JTextPane getTextArea();
    JButton getOpenButton();
    JButton getSaveButton();
    JButton getExportButton();
    JButton getNewFileButton();
    void setDefaultFrame();
}
