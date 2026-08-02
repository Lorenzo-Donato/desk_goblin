package com.deskgoblin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.deskgoblin.DeskGoblinGame;
import com.deskgoblin.model.HospitalManager;
import com.deskgoblin.model.entities.Patient;
import com.deskgoblin.model.entities.Bed;

public class GameScreen extends ScreenAdapter {
    private DeskGoblinGame game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture groundTexture, tableTexture, orbOneTexture, orbTwoTexture, scrollTexture, 
                    inkwellTexture, greenButtonTexture, redButtonTexture, handTexture, patientTexture;
    
    private BitmapFont font;
    private Vector3 mousePos;
    
    private HospitalManager hospitalManager;
    private Patient currentPatient; // Paciente que o jogador focou temporariamente
    private String selectedBedId = "M1"; // Para o Right Orb
    
    // UI states
    private enum UIState { MAIN, SCROLL, SCROLL_REGISTER, SCROLL_VIEW, LEFT_ORB, RIGHT_ORB, HEAL_MINIGAME, ID_POPUP }
    private UIState currentState = UIState.MAIN;
    
    private boolean rightButtonBlinking = false;
    private float blinkTimer = 0f;

    // Coordenadas base
    private float tableX = 0, tableY = 0;
    private float orbOneX = 400, orbOneY = 40;     // Orbe Esquerdo
    private float orbTwoX = 148, orbTwoY = 125;    // Orbe Direito
    private float scrollX = 88, scrollY = 25;      // Pergaminho
    private float inkwellX = 186, inkwellY = 82;
    private float greenBtnX = 390, greenBtnY = 15; // Botão Esquerdo (Cura)
    private float redBtnX = 430, redBtnY = 15;     // Botão Direito (Retirar)

    public GameScreen(DeskGoblinGame game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 360, camera);

        groundTexture = new Texture("ground.png");
        tableTexture = new Texture("table.png");
        orbOneTexture = new Texture("orb_one.png");
        orbTwoTexture = new Texture("orb_two.png");
        scrollTexture = new Texture("scroll.png");
        inkwellTexture = new Texture("inkwell.png");
        greenButtonTexture = new Texture("green_button.png");
        redButtonTexture = new Texture("red_button.png");
        handTexture = new Texture("hand.png");
        patientTexture = new Texture("patient.png");
        
        mousePos = new Vector3();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("VT323-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.WHITE;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        font = generator.generateFont(parameter);
        generator.dispose();

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
        
        hospitalManager = new HospitalManager();
        
        // Simular pacientes já na fila e cadastrados para testes de gameplay
        Patient p1 = new Patient("P001", "Bob", "Tosse", 2);
        Patient p2 = new Patient("P002", "Garn", "Febre", 1);
        Patient p3 = new Patient("E001", "Rei", "Escamas", 99);
        hospitalManager.registerPatient(p1);
        hospitalManager.registerPatient(p2);
        hospitalManager.registerPatient(p3);
    }

    @Override
    public void render(float delta) {
        // Atualizar processos médicos (S17 -> X segundos -> piscar botão)
        if (hospitalManager.updateMedicalProcesses(delta)) {
            rightButtonBlinking = true;
        }

        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(groundTexture, 0, 0, 640, 360);
        
        // 1.5 Draw Patient (behind the table)
        Patient visiblePatient = null;
        if (currentState == UIState.ID_POPUP) {
            visiblePatient = currentPatient;
        } else if (currentState == UIState.LEFT_ORB) {
            visiblePatient = hospitalManager.peekNextPatient();
        }
        
        if (visiblePatient != null) {
            float patientX = (640 - patientTexture.getWidth()) / 2f;
            batch.draw(patientTexture, patientX, 90);
        }
        
        if (tableX == 0) tableX = (640 - tableTexture.getWidth()) / 2f;
        batch.draw(tableTexture, tableX, tableY);
        
        batch.draw(orbOneTexture, orbOneX, orbOneY);
        batch.draw(orbTwoTexture, orbTwoX, orbTwoY);
        batch.draw(scrollTexture, scrollX, scrollY);
        batch.draw(inkwellTexture, inkwellX, inkwellY);
        batch.draw(greenButtonTexture, greenBtnX, greenBtnY);
        
        // Efeito de piscar no botão direito
        if (rightButtonBlinking) {
            blinkTimer += delta;
            if (blinkTimer % 0.5f < 0.25f) {
                batch.setColor(Color.RED);
            }
        }
        batch.draw(redButtonTexture, redBtnX, redBtnY);
        batch.setColor(Color.WHITE); // Reset color
        
        // Desenhar UI OVerlays baseados no estado
        drawOverlays(delta);

        // Draw Mouse
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);
        batch.draw(handTexture, mousePos.x, mousePos.y - handTexture.getHeight());
        
        batch.end();
        
