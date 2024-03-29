package org.example.util.cli;

import java.util.EventListener;

public interface CommandActionListener extends EventListener {

    void errorReceived(CommandErrorEvent e);
    void outputReceived(CommandOutputEvent e);
}
