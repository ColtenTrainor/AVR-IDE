package org.example.util.cli;

import org.example.Settings;
import org.example.util.cli.tools.AvrDude;
import org.example.util.cli.tools.Avra;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandExecutor {
    private final ArrayList<CommandActionListener> actionListeners = new ArrayList<>();
    public final Avra avra = new Avra(
            this, new File("avra"),
            "avra.exe", "avra", "avra");
    public final AvrDude avrDude = new AvrDude(
            this, new File("avrdude"), actionListeners,
            "avrdude.exe", "", "");

    public void addActionListener(CommandActionListener actionListener){ actionListeners.add(actionListener); }

    private void forwardErrorReceived(CommandErrorEvent e){
        for (var listener : actionListeners) {
            listener.errorReceived(e);
        }
    }

    private void forwardOutputReceived(CommandOutputEvent e){
        for (var listener : actionListeners) {
            listener.outputReceived(e);
        }
    }

    private static String[] toWindowsCommand(String... cmd){
        String[] options = {"cmd", "/C"};
        String[] concatenatedArray = Arrays.copyOf(options, options.length + cmd.length);
        System.arraycopy(cmd, 0, concatenatedArray, options.length, cmd.length);
        System.out.printf(Arrays.toString(concatenatedArray));
        return concatenatedArray;
    }

    private static String[] determineOSCommand(String... command){
        return switch (Settings.OperatingSystem) {
            case Windows -> toWindowsCommand(command);
            case Linux, Mac -> command;
        };
    }

    public void runCommand(File directory, String... command) {
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
                forwardErrorReceived(errorEvent);
            }

            inputStreamReader = new InputStreamReader(process.getErrorStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            String output;
            while ((output = bufferedReader.readLine()) != null) {
                var outputEvent = new CommandOutputEvent(this, output);
                forwardOutputReceived(outputEvent);
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
}

