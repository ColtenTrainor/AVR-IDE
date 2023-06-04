package impl;
import interfaces.IView;

import javax.swing.*;
import java.awt.*;

public class View implements IView {
    private final JFrame mainFrame;
    private final int screenSolutionWidth;
    private final int screenSolutionHeight;
    private final NavBar navBar;
    private final EditingArea editingArea;

    public View(String text){
        this.mainFrame = new JFrame("MVC example: " + text);

        this.navBar = new NavBar();
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

        mainContainer.add(navBar.getNavBarPanel(), BorderLayout.NORTH);
        mainContainer.add(centerContainer, BorderLayout.CENTER);

        this.mainFrame.setVisible(true);
    }


    private static class NavBar{
        private final JPanel navBarPanel = new JPanel();
        private final JButton newFileButton = new JButton();
        private final JButton openFileButton = new JButton();
        private final JButton saveButton = new JButton();
        private final JButton exportButton = new JButton();

        public NavBar(){
            this.navBarPanel.setLayout(new GridLayout());
            this.navBarPanel.add(newFileButton);
            this.navBarPanel.add(openFileButton);
            this.navBarPanel.add(saveButton);
            this.navBarPanel.add(exportButton);
        }
        public JPanel getNavBarPanel() {
            return navBarPanel;
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
            this.layoutDefault();
        }
        private void layoutDefault(){
            this.columnPanel.add(sideBar);
            this.editableAreaPanel.add((editableField));
        }

        public JLabel getSideBar() {
            return sideBar;
        }
        public JTextPane getEditableField() {
            return editableField;
        }
        public JPanel getColumnPanel() {
            return columnPanel;
        }
        public JPanel getEditableAreaPanel() {
            return editableAreaPanel;
        }
    }

    private void setFileActions(JButton openFileButton, JButton saveButton, JButton exportButton){
        /*
        *   This is place to add actions/listeners to the GUI
        */
        FileActions actions = new FileActions(openFileButton, saveButton, exportButton, mainFrame);
        actions.setActions();
    }


    private Dimension getScreenSolution(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    @Override
    public JFrame getMainFrame() {
        return this.mainFrame;
    }

    @Override
    public JTextPane getTextArea() {
        return this.editingArea.getEditableField();
    }

    @Override
    public JButton getOpenButton() {
        return this.navBar.openFileButton;
    }

    @Override
    public JButton getSaveButton() {
        return this.navBar.saveButton;
    }

    @Override
    public JButton getExportButton() {
        return navBar.exportButton;
    }

    @Override
    public JButton getNewFileButton() {
        return navBar.newFileButton;
    }

}
