package org.example.mvc;

import org.example.util.ConfigIOHandler;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class SyntaxHighlighter {
    private final HashMap<String, Color> colorMap = new HashMap<>();

    public SyntaxHighlighter(){
        ConfigIOHandler configHandler = new ConfigIOHandler(
                new File("cfg/highlightColors.cfg"));

        configHandler.deserialize();
        decodeColorData(configHandler.getConfigData());
    }

    private void decodeColorData(HashMap<String, String> data){
        colorMap.clear();
        for (var tokenType : data.keySet()) {
            try {
                colorMap.put(tokenType, Color.decode(data.get(tokenType)));
            } catch (NumberFormatException ignored) {}
        }
    }

    public Color getColor(String tokenType){ return colorMap.get(tokenType); }
}
