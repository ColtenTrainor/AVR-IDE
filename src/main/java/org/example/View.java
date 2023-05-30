package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class View {
    private JFrame mainFrame;
    private JPanel navBarPanel;
    private JSplitPane editingAreaPanel;

    public View(String text){
        this.mainFrame = new JFrame("MVC example: " + text);
        this.navBarPanel = new JPanel();

        this.navBarPanel.setLayout(new GridLayout());
    }

    public void setWindowSize(int width, int height){
        this.mainFrame.setSize(width, height);
    }

    public void setWindowDefault(){
        this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.mainFrame.setSize(1290, 700);
        this.mainFrame.setLocationRelativeTo(null);

        Container mainContainer = mainFrame.getContentPane();

        this.navBarPanelInit();
        this.editingAreaPanelInit();

        mainContainer.add(navBarPanel, BorderLayout.NORTH);
        mainContainer.add(editingAreaPanel, BorderLayout.CENTER);

        this.mainFrame.setVisible(true);
    }

    private void navBarPanelInit(){
        JButton openFileButton = new JButton();
        JButton saveButton = new JButton();
        JButton exportButton = new JButton();

        this.navBarPanel.add(openFileButton);
        this.navBarPanel.add(saveButton);
        this.navBarPanel.add(exportButton);

        this.setFileActions(openFileButton, saveButton, exportButton);
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

    private void setFileActions(JButton openFileButton, JButton saveButton, JButton exportButton){
        FileActions actions = new FileActions(openFileButton, saveButton, exportButton, mainFrame);
        actions.setActions();
    }

}
