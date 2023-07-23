package org.example.impl;

import com.fazecast.jSerialComm.SerialPort;
import com.formdev.flatlaf.FlatDarkLaf;
import org.example.components.JConsole;
import org.example.components.JCustomMenuBar;
import org.example.interfaces.IMainView;
import org.example.components.JSideBar;
import org.example.components.JCodeEditor;

import javax.swing.*;
import java.awt.*;

public class MainView implements IMainView {
    private final JFrame mainFrame;
    private final int screenSolutionWidth;
    private final int screenSolutionHeight;
    private final JCustomMenuBar menuBar;
    private final JCodeEditor codeEditor;
    private final JSideBar sideBar;
    private final JConsole console;

    public MainView(String text){
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf() {});
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        mainFrame = new JFrame("Example: " + text);

        menuBar = new JCustomMenuBar();
        codeEditor = new JCodeEditor();
        sideBar = new JSideBar();
        console = new JConsole();


        screenSolutionWidth = (int)(getScreenSolution().getWidth() * 0.8);
        screenSolutionHeight = (int)(getScreenSolution().getHeight() * 0.8);
    }

    public void setWindowSize(int width, int height){
        this.mainFrame.setSize(width, height);
    }

    public void setDefaultFrame(){
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(this.screenSolutionWidth, this.screenSolutionHeight);
        mainFrame.setLocationRelativeTo(null);
        Container mainContainer = mainFrame.getContentPane();

        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                codeEditor, console);
        verticalSplitPane.setDividerLocation((int) (screenSolutionHeight * .7));

        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                sideBar, verticalSplitPane);
        horizontalSplitPane.setDividerLocation(200);

        mainContainer.add(menuBar, BorderLayout.NORTH);
        mainContainer.add(horizontalSplitPane, BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }

    private Dimension getScreenSolution(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    @Override
    public JFrame getMainFrame() {
        return mainFrame;
    }

    @Override
    public JTextPane getTextArea(){
        return codeEditor.TextPane;
    }

    @Override
    public JMenuItem getOpenButton() {
        return this.menuBar.OpenFileButton;
    }

    @Override
    public JMenuItem getSaveButton() {
        return this.menuBar.SaveButton;
    }

    @Override
    public JMenuItem getSaveAsButton() {
        return menuBar.SaveAsButton;
    }

    @Override
    public JMenuItem getNewFileButton() {
        return menuBar.NewFileButton;
    }

    @Override public JComboBox<SerialPort> getPortSelector(){
        return menuBar.PortSelector;
    }

    @Override
    public JButton getCompileButton() {
        return menuBar.CompileButton;
    }

    @Override
    public JButton getFlashButton() {
        return menuBar.FlashButton;
    }

}
