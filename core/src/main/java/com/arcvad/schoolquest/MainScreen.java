package com.arcvad.schoolquest;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;

/** First screen of the application. Displayed after the application is created. */
public class MainScreen implements Screen {
    private final Batch spriteBatch = new SpriteBatch();
    private final Texture playerTexture = new Texture("assets/entities/player.png");
    private final TiledMap map = new TmxMapLoader().load("assets/tilemaps/tilemap.tmx");
    private static final Logger logger = new Logger("ARC-SCREEN", Logger.DEBUG);
    private final OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);
    private final OrthographicCamera camera = new OrthographicCamera();

    @Override
    public void show() {
        logger.debug("Screen is about to show :3");
        camera.setToOrtho(false, 800, 600);
    }

    @Override
    public void render(float delta) {
        spriteBatch.draw(playerTexture, 0f, 0f);
        ScreenUtils.clear(0, 0, 0, 1);

        // Update the camera
        camera.update();
        mapRenderer.setView(camera);

        // Render the map
        mapRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
    }
}