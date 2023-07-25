package org.example.mvc.codeassist;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InstructionRules {
    private static class Rule{
        private final String description;
        private final String colorCode;
        private Rule(String description, String colorCode){
            this.description = description;
            this.colorCode = colorCode;
        }
    }
    public String getColorCode(String inst){
        Rule rule = this.instructionMap.get(inst);
        return rule.colorCode;
    }

    public final HashMap<String, Rule> instructionMap;
    public InstructionRules(){
        this.instructionMap = new HashMap<>();
        List<String> readLinesFromFile = readLines();

        readLinesFromFile.forEach(line -> {
            // ignoring lines with # and empty line for now
            if( !line.contains("#") && !line.equals("")){
                String[] instruction = line.split(" ", 2);
                String instructionName = instruction[0].strip();

                // TODO: some inst have multiple syntax that needs handling
                if (!instructionMap.containsKey(instructionName)){
                    instructionMap.put(instructionName, new Rule(line, "#EBA430"));
                }
            }
        });
    }

    private List<String> readLines(){
        try {
            return Files.readAllLines(new File("cfg/instructionDescriptions.cfg").toPath());
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

    public boolean isAnInstruction(String word){
        return instructionMap.containsKey(word.toUpperCase());
    }

    public String getInstructionDescription(String inst){
        return instructionMap.get(inst.toUpperCase()).description;
    }

    public List<String> findMatchedInstructions(String word){
        List<String> list = new ArrayList<>();

        if (!word.equals(""))
            for (String inst : instructionMap.keySet()){
                if (inst.contains(word.toUpperCase()))
                    list.add(inst);
            }
        return  list;
    }

    private void splittingStuff(List<String> readLines){
        readLines.forEach(line -> {
            String[] instructionANDDescription = line.split("\\s*:\\s*");
            String description = line.split(" ",2)[1];

            String[] instNDestSource = instructionANDDescription[0].split("\\s+", 2);
            String inst = instNDestSource[0], dest = "", src = "";

            String[] destNSource = instNDestSource[1].split("\\s*,\\s*");
            if (destNSource.length > 1){
                int destIndex = destNSource[0].contains("Rd") ? 0 : destNSource[1].contains("Rd") ? 1 : -1;
                int sourceIndex = destNSource[0].contains("Rr") ? 0 : destNSource[1].contains("Rr") ? 1 : -1;
                dest = destIndex >= 0 ? destNSource[destIndex] : "";
                src = sourceIndex >= 0 ? destNSource[sourceIndex] : "";
            }
            else if (destNSource.length == 1){
                dest = destNSource[0].contains("Rd") ? destNSource[0] : "";
                src = destNSource[0].contains("Rs") ? destNSource[0] : "";
            }

        });
    }

}
