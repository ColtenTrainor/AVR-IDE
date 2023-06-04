package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.actions.OpenFileAction;

import javax.swing.*;
import java.awt.*;

public class EditorView {
    private JFrame mainFrame;
    private JMenuBar menuBar;
    private JSplitPane editingAreaPanel;

    public EditorView(String text){
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf() {
            });
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        this.mainFrame = new JFrame("MVC example: " + text);
        this.menuBar = new JMenuBar();
    }

    public void setWindowSize(int width, int height){
        this.mainFrame.setSize(width, height);
    }

    public void setWindowDefault(){
        this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.mainFrame.setSize(1280, 720);
        this.mainFrame.setLocationRelativeTo(null);

        Container mainContainer = mainFrame.getContentPane();

        this.menuBarInit();
        this.editingAreaPanelInit();

        mainContainer.add(menuBar, BorderLayout.NORTH);
        mainContainer.add(editingAreaPanel, BorderLayout.CENTER);

        this.mainFrame.setVisible(true);
    }

    private void menuBarInit(){
        // Menu bar layout
        var menuBarLayout = new FlowLayout();
        menuBarLayout.setAlignment(FlowLayout.LEFT);
        this.menuBar.setLayout(menuBarLayout);

        // File tab
        var fileMenu = new JMenu("File");
        var fileNew = new JMenuItem("New");
        var fileOpen = new JMenuItem("Open");
        var fileSave = new JMenuItem("Save");
        var fileSaveAs = new JMenuItem("Save As");

        this.menuBar.add(fileMenu);
        fileMenu.add(fileNew);
        fileMenu.add(fileOpen);
        fileMenu.add(fileSave);
        fileMenu.add(fileSaveAs);

        fileOpen.setAction(new OpenFileAction(this.mainFrame));

        fileOpen.setText("Open");


        // Compile & Upload buttons
        var separator = new JSeparator();
        this.menuBar.add(separator);

        var compileButton = new JButton("Compile");
        var uploadButton = new JButton("Upload");

        this.menuBar.add(compileButton);
        this.menuBar.add(uploadButton);
    }

    private void editingAreaPanelInit(){
        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new GridLayout());
        JPanel editableAreaPanel = new JPanel();
        editableAreaPanel.setLayout(new GridLayout());

        JLabel sideBar = new JLabel("Side Bar");

        JTextArea editField = new JTextArea("Enter text here.");
        editField.setLineWrap(true);

        columnPanel.add(sideBar);
        editableAreaPanel.add(editField);

        this.editingAreaPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, columnPanel, editableAreaPanel);
        this.editingAreaPanel.setDividerLocation(100);
    }
}