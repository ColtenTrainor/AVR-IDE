package org.example.util;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class Utils {
    private Utils(){} // Constructor never called, made private to disallow instantiating an instance

    public static String getFullTextFromDoc(Document doc) throws BadLocationException {
        return doc.getText(0, doc.getLength());
    }
}
