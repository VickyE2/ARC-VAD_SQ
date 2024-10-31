package com.arcvad.schoolquest.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

public class JsonConfigManager {
    private final ObjectMap<String, String> configData = new ObjectMap<>();
    private final Json json = new Json();
    private String path;

    public void createConfig(String path) {
        FileHandle configFile = Gdx.files.local(path);
        if (!configFile.exists()) {
            configFile.writeString("{}", false); // create an empty JSON if not exists
        }
        this.path = path;
        loadConfigValues();
        Gdx.app.debug("ARC-JSON", "Created or loaded config file");
    }

    public void loadConfigValues() {
        try {
            String fileContent = Gdx.files.local(path).readString();
            JsonValue root = json.fromJson(null, fileContent);
            configData.clear();

            if (root != null) {
                for (JsonValue child : root) {
                    configData.put(child.name, child.asString());
                }
            }
            Gdx.app.debug("ARC-JSON", "Config file loaded");
        } catch (Exception e) {
            Gdx.app.error("ARC-JSON", "Error loading config file", e);
        }
    }

    public void setConfigValue(String key, String value) {
        configData.put(key, value);
        saveConfig();
    }

    public String getConfigValue(String key) {
        return configData.get(key);
    }

    public synchronized void saveConfig() {
        try {
            String jsonData = json.toJson(configData);
            FileHandle configFile = Gdx.files.local(path);
            configFile.writeString(jsonData, false);
            Gdx.app.debug("ARC-JSON", "Configuration file saved successfully");
        } catch (Exception e) {
            Gdx.app.error("ARC-JSON", "Error while saving configuration file...", e);
        }
    }

    public void clearTemp(){
        try {
            Gdx.files.local(path).delete();
            Gdx.app.log("ARC-JSON", "Temporary files removed successfully");
        }catch (Exception e) {
            Gdx.app.error("ARC-JSON", "Failed to remove temporary files: " + e.getCause());
        }
    }
}
