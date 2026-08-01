package com.deskgoblin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.deskgoblin.DeskGoblinGame;

public class TutorialScreen extends ScreenAdapter {
    private DeskGoblinGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    public TutorialScreen(DeskGoblinGame game) {
        this.game = game;
        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("VT323-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "TUTORIAL", 280, 300);
        font.draw(batch, "- O Pergaminho registra os pacientes na AVL Tree.", 50, 250);
        font.draw(batch, "- O Orbe Esquerdo puxa pacientes em estado grave (Min Heap).", 50, 200);
        font.draw(batch, "- O Orbe Direito gerencia as macas (Hash Table).", 50, 150);
        font.draw(batch, "Clique para Fechar e Iniciar o Loop do Jogo", 150, 80);
        batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
