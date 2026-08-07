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

public class TutorialScreen extends ScreenAdapter {
    private DeskGoblinGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont titleFont;
    private OrthographicCamera camera;
    private Viewport viewport;

    public TutorialScreen(DeskGoblinGame game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 360, camera);
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("VT323-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter titleParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParam.size = 28;
        titleParam.color = Color.YELLOW;
        titleFont = generator.generateFont(titleParam);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        GlyphLayout layout = new GlyphLayout();

        layout.setText(titleFont, "COMO JOGAR");
        titleFont.draw(batch, "COMO JOGAR", (viewport.getWorldWidth() - layout.width) / 2f, 330);

        font.draw(batch, "- Pacientes chegam ao balcao. Clique neles para", 50, 280);
        font.draw(batch, "  ver seu cartao de ID e ouvir sua queixa.", 50, 260);

        font.draw(batch, "- Use o Pergaminho para cadastrar o paciente", 50, 230);
        font.draw(batch, "  (nome, ID e gravidade), buscar ou ver a Arvore AVL.", 50, 210);

        font.draw(batch, "- Use o Orbe Esquerdo (pequeno) para ver a fila de", 50, 180);
        font.draw(batch, "  prioridade e alocar o paciente mais grave a uma maca.", 50, 160);

        font.draw(batch, "- Use o Orbe Direito (grande) para visualizar as macas,", 50, 130);
        font.draw(batch, "  ver pacientes internados e acompanhar procedimentos.", 50, 110);

        font.draw(batch, "- Pressione ESC para pausar o jogo.", 50, 80);

        layout.setText(font, "Clique para Iniciar");
        font.draw(batch, "Clique para Iniciar", (viewport.getWorldWidth() - layout.width) / 2f, 40);
        batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game));
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
        titleFont.dispose();
    }
}