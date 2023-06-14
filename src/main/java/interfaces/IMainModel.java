package interfaces;

import java.beans.PropertyChangeListener;
import java.io.File;

public interface IMainModel {
    void setCurrentFile(File file);
    void setContent(String text);
    String getCurrentFilePath();
    File getCurrentOpenedFile();
    String getContent();
    String getHtmlContent();
    void addPropertyChangeListener(PropertyChangeListener listener);
}
