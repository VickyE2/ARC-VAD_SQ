package com.arcvad.schoolquest.player;

public class PlayerMovement {
    private String playerId;
    private float x;
    private float y;
    private float z;
    private String direction;


    public String getPlayerId() { return playerId.toString(); }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getZ() { return z; }
    public void setZ(float z) { this.z = z; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
}
