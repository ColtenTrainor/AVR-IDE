package org.example.mvc.view.components;

import org.example.Settings;
import org.example.mvc.codeassist.InstructionData;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class JDocumentationPanel extends JPanel{

    private JPanel toolBar = new JPanel();
    private JButton collapseButton = new JButton();
    private ArrayList<JInstructionPane> instructionBoxes = new ArrayList<>();

    public JDocumentationPanel(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(toolBar);
        toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        toolBar.add(collapseButton);
        collapseButton.setText("\ue5cb");
        collapseButton.setMargin(new Insets(-2, -2, -2, -2));
        collapseButton.setFont(Settings.iconsFont.deriveFont(Font.PLAIN, 20));

        var docsPanel = new JPanel();
        docsPanel.setLayout(new BoxLayout(docsPanel, BoxLayout.Y_AXIS));

        var scrollPane = new JScrollPane(docsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        add(scrollPane);

        for (var inst : InstructionData.getInstructionSet()) {
            var instData = InstructionData.getInstructionData(inst);
            if (instData.getIsEssential()) {
                var instPane = new JInstructionPane(instData);
                instructionBoxes.add(instPane);
                docsPanel.add(instPane);
            }
        }
    }

    class JInstructionPane extends JPanel{
        private JTextPane documentationTextPane = new JTextPane();
        private JButton expandButton = new JButton();
        private JTextPane expandedText = new JTextPane();

        public JInstructionPane(InstructionData instruction){
            setBorder(expandButton.getBorder());
            setLayout(new BorderLayout());

            expandedText.setVisible(false);
            documentationTextPane.setEditable(false);
            expandedText.setEditable(false);
            expandButton.setFont(Settings.iconsFont.deriveFont(Font.PLAIN, 20));
            expandButton.setText("\ue5cc");
            expandButton.setBackground(documentationTextPane.getBackground());
            expandButton.setBorder(documentationTextPane.getBorder());
            expandButton.setMargin(new Insets(-2, -2, -2, -2));
            expandButton.addActionListener(e ->
            {
                expandedText.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, \n" +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                        "Ut enim ad minim veniam, quis nostrud exercitation \n" +
                        "ullamco laboris nisi ut aliquip ex ea commodo consequat.\n");
                expandedText.setVisible(!expandedText.isVisible());
                expandButton.setText(expandedText.isVisible() ? "\ue5cf" : "\ue5cc");
            });

            JPanel topSide = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topSide.add(expandButton);
            topSide.add(documentationTextPane);

            add(expandedText, BorderLayout.CENTER);
            add(topSide, BorderLayout.NORTH);

            var shortDocStyle = documentationTextPane.addStyle("short-doc-style", null);
            try {
                documentationTextPane.getDocument().insertString(0, InstructionData.getShortInfo(instruction.getInstruction()), shortDocStyle);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
