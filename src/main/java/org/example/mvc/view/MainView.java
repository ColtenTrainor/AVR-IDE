package org.example.mvc.view;

import com.fazecast.jSerialComm.SerialPort;
import com.formdev.flatlaf.FlatDarkLaf;
import org.example.mvc.view.components.JCodeEditor;
import org.example.mvc.view.components.JConsole;
import org.example.mvc.view.components.JSideBar;

import javax.swing.*;
import java.awt.*;

public class MainView {
    private final JFrame mainFrame;
    private final int screenSolutionWidth;
    private final int screenSolutionHeight;
    private final MainMenuBar menuBar;
    private final JCodeEditor codeEditor;
    private final JSideBar sideBar;
    private final JConsole console;

    public MainView(){
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf() {});
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        mainFrame = new JFrame();
        menuBar = new MainMenuBar();
        codeEditor = new JCodeEditor();
        sideBar = new JSideBar();
        console = new JConsole();
        console.setInputEnabled(true);

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

    private class MainMenuBar extends JPanel{
        public final JMenuItem NewFileButton = new JMenuItem();
        public final JMenuItem OpenFileButton = new JMenuItem();
        public final JMenuItem SaveButton = new JMenuItem();
        public final JMenuItem SaveAsButton = new JMenuItem();
        public final JComboBox<SerialPort> PortSelector = new JComboBox<>();
        public final JButton CompileButton = new JButton();
        public final JButton FlashButton = new JButton();

        public MainMenuBar(){
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            JMenu fileMenu = new JMenu("File");
            fileMenu.add(NewFileButton);
            fileMenu.add(OpenFileButton);
            fileMenu.add(SaveButton);
            fileMenu.add(SaveAsButton);
            var leftSide = new JMenuBar();
            leftSide.add(fileMenu);
            var rightSide = new JMenuBar();
            rightSide.setLayout(new FlowLayout(FlowLayout.RIGHT));
            rightSide.add(PortSelector);
            rightSide.add(CompileButton);
            rightSide.add(FlashButton);
            add(leftSide);
            add(rightSide);
            setLayout(new GridLayout());
        }
    }


    private Dimension getScreenSolution(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public JTextPane getEditorPane(){
        return codeEditor.TextPane;
    }
    public JCodeEditor getCodeEditor(){
        return codeEditor;
    }

    public JMenuItem getOpenButton() {
        return this.menuBar.OpenFileButton;
    }

    public JMenuItem getSaveButton() {
        return this.menuBar.SaveButton;
    }


    public JMenuItem getSaveAsButton() {
        return menuBar.SaveAsButton;
    }

    public JMenuItem getNewFileButton() {
        return menuBar.NewFileButton;
    }

    public JComboBox<SerialPort> getPortSelector(){
        return menuBar.PortSelector;
    }

    public JButton getCompileButton() {
        return menuBar.CompileButton;
    }

    public JButton getFlashButton() {
        return menuBar.FlashButton;
    }

    public JConsole getConsole(){ return console; }

}
