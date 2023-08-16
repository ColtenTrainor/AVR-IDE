package org.example.util;

import com.fazecast.jSerialComm.SerialPort;
import org.example.Settings;
import org.example.mvc.view.components.JConsole;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CommandExecutor {
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

    public static void runCommand(String... command){
        runCommand(new File(""), command);
    }

    public static void runCommand(File directory, String... command) {
        command = determineOSCommand(command);

        ProcessBuilder processBuilder = new ProcessBuilder().command(command);
        processBuilder.directory(directory);

        try {
            Process process = processBuilder.start();

            //TODO: only testing error stream
            InputStreamReader inputStreamReader = new InputStreamReader(process.getErrorStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output;
            while ((output = bufferedReader.readLine()) != null) {
                System.out.println("Spit: " + output);
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

    public static class Avra {
        public static void compile(File asmFile){
            switch (Settings.OperatingSystem){ //TODO: add include path to command
                case Windows -> runCommand(new File("avra"), "avra.exe", "\"" + asmFile.getAbsolutePath() + "\"",
                        "-o", "\"" + Settings.getDefaultSaveDir().getAbsolutePath() + "\\" + changeExtension(asmFile.getName()) + "\"");
                case Linux -> runCommand(new File("avra"), "avra", "\"" + asmFile.getAbsolutePath() + "\"",
                        "-o", "\"" + Settings.getDefaultSaveDir().getAbsolutePath() + "/" + changeExtension(asmFile.getName()) + "\"");
            }
        }
    }

    public static class AvrDude {
        public static void flash(File asmFile, SerialPort port){
            switch (Settings.OperatingSystem){
                case Windows -> runCommand(new File("avrdude"), "avrdude.exe", "-p", "m328p",
                        "-c", "arduino", "-P", port.getSystemPortName(), "-U", "flash:w:" +
                                changeExtension(asmFile.getAbsolutePath()) + ":i");
                case Linux -> {}
            }
        }
    }
}
