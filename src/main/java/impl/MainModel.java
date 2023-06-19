package impl;

import interfaces.IMainModel;

import java.io.File;
import java.beans.*;
import java.io.IOException;
import java.nio.file.Files;

public class MainModel implements IMainModel {
    private File currentOpenedFile;
    private String content;
    private final PropertyChangeSupport changeObserver;

    public MainModel(){
        //TODO:
        this.content = "";
        this.currentOpenedFile = null;
        this.changeObserver = new PropertyChangeSupport(this);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeObserver.addPropertyChangeListener(listener);
    }
    @Override
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
    @Override
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
