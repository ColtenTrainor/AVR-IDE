package impl;

import com.formdev.flatlaf.FlatDarkLaf;
import interfaces.IMainView;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.StringTokenizer;

public class MainView implements IMainView {
    private final JFrame mainFrame;
    private final int screenSolutionWidth;
    private final int screenSolutionHeight;
    private final MenuBar menuBar;
    private final EditingArea editingArea;

    public MainView(String text){
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf() {
            });
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        this.mainFrame = new JFrame("Example: " + text);

        this.menuBar = new MenuBar();
        this.editingArea = new EditingArea();

        this.screenSolutionWidth = (int)(getScreenSolution().getWidth() * 0.5);
        this.screenSolutionHeight = (int)(getScreenSolution().getHeight() * 0.5);
    }

    public void setWindowSize(int width, int height){
        this.mainFrame.setSize(width, height);
    }

    public void setDefaultFrame(){
        this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.mainFrame.setSize(this.screenSolutionWidth, this.screenSolutionHeight);
        this.mainFrame.setLocationRelativeTo(null);
        Container mainContainer = mainFrame.getContentPane();

        JSplitPane centerContainer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editingArea.columnPanel, editingArea.editableScrollPane);
        centerContainer.setDividerLocation(100);

        mainContainer.add(menuBar.getMenuBarContainer(), BorderLayout.NORTH);
        mainContainer.add(centerContainer, BorderLayout.CENTER);

        this.mainFrame.setVisible(true);
    }

    private static class MenuBar {
        private final JMenuBar menuBarContainer = new JMenuBar();
        private final JMenuItem newFileButton = new JMenuItem();
        private final JMenuItem openFileButton = new JMenuItem();
        private final JMenuItem saveButton = new JMenuItem();
        private final JMenuItem exportButton = new JMenuItem();
        private final JMenuItem saveAsButton = new JMenuItem();
        private final JButton compileButton = new JButton();
        private final JButton uploadButton = new JButton();

        public MenuBar(){
            this.menuBarContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
            JMenu fileMenu = new JMenu("File");
            fileMenu.add(newFileButton);
            fileMenu.add(openFileButton);
            fileMenu.add(saveButton);
            fileMenu.add(saveAsButton);
            this.menuBarContainer.add(fileMenu);
            this.menuBarContainer.add(compileButton);
            this.menuBarContainer.add(uploadButton);

        }
        public JMenuBar getMenuBarContainer() {
            return menuBarContainer;
        }
    }

    private static class EditingArea{
        private final JPanel columnPanel = new JPanel();
        private final JScrollPane editableScrollPane;
        private final JLabel sideBar = new JLabel("Side Bar");
        private final JTextPane editableField = new JTextPane();
        public EditingArea() {
            this.columnPanel.setLayout(new GridLayout());
            this.editableField.setContentType("text/html");
            this.editableField.setEditable(true);
            this.editableScrollPane = new JScrollPane(editableField);
            this.setLayoutDefault();
        }
        private void setLayoutDefault(){
            this.columnPanel.add(sideBar);
            editableField.setToolTipText("Type codes here.");
        }
    }

    private Dimension getScreenSolution(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    @Override
    public JFrame getMainFrame() {
        return this.mainFrame;
    }

    @Override
    public JTextPane getTextArea(){
        return this.editingArea.editableField;
    }
    @Override
    public JLabel getSideBarLabel(){
        return editingArea.sideBar;
    }

    @Override
    public JScrollPane getScrollPane() {
        return editingArea.editableScrollPane;
    }

    @Override
    public JMenuItem getOpenButton() {
        return this.menuBar.openFileButton;
    }

    @Override
    public JMenuItem getSaveButton() {
        return this.menuBar.saveButton;
    }

    @Override
    public JMenuItem getSaveAsButton() {
        return menuBar.saveAsButton;
    }
    @Override
    public JMenuItem getExportButton() {
        return menuBar.exportButton;
    }

    @Override
    public JMenuItem getNewFileButton() {
        return menuBar.newFileButton;
    }

    @Override
    public JButton getCompileButton() {
        return menuBar.compileButton;
    }

    @Override
    public JButton getUploadButton() {
        return menuBar.uploadButton;
    }

}
