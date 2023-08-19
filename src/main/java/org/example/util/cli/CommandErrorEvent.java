package org.example.util.cli;

import java.util.EventObject;

public class CommandErrorEvent extends EventObject {

    private final String message;

    public CommandErrorEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() { return message; }
}
