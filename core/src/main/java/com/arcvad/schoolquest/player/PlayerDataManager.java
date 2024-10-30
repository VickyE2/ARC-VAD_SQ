package com.arcvad.schoolquest.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {

    private static final String PREFS_NAME = "player_data";
    private final Preferences preferences;

    public PlayerDataManager() {
        preferences = Gdx.app.getPreferences(PREFS_NAME);
    }

    // Save player data to preferences
    public void savePlayerData(Player player) {
        for (Map.Entry<String, String> entry : player.toMap().entrySet()) {
            preferences.putString(entry.getKey(), entry.getValue());
        }
        preferences.flush();
    }

    public Player loadPlayerData() {
        Map<String, String> data = new HashMap<>();
        data.put("name", preferences.getString("name", "Player"));
        data.put("eyeLashStyle", preferences.getString("eyeLashStyle", "style_1"));
        data.put("eyeLashColor", preferences.getString("eyeLashColor", "black"));
        data.put("hairStyle", preferences.getString("hairStyle", "style_1"));
        data.put("hairColor", preferences.getString("hairColor", "black"));
        data.put("eyeColor", preferences.getString("eyeColor", "black"));
        data.put("topCloth", preferences.getString("topCloth", "c_def_1"));
        data.put("bottomCloth", preferences.getString("bottomCloth", "t_def_1"));
        data.put("shoe", preferences.getString("shoe", "s_def_1"));


        Map<String, String> accessories = new HashMap<>();

        int accessoryIndex = 0;
        String accessoryKey;
        while ((accessoryKey = preferences.getString("accessory_" + accessoryIndex, null)) != null) {
            accessories.put("accessory_" + accessoryIndex, accessoryKey);
            accessoryIndex++;
        }

        data.putAll(accessories);

        return Player.fromMap(data);
    }

    /**
     * <p style="color:red; text-align:center;"><b>This should only be used in case of player reset</b></p>
     */
    public void clearData() {
        preferences.clear();
        preferences.flush();
    }
}
