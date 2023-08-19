package org.example.util.cli.tools;

import com.fazecast.jSerialComm.SerialPort;
import org.example.util.cli.CommandActionListener;
import org.example.util.cli.CommandErrorEvent;
import org.example.util.cli.CommandExecutor;

import java.io.File;
import java.util.ArrayList;

public class AvrDude extends CliTool{
    private final ArrayList<CommandActionListener> actionListeners;

    public AvrDude(CommandExecutor commandExecutor, File toolDir,
                   ArrayList<CommandActionListener> actionListeners,
                   String windowsExecutable, String linuxExecutable, String macExecutable) {
        super(commandExecutor, toolDir, windowsExecutable, linuxExecutable, macExecutable);

        this.actionListeners = actionListeners;
    }

    public void flash(File asmFile, SerialPort port){
        if (port == null) {
            var nullPortError = new CommandErrorEvent(this,
                    "Cannot flash: no port selected");
            for (var listener : actionListeners) {
                listener.errorReceived(nullPortError);
            }
            return;
        }

        commandExecutor.runCommand(ToolDir, getExecutable(),
                "-p", "m328p",
                "-c", "arduino",
                "-P", port.getSystemPortName(),
                "-U", "flash:w:" + changeExtension(asmFile.getAbsolutePath(),
                        ".asm", ".hex") + ":i");
    }
}
