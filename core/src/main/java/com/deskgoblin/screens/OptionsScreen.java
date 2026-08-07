package com.deskgoblin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.deskgoblin.DeskGoblinGame;

public class OptionsScreen extends ScreenAdapter {
    private DeskGoblinGame game;
    private GameScreen gameScreen;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;

    public OptionsScreen(DeskGoblinGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 360, camera);
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);

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

        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        GlyphLayout layout = new GlyphLayout();
        float worldWidth = viewport.getWorldWidth();

        layout.setText(font, "JOGO PAUSADO");
        font.draw(batch, "JOGO PAUSADO", (worldWidth - layout.width) / 2f, 250);

        layout.setText(font, "Pressione C para Continuar");
        font.draw(batch, "Pressione C para Continuar", (worldWidth - layout.width) / 2f, 150);

        layout.setText(font, "Pressione S para Sair (Tela de Inicio)");
        font.draw(batch, "Pressione S para Sair (Tela de Inicio)", (worldWidth - layout.width) / 2f, 100);
        batch.end();

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.C)) {
            game.setScreen(gameScreen);
        } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.S)) {
            game.setScreen(new StartScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
