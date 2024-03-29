package org.example.mvc.codeassist;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class InstructionData {

    private static final HashMap<String, InstructionData> allInstructionData = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> instructionSetByCategory = new HashMap<>();
    private static String[] essentialInstructions;
    static { loadDataFromConfig(); }

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
        if (!instructionSetByCategory.containsKey(category))
            instructionSetByCategory.put(category, new ArrayList<>());
        instructionSetByCategory.get(category).add(instruction);
    }

    public String getInstruction(){ return instruction; }
    public List<String> getArguments(){ return Collections.unmodifiableList(Arrays.asList(arguments)); }
    public String getCategory(){ return category; }
    public String getShortDescription(){ return shortDescription; }
    public boolean getIsEssential(){ return isEssential; }
    public static boolean isAnInstruction(String inst){
        return allInstructionData.containsKey(inst.toUpperCase());
    }
    public static Set<String> getInstructionSet(){ return allInstructionData.keySet(); }
    public static ArrayList<String> getInstructionSetFromCategory(String category) { return instructionSetByCategory.get(category); }
    public static Set<String> getCategorySet() { return instructionSetByCategory.keySet(); }

    public static InstructionData getInstructionData(String instruction){
        return allInstructionData.get(instruction.toUpperCase());
    }

    public static List<String> findMatchedInstructions(String word){
        List<String> list = new ArrayList<>();

        if (!word.isEmpty())
            for (String inst : allInstructionData.keySet()){
                if (inst.contains(word.toUpperCase()))
                    list.add(inst);
            }
        return  list;
    }

    public static void loadDataFromConfig(){
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

    public static String getArgumentsAsString(String instruction){
        var args = getInstructionData(instruction).getArguments();
        var stringBuilder = new StringBuilder();
        for (int i = 0; i < args.size() - 1; i++) {
            stringBuilder.append(args.get(i)).append(", ");
        }
        try {
            stringBuilder.append(args.get(args.size() - 1));
        }catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("getArgumentAsString failed.");
        }
        return stringBuilder.toString();
    }

    public static String getShortInfo(String instruction){
        var instData = allInstructionData.get(instruction);
        return instruction + " " + getArgumentsAsString(instruction) +
                " : " + instData.getShortDescription();
    }

    public static String getLongInfo(){
        return "";
    }
}
