package com.arcvad.schoolquest.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    private AssetManager assetManager = new AssetManager();

    public static final AssetDescriptor<Skin> UI_01_ATLAS = new AssetDescriptor<>("ui/ui_t1.json", Skin.class, new SkinLoader.SkinParameter("ui/ui_t1.atlas"));

    public void loadAll(){assetManager.load(UI_01_ATLAS);}

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
