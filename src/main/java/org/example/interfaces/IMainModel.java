package org.example.interfaces;

import java.beans.PropertyChangeListener;
import java.io.File;

public interface IMainModel {
    boolean getIsSaved();
    void setIsSaved(boolean value);
    void setCurrentFile(File file);
    void setContent(String text);
    String getCurrentFilePath();
    File getCurrentOpenedFile();
    String getContent();
    void addPropertyChangeListener(PropertyChangeListener listener);
}
