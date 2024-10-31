package com.arcvad.schoolquest.player;

import com.arcvad.schoolquest.utils.JsonConfigManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {
    private static final String RETRIEVED_PLAYER_DATA = "server_player_data.json";
    private final JsonConfigManager jsonConfigManager;

    public PlayerDataManager() {
        jsonConfigManager = new JsonConfigManager();
        jsonConfigManager.createConfig(RETRIEVED_PLAYER_DATA);
    }

    // Save player data to JSON config
    public void savePlayerData(Map<String, String> playerData) {
        for (Map.Entry<String, String> entry : playerData.entrySet()) {
            Gdx.app.debug("ARC-PLAYER", "Key: " + entry.getKey() + " with value: " + entry.getValue());
            jsonConfigManager.setConfigValue(entry.getKey(), entry.getValue());
        }
        Gdx.app.log("ARC-JSON", "Player data saved to config");
    }

    public Player loadPlayerData() {
        Map<String, String> data = new HashMap<>();

        // Load player data from JSON config
        data.put("name", jsonConfigManager.getConfigValue("name"));
        data.put("eyeLashStyle", jsonConfigManager.getConfigValue("eyeLashStyle"));
        data.put("eyeLashColor", jsonConfigManager.getConfigValue("eyeLashColor"));
        data.put("hairStyle", jsonConfigManager.getConfigValue("hairStyle"));
        data.put("hairColor", jsonConfigManager.getConfigValue("hairColor"));
        data.put("eyeColor", jsonConfigManager.getConfigValue("eyeColor"));
        data.put("topCloth", jsonConfigManager.getConfigValue("topCloth"));
        data.put("bottomCloth", jsonConfigManager.getConfigValue("bottomCloth"));
        data.put("shoe", jsonConfigManager.getConfigValue("shoe"));
        data.put("skinColor", jsonConfigManager.getConfigValue("skinColor"));

        // Load accessories safely
        Map<String, String> accessories = new HashMap<>();
        int accessoryIndex = 0;
        while (true) {
            String accessoryKey = jsonConfigManager.getConfigValue("accessory_" + accessoryIndex);
            if (accessoryKey == null) {
                break; // Exit if the key doesn't exist
            }
            accessories.put("accessory_" + accessoryIndex, accessoryKey);
            accessoryIndex++;
        }

        data.putAll(accessories);

        // Return the player object created from the loaded data
        return Player.fromMap(data);
    }


    /**
     * <p style="color:red; text-align:center;"><b>This should only be used in case of player reset</b></p>
     */
    public void clearLocalData() {
        jsonConfigManager.clearTemp();
    }

    // Helper method to parse the server response into a player data map
    public Map<String, String> parseResponse(String response) {
        Map<String, String> resultMap = new HashMap<>();
        JsonReader jsonReader = new JsonReader();
        try {
            JsonValue root = jsonReader.parse(response);
            for (JsonValue entry : root) {
                resultMap.put(entry.name(), entry.asString());
            }
        } catch (Exception e) {
            Gdx.app.error("ARC-PLAYER", "Error parsing JSON response", e);
        }
        return resultMap;
    }
}
