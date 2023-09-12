package org.example.util.cli.tools;

import org.example.Settings;
import org.example.util.cli.CommandExecutor;

import java.io.File;

public abstract class CliTool {
    protected final CommandExecutor commandExecutor;
    protected final File ToolDir;

    private final String windowsExecutable;
    private final String linuxExecutable;
    private final String macExecutable;

    protected CliTool(CommandExecutor commandExecutor, File toolDir,
                   String windowsExecutable, String linuxExecutable, String macExecutable){
        this.commandExecutor = commandExecutor;
        this.ToolDir = toolDir;

        this.windowsExecutable = windowsExecutable;
        this.linuxExecutable = linuxExecutable;
        this.macExecutable = macExecutable;
    }

    protected String getExecutable(){
        return switch (Settings.OperatingSystem){
            case Windows -> windowsExecutable;
            case Linux -> "./" + linuxExecutable;
            case Mac -> "./" + macExecutable;
        };
    }

    protected static String changeExtension(String fileName, String oldExtension, String newExtension){
        if (fileName.contains(oldExtension)) return fileName.replace(oldExtension, newExtension);
        else return fileName + newExtension;
    }

    protected static String quote(String string){
        return "\"" + string + "\"";
    }
}
