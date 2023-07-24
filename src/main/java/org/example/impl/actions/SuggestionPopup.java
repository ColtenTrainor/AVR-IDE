package org.example.impl.actions;

import org.example.impl.regAndIns.InstructionRules;
import org.example.interfaces.IMainView;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class SuggestionPopup implements Runnable{
    private final JPopupMenu popupMenu;
    private final IMainView view;
    private final InstructionRules rules;
    public SuggestionPopup(IMainView view){
        this.popupMenu = new JPopupMenu();
        this.popupMenu.setMaximumSize(new Dimension(100, 200));
        this.view = view;
        this.rules = new InstructionRules();
        this.view.getMainFrame().add(popupMenu);
    }

    public void addItemToMenu(String text){
        this.popupMenu.add(new JMenuItem(text));
    }

    public void addListOfItemsToMenu(List<String> list){
        for (String item : list)
            this.popupMenu.add(new JMenuItem(rules.getInstructionDescription(item)));
    }
    public void clearPopMenu(){
        this.popupMenu.removeAll();
        this.showPopupMenu(0 , 0);
    }
    public void showPopupMenu(int x, int y){
        this.setPopupMenuFocus(false);
        this.popupMenu.show(this.view.getTextArea(), x + 20, y + 10);
    }
    public void setPopupMenuFocus(boolean isFocused){
        this.popupMenu.setFocusable(false);
    }

    public void addTextPrediction(){
        JTextPane textPane = view.getTextArea();
        StyledDocument doc = textPane.getStyledDocument();

        try{
            int lastEscapeN = doc.getText(0, doc.getLength()).lastIndexOf('\n');
//            System.out.println("Last \\n: " + lastEscapeN);
//            System.out.println("Leng: " + doc.getLength());
            String lastLine = lastEscapeN == -1 ? doc.getText(0, doc.getLength()) : doc.getText(lastEscapeN + 1, doc.getLength()-lastEscapeN-1);
//            System.out.println("Last line: " + lastLine);
            //TODO: add validation of existent instruction
//            String[] words = lastLine.split("\\s+");
            int lastWordOffset = lastLine.lastIndexOf(' ') == -1 ? 0 : lastLine.lastIndexOf(' ')+1;
            String lastWord = lastLine.substring(lastWordOffset);

            this.clearPopMenu();
            this.addListOfItemsToMenu(rules.findMatchedInstructions(lastWord));
            this.showPopupMenu(getCaretPositionInView()[0], getCaretPositionInView()[1]);

        }catch (BadLocationException | IndexOutOfBoundsException ex){
            System.out.println("Text prediction failed.");
            ex.printStackTrace();
        }
    }

    private int[] getCaretPositionInView(){
        Rectangle2D xy = null;
        JTextPane pane = view.getTextArea();
        try{
            xy =  pane.modelToView2D(pane.getDocument().getLength());
//            System.out.println(xy.getX() + " & " + xy.getY());
        } catch (BadLocationException | NullPointerException ex){
            System.out.println("Caret Position -> x,y : failed ||" + pane.getDocument().getLength());
        }
        return new int[]{(int)xy.getX(), (int)xy.getY()};
    }

    @Override
    public void run() {
        addTextPrediction();
    }
}
