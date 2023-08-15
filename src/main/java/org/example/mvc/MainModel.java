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
    private boolean isSaved = true;


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
            if (currentOpenedFile != null)
                this.content = Files.readString(currentOpenedFile.toPath());
            else this.content = "";
        }catch (IOException | NullPointerException ex){
            ex.printStackTrace();
        }
        changeObserver.firePropertyChange("file", oldFile, currentOpenedFile);
    }

    public void newFileFromTemplate(File template){
        File oldFile = currentOpenedFile;
        this.currentOpenedFile = null;
        try {
            if (template.exists())
                this.setContent(Files.readString(template.toPath()));
            else this.setContent("Failed to read template file");
        }catch (IOException | NullPointerException ex){
            ex.printStackTrace();
        }
        changeObserver.firePropertyChange("file", oldFile, currentOpenedFile);
        this.setIsSaved(false);
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
