package impl.regAndIns;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RulesInit {
    private static class Rule{
        private final String source;
        private final String destination;
        private final String description;
        private Rule(String source, String destination, String description){
            this.source = source;
            this.destination = destination;
            this.description = description;
        }
    }
    private HashMap<String, String> instructionMap;
    public RulesInit (){
        this.instructionMap = new HashMap<>();
        List<String> readLinesFromFile = readLines();

        readLinesFromFile.forEach(line -> {
            // ignoring lines with # and empty line for now
            if( !line.contains("#") && !line.equals("")){
                String[] instruction = line.split(" ", 2);
                String instructionName = instruction[0].strip();

                // TODO: some inst have multiple syntax that needs handling
                if (!instructionMap.containsKey(instructionName)){
                    instructionMap.put(instructionName, instruction[1].strip());
                }
            }
        });
    }
    private List<String> readLines(){
        try {
            return Files.readAllLines(new File("D:\\Summer 2023\\AVR-IDE\\src\\main\\java\\impl\\regAndIns\\instructionSyntax.txt").toPath());
        }catch (IOException ex){
            System.out.println("Read rules failed.");
            ex.printStackTrace();
            return null;
        }
    }
    public void printRuleMap(){
        instructionMap.forEach((hkey, hval)->{
            System.out.printf("Instruction: %s --- Info: %s\n", hkey, hval);
        });
        System.out.println(instructionMap.size());
    }

    public List<String> getKeySet(){
        return new ArrayList<>(this.instructionMap.keySet());
    }

    private void splittingStuff(List<String> readLines){
        readLines.forEach(line -> {
            String[] instructionANDDescription = line.split(":");
            String description = instructionANDDescription[1];

            String[] InstructionComponents = instructionANDDescription[0].split(",");
            String inst = "", dest = "", src = "";
            if (InstructionComponents.length != 1){

            }

        });
    }

}
