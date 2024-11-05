package com.arcvad.schoolquest;

import com.arcvad.schoolquest.player.Player;
import com.arcvad.schoolquest.player.PlayerDataManager;
import com.arcvad.schoolquest.utils.Assets;
import com.arcvad.schoolquest.web_socket.ArcSocket;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.ray3k.stripe.FreeTypeSkinLoader;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.arcvad.schoolquest.global.GlobalServer.socket;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ARCCore extends Game {

    private PlayerDataManager manager;
    private Assets assets;
    public float pingTime;
    public boolean waitingForPing;
    private long pingInterval = 1000;  // interval in seconds
    private long pongTimeout = 5000;   // timeout in seconds
    private Timer timer = new Timer();

    @Override
    public void create() {
        waitingForPing = false;
        assets = new Assets();
        socket = new ArcSocket("0.0.0.0", 55489);
        socket.addListener(new WebSocketListener() {
            @Override
            public boolean onOpen(WebSocket webSocket) {
                try {
                    Gdx.app.log("ARC-SOCKET", "Connected to ARC-Father");
                    webSocket.send("Dad what's up?");
                    startPingTask();
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
                    Gdx.app.log("ARC-SOCKET", "Received packet: " + packet);

                    if (packet.startsWith("playerDataResponse")) {
                        manager = new PlayerDataManager();

                        String jsonData = packet.substring(packet.indexOf(":") + 1).trim();
                        if (!jsonData.isEmpty()) {
                            Map<String, String> playerData = manager.parseResponse(jsonData);
                            createdPlayer = manager.savePlayerData(playerData);
                        } else {
                            Gdx.app.error("ARC-JSON", "Received null or empty response for player data request");
                        }
                    }else if (packet.equals("PING")) {
                        float finalPing = System.currentTimeMillis() - pingTime;
                        Gdx.app.log("ARC-SOCKET", "Received ping. Latency: " + finalPing + "ms");
                        waitingForPing = false;
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
            socket.connect();
        }catch (WebSocketException e) {
            Gdx.app.error("ARC-SOCKET", "Failed to connect to server");
        }

        /*
        PlayerDataManager dataManager = new PlayerDataManager();
        Player player = dataManager.loadPlayerData();
         */

        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Gdx.app.debug("ARC-CORE", "Game initialized and MainScreen set as the active sc reen.");
        Gdx.app.log("ARC-CORE", "Loaded player with name: " + player.getName());

        assets.loadAll();
        assets.getAssetManager().finishLoading();

        setScreen(new MainMenuScreen(assets.getAssetManager()));

    }

    @Override
    public void dispose(){
        manager = new PlayerDataManager();
        stopPingTask();
        Gdx.app.debug("ARC-CORE", "Game is closing...");
        socket.close(9999, "Game closed");
        manager.clearLocalData();
    }

    private void stopPingTask() {
        timer.cancel();  // Clears all scheduled tasks
    }

    private  void startPingTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                    try {
                        sendPing();
                        Gdx.app.log("ARC-SOCKET", "Ping started at: " + pingTime);
                    } catch (WebSocketException e) {
                        Gdx.app.error("ARC-SOCKET", "Server seems to be unreachable");
                    }
                }
        };
        timer.schedule(task, 100);
    }
    private void sendPing() {
        TimerTask task = new TimerTask()  {
            @Override
            public void run() {
                if (waitingForPing) {
                    if (System.currentTimeMillis() - pingTime > pongTimeout) {
                        System.out.println("Server timeout...Reconnecting");
                    }
                }else{
                    waitingForPing = true;
                    pingTime = System.currentTimeMillis();
                    socket.send("ping");
                    System.out.println("Sending ping...");
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, pingInterval);
    }
}
