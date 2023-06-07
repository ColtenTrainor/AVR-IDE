package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CommandExecutor {

    private String[] toWindowsCommand(String... cmd){
        String[] options = {"cmd", "/C"};
        String[] concatenatedArray = Arrays.copyOf(options, options.length + cmd.length);
        System.arraycopy(cmd, 0, concatenatedArray, options.length, cmd.length);
        System.out.printf(Arrays.toString(concatenatedArray));
        return concatenatedArray;
    }

    private String[] determineOSCommand(String... command){
        switch (Settings.OperatingSystem) {
            case Windows -> { return toWindowsCommand(command); }
            case Linux -> { return command; }
            case Mac -> { return new String[]{}; } //TODO: mac support
            default -> { return null;}
        }
    }

    public void runCommand(String... command){
        runCommand(new File(""), command);
    }

    public void runCommand(File directory, String... command) {
        command = toWindowsCommand(command);

        ProcessBuilder processBuilder = new ProcessBuilder().command(command);
        processBuilder.directory(directory);

        try {
            Process process = processBuilder.start();

            //read the output
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output;
            while ((output = bufferedReader.readLine()) != null) {
                System.out.println(output);
            }

            //wait for the process to complete
            process.waitFor();

            //close the resources
            bufferedReader.close();
            process.destroy();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
