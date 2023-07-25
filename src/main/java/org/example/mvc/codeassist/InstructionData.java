package org.example.mvc.codeassist;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class InstructionData {

    private static final HashMap<String, InstructionData> allInstructionData = new HashMap<>();
    static { LoadDataFromConfig(); }

    private final String instruction;
    private final String[] arguments;
    private final String category;
    private final String shortDescription;
    private final boolean isEssential;

    public InstructionData(String instruction, String[] arguments, String category,
                           String shortDescription, boolean isEssential) {
        this.instruction = instruction;
        this.arguments = arguments;
        this.category = category;
        this.shortDescription = shortDescription;
        this.isEssential = isEssential;

        allInstructionData.put(instruction, this);
    }

    public String getInstruction(){ return instruction; }
    public List<String> getArguments(){ return Collections.unmodifiableList(Arrays.asList(arguments)); }
    public String getCategory(){ return category; }
    public String getShortDescription(){ return shortDescription; }
    public boolean getIsEssential(){ return isEssential; }

    public static Set<String> getInstructionSet(){ return allInstructionData.keySet(); }

    public static InstructionData getInstructionData(String instruction){
        return allInstructionData.get(instruction);
    }

    public static List<String> findMatchedInstructions(String word){
        List<String> list = new ArrayList<>();

        if (!word.equals(""))
            for (String inst : allInstructionData.keySet()){
                if (inst.contains(word.toUpperCase()))
                    list.add(inst);
            }
        return  list;
    }

    public static void LoadDataFromConfig(){
        var gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(InstructionData.class, new InstructionDataDeserializer());
        var gson = gsonBuilder.create();
        var instFile = new File("cfg/instructionDef.json");
        try {
            gson.fromJson(Files.readString(instFile.toPath()), InstructionData[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
