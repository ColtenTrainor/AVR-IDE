package org.example.util.cli.tools;

import org.example.util.cli.CommandExecutor;

import java.io.File;

public class Avra extends CliTool{
    private final File includesDir;
    public Avra(CommandExecutor commandExecutor, File toolDir,
                String windowsExecutable, String linuxExecutable, String macExecutable) {
        super(commandExecutor, toolDir, windowsExecutable, linuxExecutable, macExecutable);

        includesDir = new File(toolDir.getPath() + "/includes");
    }

    public void compile(File asmFile){
        commandExecutor.runCommand(ToolDir, getExecutable(),
                asmFile.getAbsolutePath(),
                "-o", changeExtension(asmFile.getAbsolutePath(), ".asm", ".hex"),
                "-I", includesDir.getAbsolutePath());
    }
}
