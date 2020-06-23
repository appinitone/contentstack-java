package com.contentstack.test;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadJsonFile {
    String currentDirectory = System.getProperty("user.dir");
    String credential = currentDirectory+"/src/test/java/com/contentstack/test/config.json";
    public JSONObject readCredentials() {
        try {
            String stringCredential = new String((Files.readAllBytes(Paths.get(credential))));
            return new JSONObject(stringCredential).optJSONObject("keys");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

