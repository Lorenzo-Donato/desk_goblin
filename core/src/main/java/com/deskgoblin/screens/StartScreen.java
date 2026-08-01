package com.deskgoblin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.deskgoblin.DeskGoblinGame;

public class StartScreen extends ScreenAdapter {
    private DeskGoblinGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    public StartScreen(DeskGoblinGame game) {
        this.game = game;
        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("VT323-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "DESK GOBLIN", 250, 250);
        font.draw(batch, "Clique para Iniciar", 220, 150);
        font.draw(batch, "Pressione ESC para Sair", 200, 100);
        batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new TutorialScreen(game));
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