        handleInput();
    }

    private void drawOverlays(float delta) {
        // Fundos semi-transparentes para overlays
        if (currentState != UIState.MAIN) {
            font.draw(batch, "[ ESC para fechar tela atual ]", 10, 350);
        }

        switch (currentState) {
            case SCROLL:
                font.draw(batch, "--- PERGAMINHO (AVL Tree) ---", 180, 320);
                font.draw(batch, "1. Cadastrar Paciente", 180, 290);
                font.draw(batch, "2. Ver Pacientes", 180, 260);
                break;
            case SCROLL_REGISTER:
                font.draw(batch, "Simulando Cadastro (S10) -> Salvo na BST, enviado pro MinHeap", 10, 320);
                font.draw(batch, "Pressione ENTER para cadastrar um paciente teste.", 10, 290);
                break;
            case SCROLL_VIEW:
                font.draw(batch, "Visualizando Pacientes (Pesquisa na BST)", 150, 320);
                font.draw(batch, "Pressione ID '1' para pesquisar P001", 150, 290);
                break;
            case LEFT_ORB:
                font.draw(batch, "--- ORBE ESQUERDO (Min Heap) ---", 180, 320);
                Patient next = hospitalManager.peekNextPatient();
                if (next != null) {
                    font.draw(batch, "Próximo Paciente: " + next.getName() + " (Sev: " + next.getSeverityScore() + ")", 150, 290);
                    font.draw(batch, "Pressione 'M' para associar à Maca 1 e começar tratamento.", 60, 260);
                } else {
                    font.draw(batch, "Fila vazia.", 250, 290);
                }
                break;
            case RIGHT_ORB:
                font.draw(batch, "--- ORBE DIREITO (Hash Table das Macas) ---", 100, 320);
                Bed b = hospitalManager.getBed(selectedBedId);
                if (b != null) {
                    font.draw(batch, b.toString(), 100, 290);
                }
                font.draw(batch, "Use os botões na tela principal para gerenciar.", 100, 260);
                break;
            case HEAL_MINIGAME:
                font.draw(batch, "--- MINI-GAME DE CURA ---", 200, 320);
                font.draw(batch, "Work In Progress...", 220, 290);
                break;
            case ID_POPUP:
                font.draw(batch, "--- DADOS DO PACIENTE ---", 200, 320);
                if (currentPatient != null) {
                    font.draw(batch, currentPatient.toString(), 150, 290);
                }
                break;
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (currentState == UIState.MAIN) {
                // S4 -> Pausar o jogo
                game.setScreen(new OptionsScreen(game, this));
            } else {
                currentState = UIState.MAIN;
            }
            return;
        }

        if (currentState == UIState.MAIN) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                Rectangle orbOneBounds = new Rectangle(orbOneX, orbOneY, orbOneTexture.getWidth(), orbOneTexture.getHeight());
                Rectangle orbTwoBounds = new Rectangle(orbTwoX, orbTwoY, orbTwoTexture.getWidth(), orbTwoTexture.getHeight());
                Rectangle scrollBounds = new Rectangle(scrollX, scrollY, scrollTexture.getWidth(), scrollTexture.getHeight());
                Rectangle greenBtnBounds = new Rectangle(greenBtnX, greenBtnY, greenButtonTexture.getWidth(), greenButtonTexture.getHeight());
                Rectangle redBtnBounds = new Rectangle(redBtnX, redBtnY, redButtonTexture.getWidth(), redButtonTexture.getHeight());
                
                // Clicar no ID do Paciente (Simulando clique no personagem)
                Rectangle patientBounds = new Rectangle((640 - patientTexture.getWidth()) / 2f, 90, patientTexture.getWidth(), patientTexture.getHeight());
                if (patientBounds.contains(mousePos.x, mousePos.y)) {
                    currentState = UIState.ID_POPUP;
                    currentPatient = hospitalManager.peekNextPatient();
                    return;
                }

                if (scrollBounds.contains(mousePos.x, mousePos.y)) {
                    currentState = UIState.SCROLL;
                } else if (orbOneBounds.contains(mousePos.x, mousePos.y)) {
                    // CORRIGIDO: Orb da direita (visual) abre o ORBE DIREITO (HashTable)
                    currentState = UIState.RIGHT_ORB;
                } else if (orbTwoBounds.contains(mousePos.x, mousePos.y)) {
                    // CORRIGIDO: Orb da esquerda (visual) abre o ORBE ESQUERDO (MinHeap)
                    currentState = UIState.LEFT_ORB;
                } else if (greenBtnBounds.contains(mousePos.x, mousePos.y)) {
                    currentState = UIState.HEAL_MINIGAME;
                } else if (redBtnBounds.contains(mousePos.x, mousePos.y)) {
                    // S15 -> Retirar o paciente da maca selecionada
                    if (hospitalManager.removePatientFromBed("M1")) {
                        rightButtonBlinking = false;
                    }
                }
            }
        } else if (currentState == UIState.SCROLL) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) currentState = UIState.SCROLL_REGISTER;
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) currentState = UIState.SCROLL_VIEW;
        } else if (currentState == UIState.SCROLL_REGISTER) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                Patient p = new Patient("P999", "Novo_Paciente", "Gripe", 5);
                hospitalManager.registerPatient(p);
                currentState = UIState.SCROLL;
            }
        } else if (currentState == UIState.SCROLL_VIEW) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                Patient p = hospitalManager.getPatientRecord("P001");
                // Pesquisa concluída (a mensagem foi removida, o log só aparece no terminal agora se você colocar System.out)
            }
        } else if (currentState == UIState.LEFT_ORB) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                Patient p = hospitalManager.popNextPatient();
                if (p != null) {
                    hospitalManager.assignPatientToBed(p, "M1", 3.0f); // Demora 3 segundos o tratamento
                    currentState = UIState.MAIN;
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        groundTexture.dispose();
        tableTexture.dispose();
        orbOneTexture.dispose();
        orbTwoTexture.dispose();
        scrollTexture.dispose();
        inkwellTexture.dispose();
        greenButtonTexture.dispose();
        redButtonTexture.dispose();
        handTexture.dispose();
        patientTexture.dispose();
        font.dispose();
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }
}