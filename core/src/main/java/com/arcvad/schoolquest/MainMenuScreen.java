package com.arcvad.schoolquest;

import com.arcvad.schoolquest.utils.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;

/** First screen of the application. Displayed after the application is created. */
class MainMenuScreen extends ScreenAdapter {

    private Stage stage;
    private Viewport viewport;
    private AssetManager assetManager;
    private Skin skin;
    private Table mainTable;
    private final JsonConfigManager jsonConfigManager;

    private AESCustomEncrypter encrypter = new AESCustomEncrypter();


    public MainMenuScreen(AssetManager assetManager){
        this.jsonConfigManager = new JsonConfigManager();
        this.assetManager = assetManager;
        skin = assetManager.get(Assets.UI_01_ATLAS);
    }
    private static final Logger logger = new Logger("ARC-SCREEN", Logger.DEBUG);

    @Override
    public void show() {
        jsonConfigManager.createConfig("user/logic.json");

        logger.debug("Screen is about to show :3");
        viewport = new ExtendViewport(1280, 720);
        stage = new Stage(viewport);

        mainTable = new Table();
        mainTable.setFillParent(true);

        stage.addActor(mainTable);
        ButtonAdder adder = new ButtonAdder(mainTable, skin);

        adder.addTextButton("Play");
        adder.addTextButton("Settings");
        if (jsonConfigManager.exists("key")){
            TextButton loginButton = adder.addTextButton("Profile");
            loginButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Forms forms = new Forms(skin, stage);
                    forms.openLoginForm();
                }
            });
            stage.addActor(loginButton);
        }else {
            TextButton registerButton = adder.addTextButton("Register");
            registerButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Forms forms = new Forms(skin, stage);
                    forms.openRegisterForm();
                }
            });
            stage.addActor(registerButton);
        }

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .1f, .15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
