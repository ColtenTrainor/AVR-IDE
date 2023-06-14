package impl;

import interfaces.IMainView;

import javax.swing.text.BadLocationException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DebugMode implements Runnable{
    private final IMainView view;
    private boolean isActivated;
    private boolean isContentChanged;
    private final PropertyChangeSupport changeObserver;
    public DebugMode(IMainView view){
        this.view = view;
        this.isActivated = true;
        this.isContentChanged = false;
        this.changeObserver = new PropertyChangeSupport(this);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeObserver.addPropertyChangeListener(listener);
    }

    @Override
    public void run() {
        String previousContent = "";
        while (isActivated){
            String currentContent = view.getTextArea().getText();

            if ( ! currentContent.contentEquals(previousContent)){
                setContentState();
                try {
                    System.out.print("DEBUG: " + view.getTextArea().getText());
                    System.out.println("Raw: " +view.getTextArea().getDocument().getText(0, view.getTextArea().getDocument().getLength()));
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
                previousContent = currentContent;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Sleep failed.");
            }
        }
    }
    private void setContentState(){
        boolean oldState = this.isContentChanged;
        this.isContentChanged = true;
        changeObserver.firePropertyChange("state", oldState, true);
        System.out.println("...State changed");
    }

    private void fontColorChanging(String htmlString){
        htmlString = htmlString.replaceAll("ldi", "<font color=\"red\"> ldi </font>");
    }

}
