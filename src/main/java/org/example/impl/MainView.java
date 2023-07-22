package org.example.impl;

import com.fazecast.jSerialComm.SerialPort;
import com.formdev.flatlaf.FlatDarkLaf;
import org.example.interfaces.IMainView;

import javax.swing.*;
import java.awt.*;

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

        this.screenSolutionWidth = (int)(getScreenSolution().getWidth() * 0.8);
        this.screenSolutionHeight = (int)(getScreenSolution().getHeight() * 0.8);
    }

    public void setWindowSize(int width, int height){
        this.mainFrame.setSize(width, height);
    }

    public void setDefaultFrame(){
        this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.mainFrame.setSize(this.screenSolutionWidth, this.screenSolutionHeight);
        this.mainFrame.setLocationRelativeTo(null);
        Container mainContainer = mainFrame.getContentPane();

        JSplitPane centerContainer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editingArea.columnPanel, editingArea.editableField);
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
        private final JComboBox<SerialPort> portSelector = new JComboBox<>();
        private final JButton compileButton = new JButton();
        private final JButton flashButton = new JButton();

        public MenuBar(){
            this.menuBarContainer.setLayout(new BoxLayout(menuBarContainer, BoxLayout.X_AXIS));
            JMenu fileMenu = new JMenu("File");
            fileMenu.add(newFileButton);
            fileMenu.add(openFileButton);
            fileMenu.add(saveButton);
            fileMenu.add(saveAsButton);
            this.menuBarContainer.add(fileMenu);
            this.menuBarContainer.add(Box.createHorizontalGlue());
            this.menuBarContainer.add(portSelector);
            this.menuBarContainer.add(compileButton);
            this.menuBarContainer.add(flashButton);
        }
        public JMenuBar getMenuBarContainer() {
            return menuBarContainer;
        }
    }

    private static class EditingArea{
        private final JPanel columnPanel = new JPanel();
        private final JPanel editableAreaPanel = new JPanel();
        private final JLabel sideBar = new JLabel("Side Bar");
        private final JTextPane editableField = new JTextPane();
        public EditingArea() {
            this.columnPanel.setLayout(new GridLayout());
            this.editableAreaPanel.setLayout(new GridLayout());
            this.editableField.setContentType("text/html");

            this.setLayoutDefault();
        }
        private void setLayoutDefault(){
            this.columnPanel.add(sideBar);
            this.editableAreaPanel.add((editableField));
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

    @Override public JComboBox<SerialPort> getPortSelector(){
        return menuBar.portSelector;
    }

    @Override
    public JButton getCompileButton() {
        return menuBar.compileButton;
    }

    @Override
    public JButton getFlashButton() {
        return menuBar.flashButton;
    }

}
