package com.deskgoblin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.deskgoblin.DeskGoblinGame;

public class OptionsScreen extends ScreenAdapter {
    private DeskGoblinGame game;
    private GameScreen gameScreen;
    private SpriteBatch batch;
    private BitmapFont font;

    public OptionsScreen(DeskGoblinGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("VT323-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.YELLOW;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "JOGO PAUSADO", 250, 250);
        font.draw(batch, "Pressione C para Continuar", 180, 150);
        font.draw(batch, "Pressione S para Sair (Tela de Início)", 130, 100);
        batch.end();

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.C)) {
            // S5 -> Despausar o jogo
            game.setScreen(gameScreen);
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.S)) {
            // S6 -> Voltar a tela de inicio
            game.setScreen(new StartScreen(game));
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
