package com.arcvad.schoolquest;

import com.arcvad.schoolquest.player.Player;
import com.arcvad.schoolquest.player.PlayerDataManager;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ARCCore extends Game {


    @Override
    public void create() {
        PlayerDataManager dataManager = new PlayerDataManager();
        Player player = dataManager.loadPlayerData();

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        setScreen(new MainScreen());

        Gdx.app.debug("ARC-CORE", "Game initialized and MainScreen set as the active screen.");
        Gdx.app.log("ARC-CORE", "Loaded player with name: " + player.getName());
    }
}
