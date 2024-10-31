package com.arcvad.schoolquest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;

/** First screen of the application. Displayed after the application is created. */
public class MainScreen implements Screen {
    private final Batch spriteBatch = new SpriteBatch();
    private final Texture playerTexture = new Texture("entities/male/male_character_walking.png");

    private TiledMap map;
    private static final Logger logger = new Logger("ARC-SCREEN", Logger.DEBUG);
    private OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera camera = new OrthographicCamera();

    @Override
    public void show() {
        logger.debug("Screen is about to show :3");
        camera.setToOrtho(false, 800, 600);
        try {
            map = new TmxMapLoader().load("tile-maps/test002.tmx");
            mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);
            Gdx.app.log("ARC-TILER", "Map loaded Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.error("ARC-TILER", "Failed to load map or a layer is empty or incorrectly encoded.");
        }
    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        spriteBatch.draw(playerTexture, 0f, 0f);
        ScreenUtils.clear(0, 0, 0, 1);

        // Update the camera
        camera.update();
        mapRenderer.setView(camera);

        // Render the map
        mapRenderer.render();
        spriteBatch.end();
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
