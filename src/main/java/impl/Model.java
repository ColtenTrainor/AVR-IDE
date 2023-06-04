package impl;

import interfaces.IModel;

import java.io.File;
import java.beans.*;

public class Model implements IModel {
    private File currentOpenedFile;
    private String content;
    private final PropertyChangeSupport support;

    public Model(){
        //TODO:
        this.content = "";
        this.currentOpenedFile = null;
        this.support = new PropertyChangeSupport(this);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    @Override
    public void setCurrentFile(File file){
        File oldFile = currentOpenedFile;
        this.currentOpenedFile = file;
        support.firePropertyChange("file", oldFile, currentOpenedFile);
    }

    public void setContent(String content){
        String oldContent = this.content;
        this.content = content;
        support.firePropertyChange("content", oldContent, this.content);
        //TODO:
    }

    @Override
    public String getCurrentFilePath() {
        return getFilePathOrEmpty(this.currentOpenedFile);
    }
    @Override
    public String getContent() {
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
