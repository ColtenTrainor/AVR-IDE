package org.example.mvc;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainModel{
    private File currentOpenedFile;
    private String content;
    private final PropertyChangeSupport changeObserver;
    private boolean isSaved;
    public boolean getIsSaved() { return isSaved; }
    public void setIsSaved(boolean value) { isSaved = value; }

    public MainModel(){
        this.content = "";
        this.currentOpenedFile = null;
        this.changeObserver = new PropertyChangeSupport(this);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeObserver.addPropertyChangeListener(listener);
    }

    public void setCurrentFile(File file){
        File oldFile = currentOpenedFile;
        this.currentOpenedFile = file;

        try {
            String text = "<p>" + Files.readString(currentOpenedFile.toPath()).replaceAll("\n", "</p><p>") + "</p>";
            this.content = text;
        }catch (IOException ex){
            ex.printStackTrace();
        }
        changeObserver.firePropertyChange("file", oldFile, currentOpenedFile);
    }

    public void setContent(String content){
        String oldContent = this.content;
        this.content = content;
        changeObserver.firePropertyChange("content", oldContent, this.content);
    }


    public String getCurrentFilePath() {
        return getFilePathOrEmpty(this.currentOpenedFile);
    }

    public String getContent() {
        return this.content;
    }

    public String getHtmlContent(){
        return this.content;
    }

    public File getCurrentOpenedFile() {
        return currentOpenedFile;
    }

    public String getFilePathOrEmpty(File file){
        if (file != null)
            return file.getAbsolutePath();
        else return "*No file selected.";
    }
}
