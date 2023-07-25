package org.example.mvc.codeassist;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class InstructionDataDeserializer implements JsonDeserializer<InstructionData> {
    @Override
    public InstructionData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var jsonObj = json.getAsJsonObject();
        var argsJson = jsonObj.get("args").getAsJsonArray();
        String[] args = new String[argsJson.size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = argsJson.get(i).getAsString();
        }
        return new InstructionData(
                jsonObj.get("inst").getAsString(),
                args,
                jsonObj.get("category").getAsString(),
                jsonObj.get("short_desc").getAsString(),
                jsonObj.get("essential").getAsBoolean()
        );
    }
}
