package com.arcvad.schoolquest;

import com.arcvad.schoolquest.player.Player;
import com.arcvad.schoolquest.player.PlayerDataManager;
import com.arcvad.schoolquest.web_socket.ArcSocket;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;

import java.util.Map;

import static com.arcvad.schoolquest.global.GlobalServer.socket;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ARCCore extends Game {

    private PlayerDataManager manager;

    @Override
    public void create() {

        socket = new ArcSocket("localhost:3000");
        socket.addListener(new WebSocketListener() {
            @Override
            public boolean onOpen(WebSocket webSocket) {
                try {
                    Gdx.app.log("ARC-SOCKET", "Connected to ARC-Father");
                    webSocket.send("Dad what's up?");
                    return true;
                }catch (Exception e){
                    return false;
                }
            }

            @Override
            public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
                return false;
            }

            @Override
            public boolean onMessage(WebSocket webSocket, String packet) {
                try {
                    manager = new PlayerDataManager();
                    Gdx.app.log("ARC-SOCKET", "Received packet: " + packet);

                    // Check if the packet contains player data
                    if (packet.startsWith("playerDataResponse")) {
                        // Assuming the packet is formatted as "playerDataResponse: { ... }"
                        String jsonData = packet.substring(packet.indexOf(":") + 1).trim();
                        if (!jsonData.isEmpty()) {
                            Map<String, String> playerData = manager.parseResponse(jsonData);
                            manager.savePlayerData(playerData);
                        } else {
                            Gdx.app.error("ARC-JSON", "Received null or empty response for player data request");
                        }
                    }

                    return true;
                } catch (WebSocketException e) {
                    return false;
                }
            }

            @Override
            public boolean onMessage(WebSocket webSocket, byte[] packet) {
                try {
                    for (Byte currentByte : packet) {
                        Gdx.app.log("ARC-SOCKET", "Received packet byte: " + currentByte);
                    }
                    return true;
                }catch (WebSocketException e){
                    return false;
                }
            }

            @Override
            public boolean onError(WebSocket webSocket, Throwable error) {
                try {
                    Gdx.app.log("ARC-SOCKET", "Encountered error during runtime: " + error.getMessage());
                    Gdx.app.log("ARC-SOCKET", "Seems to be caused by: " + error.getCause());
                    return true;
                }catch (WebSocketException e){
                    return false;
                }
            }
        });

        try {
            // Connect to the WebSocket server
            socket.connect();
        }catch (WebSocketException e) {
            Gdx.app.error("ARC-SOCKET", "Failed to connect to server");
        }

        PlayerDataManager dataManager = new PlayerDataManager();
        Player player = dataManager.loadPlayerData();

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        setScreen(new MainScreen());

        Gdx.app.debug("ARC-CORE", "Game initialized and MainScreen set as the active screen.");
        Gdx.app.log("ARC-CORE", "Loaded player with name: " + player.getName());
    }

    @Override
    public void dispose(){
        Gdx.app.debug("ARC-CORE", "Game is closing...");
        socket.close(9999, "Game closed");
    }
}
