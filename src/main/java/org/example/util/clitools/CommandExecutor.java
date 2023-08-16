package org.example.util.clitools;

import com.fazecast.jSerialComm.SerialPort;
import org.example.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandExecutor {
    public Avra avra = new Avra();
    public AvrDude avrDude = new AvrDude();
    private ArrayList<CommandActionListener> actionListeners = new ArrayList<>();

    public void addActionListener(CommandActionListener actionListener){ actionListeners.add(actionListener); }

    private static String[] toWindowsCommand(String... cmd){
        String[] options = {"cmd", "/C"};
        String[] concatenatedArray = Arrays.copyOf(options, options.length + cmd.length);
        System.arraycopy(cmd, 0, concatenatedArray, options.length, cmd.length);
        System.out.printf(Arrays.toString(concatenatedArray));
        return concatenatedArray;
    }

    private static String[] determineOSCommand(String... command){
        switch (Settings.OperatingSystem) {
            case Windows -> { return toWindowsCommand(command); }
            case Linux -> { return command; }
            case Mac -> { return new String[]{}; } //TODO: mac support
            default -> { return null;}
        }
    }

    public  void runCommand(String... command){
        runCommand(new File(""), command);
    }

    public  void runCommand(File directory, String... command) {
        command = determineOSCommand(command);

        ProcessBuilder processBuilder = new ProcessBuilder().command(command);
        processBuilder.directory(directory);

        try {
            Process process = processBuilder.start();

            InputStreamReader inputStreamReader = new InputStreamReader(process.getErrorStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String error;
            while ((error = bufferedReader.readLine()) != null) {
                var errorEvent = new CommandErrorEvent(this, error);
                for (var listener : actionListeners) {
                    listener.errorReceived(errorEvent);
                }
            }

            inputStreamReader = new InputStreamReader(process.getErrorStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            String output;
            while ((output = bufferedReader.readLine()) != null) {
                var outputEvent = new CommandOutputEvent(this, output);
                for (var listener : actionListeners) {
                    listener.outputReceived(outputEvent);
                }
            }

            //wait for the process to complete
            process.waitFor();

            //close the resources
            bufferedReader.close();
            process.destroy();

        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
        }
    }

    private static String changeExtension(String fileName){
        if (fileName.contains(".asm")) return fileName.replace(".asm", ".hex");
        else return fileName + ".hex";
    }

    public  class Avra {
        public  void compile(File asmFile){
            switch (Settings.OperatingSystem){ //TODO: add include path to command
                case Windows -> runCommand(new File("avra"), "avra.exe", "\"" + asmFile.getAbsolutePath() + "\"",
                        "-o", "\"" + Settings.getDefaultSaveDir().getAbsolutePath() + "\\" + changeExtension(asmFile.getName()) + "\"");
                case Linux -> runCommand(new File("avra"), "avra", "\"" + asmFile.getAbsolutePath() + "\"",
                        "-o", "\"" + Settings.getDefaultSaveDir().getAbsolutePath() + "/" + changeExtension(asmFile.getName()) + "\"");
            }
        }
    }

    public class AvrDude {
        public void flash(File asmFile, SerialPort port){
            if (port == null) {
                System.out.println("port is null, cannot flash"); //TODO: put this in app's console
                return;
            }
            switch (Settings.OperatingSystem){
                case Windows -> runCommand(new File("avrdude"), "avrdude.exe", "-p", "m328p",
                        "-c", "arduino", "-P", port.getSystemPortName(), "-U", "flash:w:" +
                                changeExtension(asmFile.getAbsolutePath()) + ":i");
                case Linux -> {}
            }
        }
    }
}

