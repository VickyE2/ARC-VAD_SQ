package com.arcvad.schoolquest.player;

import com.arcvad.schoolquest.utils.AESCustomEncrypter;
import com.arcvad.schoolquest.utils.JsonConfigManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import javax.crypto.SecretKey;
import java.util.*;

public class PlayerDataManager {
    private static final String RETRIEVED_PLAYER_DATA = "server_player_data.json";
    private final JsonConfigManager jsonConfigManager;

    public PlayerDataManager() {
        jsonConfigManager = new JsonConfigManager();
        jsonConfigManager.createConfig(RETRIEVED_PLAYER_DATA);
    }

    // Save player data to JSON config
    public boolean savePlayerData(Map<String, String> playerData) {
        if (!playerData.isEmpty()) {
            for (Map.Entry<String, String> entry : playerData.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // Log the key-value pair for debugging
                Gdx.app.debug("ARC-PLAYER", "Key: " + key + " with value: " + value);

                // Handle null values explicitly
                if (value != null && !value.equals("null")) {
                    jsonConfigManager.setConfigValue(key, value);
                } else {
                    // Optionally handle null or empty cases
                    jsonConfigManager.setConfigValue(key, null);
                }
            }

            jsonConfigManager.createConfig("user/logic.json");
            if (!jsonConfigManager.exists("key")) {
                try{
                    SecretKey key = AESCustomEncrypter.generateSecretKey();
                    jsonConfigManager.setConfigValue("key", key.toString());
                    jsonConfigManager.setConfigValue(AESCustomEncrypter.encrypt(
                        "username", key), AESCustomEncrypter.encrypt(
                        playerData.get("username"), key));
                    jsonConfigManager.setConfigValue(AESCustomEncrypter.encrypt(
                        "password", key), AESCustomEncrypter.encrypt(
                        playerData.get("password"), key));
                }catch (Exception e){
                    Gdx.app.error("ARC-ENCRYPTER", "Failed to generate custom key");
                }
            }
            Gdx.app.log("ARC-JSON", "Player data saved to config");
            return true;
        }
        return false;
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

        // Load clothes and accessories
        data.put("topClothes", loadClothes("topCloth"));
        data.put("bottomClothes", loadClothes("bottomCloth"));
        data.put("shoes", loadClothes("shoe"));
        data.put("accessories", loadClothes("accessory"));

        // Return the player object created from the loaded data
        return Player.fromMap(data);
    }

    private String loadClothes(String type) {
        List<Map<String, String>> clothes = new ArrayList<>();
        int index = 0;
        while (true) {
            Map<String, String> cloth = new HashMap<>();
            String rarity = jsonConfigManager.getConfigValue(type + "_" + index + "_rarity");
            String id = jsonConfigManager.getConfigValue(type + "_" + index + "_id");

            if (rarity == null && id == null) break; // Exit if no more items

            if (rarity != null) cloth.put("rarity", rarity);
            if (id != null) cloth.put("id", id);

            clothes.add(cloth);
            index++;
        }
        // Serialize the list of clothes as a JSON string
        return jsonConfigManager.getJson().toJson(clothes); // Ensure you have a way to serialize to JSON
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
        try {
            Json json = new Json();
            Map<String, Object> jsonValue = json.fromJson(HashMap.class, response);
            for (Map.Entry<String, Object> entry : jsonValue.entrySet()) {
                resultMap.put(entry.getKey(), String.valueOf(entry.getValue()));
            }

            Gdx.app.log("ARC-PLAYER","Successfully parsed player: " + jsonValue.getOrDefault("name", "player"));
        } catch (Exception e) {
            Gdx.app.error("ARC-PLAYER", "Error parsing JSON response", e);
        }
        return resultMap;
    }
}
