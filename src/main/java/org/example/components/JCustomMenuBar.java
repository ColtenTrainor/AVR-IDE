package org.example.components;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;

public class JCustomMenuBar extends JPanel{
    public final JMenuItem NewFileButton = new JMenuItem();
    public final JMenuItem OpenFileButton = new JMenuItem();
    public final JMenuItem SaveButton = new JMenuItem();
    public final JMenuItem SaveAsButton = new JMenuItem();
    public final JComboBox<SerialPort> PortSelector = new JComboBox<>();
    public final JButton CompileButton = new JButton();
    public final JButton FlashButton = new JButton();

    public JCustomMenuBar(){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(NewFileButton);
        fileMenu.add(OpenFileButton);
        fileMenu.add(SaveButton);
        fileMenu.add(SaveAsButton);
        var leftSide = new JMenuBar();
        leftSide.add(fileMenu);
        var rightSide = new JMenuBar();
        rightSide.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightSide.add(PortSelector);
        rightSide.add(CompileButton);
        rightSide.add(FlashButton);
        add(leftSide);
        add(rightSide);
        setLayout(new GridLayout());
    }
}
