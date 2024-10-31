package com.arcvad.schoolquest.player;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private String name;
    private String eyeLashStyle;
    private String eyeLashColor;
    private String hairStyle;
    private String hairColor;
    private String eyeColor;
    private Map<String, String> accessories;
    private String shoe;
    private String topCloth;
    private String bottomCloth;
    private String skinColor;

    // Default values
    public Player() {
        this.name = "Player";
        this.skinColor = "dark_brown";
        this.eyeLashStyle = "style_1";  // Default eyelash style
        this.eyeLashColor = "black";     // Default eyelash color
        this.hairStyle = "style_1";      // Default hairstyle
        this.hairColor = "black";        // Default hair color
        this.eyeColor = "black";          // Default eye color
        this.topCloth = "c_def_1";       // Default top cloth
        this.bottomCloth = "t_def_1";    // Default bottom cloth
        this.shoe = "s_def_1";           // Default shoe
        this.accessories = new HashMap<>(); // Initialize accessories list
    }

    // Getters and Setters for each attribute
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEyeLashStyle() { return eyeLashStyle; }
    public void setEyeLashStyle(String eyeLashStyle) { this.eyeLashStyle = eyeLashStyle; }

    public String getEyeLashColor() { return eyeLashColor; }
    public void setEyeLashColor(String skinColor) { this.skinColor = skinColor; }

    public String getSkinColor() { return eyeLashColor; }
    public void setSkinColor(String skinColor) { this.skinColor = skinColor; }

    public String getHairStyle() { return hairStyle; }
    public void setHairStyle(String hairStyle) { this.hairStyle = hairStyle; }

    public String getHairColor() {
        return hairColor;
    }
    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getEyeColor() {
        return eyeColor;
    }
    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Map<String, String> getAccessory() {
        return accessories;
    }
    public void setAccessory(Map<String, String> accessory) {
        this.accessories = accessory;
    }
    public void removeAccesory(String key) {
        accessories.remove(key);
    }

    public String getShoe() { return shoe; }
    public void setShoe(String shoe) { this.shoe = shoe; }

    public String getTopCloth() { return topCloth; }
    public void setTopCloth(String topCloth) { this.topCloth = topCloth; }

    public String getBottomCloth() { return bottomCloth; }
    public void setBottomCloth(String bottomCloth) { this.bottomCloth = bottomCloth; }

    // Convert to a Map for saving to file
    public Map<String, String> toMap() {
        Map<String, String> playerData = new HashMap<>();
        playerData.put("name", name);
        playerData.put("eyeLashStyle", eyeLashStyle);
        playerData.put("eyeLashColor", eyeLashColor);
        playerData.put("hairStyle", hairStyle);
        playerData.put("hairColor", hairColor);
        playerData.put("eyeColor", eyeColor);
        playerData.put("topCloth", topCloth);
        playerData.put("bottomCloth", bottomCloth);
        playerData.put("shoe", shoe);
        playerData.put("skinColor", skinColor);

        accessories.forEach((key, value) -> playerData.put("accessory_" + key, value));

        return playerData;
    }

    public static Player fromMap(Map<String, String> playerData) {
        Player player = new Player();
        player.setName(playerData.getOrDefault("name", "Player"));
        player.setEyeLashStyle(playerData.getOrDefault("eyeLashStyle", "style_1"));
        player.setEyeLashColor(playerData.getOrDefault("eyeLashColor", "black"));
        player.setHairStyle(playerData.getOrDefault("hairStyle", "style_1"));
        player.setHairColor(playerData.getOrDefault("hairColor", "black"));
        player.setEyeColor(playerData.getOrDefault("eyeColor", "black"));
        player.setTopCloth(playerData.getOrDefault("topCloth", "c_def_1"));
        player.setBottomCloth(playerData.getOrDefault("bottomCloth", "t_def_1"));
        player.setShoe(playerData.getOrDefault("shoe", "s_def_1"));
        player.setSkinColor(playerData.getOrDefault("skinColor", "dark_brown"));

        // Initialize accessories from the map
        Map<String, String> accessories = new HashMap<>();
        for (Map.Entry<String, String> entry : playerData.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("accessory_")) {
                accessories.put(key.substring("accessory_".length()), entry.getValue());
            }
        }
        player.setAccessory(accessories);

        return player;
    }
}
