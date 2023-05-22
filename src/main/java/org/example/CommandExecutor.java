package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CommandExecutor {
    private String operatingSys = System.getProperty("os.name").toLowerCase();

    private String[] toWindowsCommand(String... cmd){
        String[] options = {"cmd", "/C"};
        String[] concatenatedArray = Arrays.copyOf(options, options.length + cmd.length);
        System.arraycopy(cmd, 0, concatenatedArray, options.length, cmd.length);
//        System.out.printf(Arrays.toString(concatenatedArray));
        return concatenatedArray;
    }

    private String[] determineOSCommand(String... command){
        if (operatingSys.contains("windows"))
            return toWindowsCommand(command);
        else if (operatingSys.contains("nux") || operatingSys.contains("nix"))
            return command;
        //TODO: nux & nix & mac PLACEHOLDER
        else if (operatingSys.contains("mac"))
            return new String[]{};
        else return null;
    }

    public void runCommand(String... command) {
        command = toWindowsCommand(command);

        ProcessBuilder processBuilder = new ProcessBuilder().command(command);

        try {
            Process process = processBuilder.start();

            //read the output
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output = null;
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
