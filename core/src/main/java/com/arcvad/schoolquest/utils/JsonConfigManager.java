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
                    // Check for cycles or deep structures
                    if (child.isString()) {
                        configData.put(child.name, child.asString());
                    } else if (child.isArray()) {
                        if (child.isArray()) {
                            // Handle arrays with a different approach to prevent recursion
                            StringBuilder arrayString = new StringBuilder("[");
                            for (JsonValue item : child) {
                                // Serialize each item to a string, ensuring no recursion happens
                                if (item.isObject() || item.isArray()) {
                                    // If it's an object or array, serialize it instead of calling asString()
                                    arrayString.append(json.toJson(item)).append(",");
                                } else {
                                    // Otherwise, just append the string representation of the value
                                    arrayString.append(item.asString()).append(",");
                                }
                            }

                            // Remove the trailing comma and close the array
                            if (arrayString.length() > 1) {
                                arrayString.setLength(arrayString.length() - 1);
                            }
                            arrayString.append("]");
                            configData.put(child.name, arrayString.toString());
                        }
                    } else if (child.isObject()) {
                        // Handle object differently to avoid cycles
                        String objectString = json.toJson(child); // Ensure child is not self-referential
                        configData.put(child.name, objectString);
                    } else {
                        Gdx.app.log("ARC-JSON", "Unhandled value type for key: " + child.name);
                    }
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

    public Boolean getBooleanValue(String key) {
        return Boolean.getBoolean(configData.get(key));
    }

    public Boolean exists(String key) {
        return configData.containsKey(key);
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
