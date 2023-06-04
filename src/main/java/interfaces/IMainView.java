package interfaces;

import javax.swing.*;

public interface IMainView {
    JFrame getMainFrame();
    JTextPane getTextArea();
    JMenuItem getOpenButton();
    JMenuItem getSaveButton();
    JMenuItem getSaveAsButton();
    JMenuItem getExportButton();
    JMenuItem getNewFileButton();
    JButton getCompileButton();
    JButton getUploadButton();
    void setDefaultFrame();
}
