package org.example;

import javax.swing.*;
import java.awt.*;

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

        this.navBarPanelUpdate();
        this.editingAreaPanelUpdate();

        mainContainer.add(navBarPanel, BorderLayout.NORTH);
        mainContainer.add(editingAreaPanel, BorderLayout.CENTER);

        this.mainFrame.setVisible(true);
    }

    private void navBarPanelUpdate(){
        JButton openFileButton = new JButton("Open File");
        JButton saveButton = new JButton("Save File");
        JButton exportButton = new JButton("Export File");
        this.navBarPanel.add(openFileButton);
        this.navBarPanel.add(saveButton);
        this.navBarPanel.add(exportButton);
    }

    private void editingAreaPanelUpdate(){
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
