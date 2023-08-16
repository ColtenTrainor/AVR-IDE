package org.example.mvc;

import javax.swing.text.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainModel{
    private File currentOpenedFile;
    private Document editorDocument;
    private MutableAttributeSet editorDefaultAttrSet;
    private final PropertyChangeSupport changeObserver;
    private boolean isSaved = true;


    public MainModel(){
        this.editorDocument = new DefaultStyledDocument();
        this.currentOpenedFile = null;
        this.changeObserver = new PropertyChangeSupport(this);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeObserver.addPropertyChangeListener(listener);
    }

    public void newContentFromString(String text){
        try {
            editorDocument.remove(0, editorDocument.getLength());
            editorDocument.insertString(0, text, editorDefaultAttrSet);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCurrentFile(File file){
        var oldFile = currentOpenedFile;
        currentOpenedFile = file;
        try {
            newContentFromString(Files.readString(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        changeObserver.firePropertyChange("file", oldFile, file);
    }

    public void newFileFromTemplate(File template){
        var oldFile = currentOpenedFile;
        currentOpenedFile = null;
        try {
            newContentFromString(Files.readString(template.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        changeObserver.firePropertyChange("file", oldFile, null);
        setIsSaved(false);
    }

    public String getCurrentFilePath() {
        return getFilePathOrEmpty(this.currentOpenedFile);
    }

    public Document getEditorDocument() {
        return this.editorDocument;
    }
    public MutableAttributeSet getEditorDefaultAttrSet(){
        return this.editorDefaultAttrSet;
    }
    public void setEditorDefaultAttrSet(MutableAttributeSet value){
        editorDefaultAttrSet = value;
    }

    public File getCurrentOpenedFile() {
        return currentOpenedFile;
    }

    public String getFilePathOrEmpty(File file){
        if (file != null)
            return file.getAbsolutePath();
        else return "New File";
    }
    public boolean getIsSaved() { return isSaved; }
    public void setIsSaved(boolean value) {
        var oldValue = isSaved;
        isSaved = value;
        changeObserver.firePropertyChange("is_saved", oldValue, value);
    }
}
