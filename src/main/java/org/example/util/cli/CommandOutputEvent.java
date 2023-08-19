package org.example.util.cli;

import java.util.EventObject;

public class CommandOutputEvent extends EventObject {

    private final String message;

    public CommandOutputEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() { return message; }
}
