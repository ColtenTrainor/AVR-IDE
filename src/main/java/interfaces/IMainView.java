package interfaces;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public interface IMainView {
    JFrame getMainFrame();
//    JEditorPane getTextArea();
    JTextPane getTextArea();
    JLabel getSideBarLabel();
    JScrollPane getScrollPane();
    JMenuItem getOpenButton();
    JMenuItem getSaveButton();
    JMenuItem getSaveAsButton();
    JMenuItem getExportButton();
    JMenuItem getNewFileButton();
    JButton getCompileButton();
    JButton getUploadButton();
    void setDefaultFrame();
}
