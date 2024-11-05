package com.arcvad.schoolquest.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonAdder {
    private Table mainTable;
    private Skin skin;

    public ButtonAdder(Table mainTable, Skin skin){
        this.mainTable = mainTable;
        this.skin = skin;
    }

    public TextButton addTextButton(String name) {
        TextButton button = new TextButton(name, skin);
        mainTable.add(button).padBottom(20);
        mainTable.row();

        return button;
    }

    public TextButton addTextButton(String name, int posx, int posy) {
        TextButton button = new TextButton(name, skin);
        button.setPosition(posx, posy);
        mainTable.add(button).padBottom(20);
        mainTable.row();

        return button;
    }

    public TextButton addTextButton(String name, int posx, int posy, int sizex, int sizey) {
        TextButton button = new TextButton(name, skin);
        button.setPosition(posx, posy);
        button.setSize(sizex, sizey);
        mainTable.add(button).padBottom(20);
        mainTable.row();

        return button;
    }

    public TextButton addTextButton(String name, float sizex, float sizey) {
        TextButton button = new TextButton(name, skin);
        button.setSize(sizex, sizey);
        mainTable.add(button).padBottom(20);
        mainTable.row();

        return button;
    }

    public ImageButton addImageButton(Texture buttonTexture) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(buttonTexture);

        ImageButton button = new ImageButton(drawable);
        mainTable.add(button).padBottom(20);
        mainTable.row();

        return button;
    }
}
