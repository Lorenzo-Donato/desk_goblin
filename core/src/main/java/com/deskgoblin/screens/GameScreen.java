package com.deskgoblin.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture groundTexture;
    private Texture tableTexture;
    private Texture orbOneTexture;
    private Texture orbTwoTexture;
    private Texture scrollTexture;
    private Texture inkwellTexture;
    private Texture greenButtonTexture;
    private Texture redButtonTexture;
    private Texture handTexture;
    private Texture patientTexture;
    
    private BitmapFont font;
    private BitmapFont techFont;
    private String uiLogText = "";
    private String uiTechText = "";
    private float logTimer = 0f;
    
    private Vector3 mousePos;
    
    private com.deskgoblin.model.HospitalManager hospitalManager;
    private com.deskgoblin.model.entities.Patient currentPatient;

    // Coordenadas base
    private float tableX = 0;
    private float tableY = 0;
    
    // Orbe Verde (orb_one.png) - Na parte direita da mesa
    private float orbOneX = 400;
    private float orbOneY = 40;
    
    // Orbe Rosa/Avermelhado (orb_two.png) - Colado na parte de cima do balcão à esquerda
    // Se a mesa tem 138px de altura, Y=125 coloca o objeto um pouco mais para baixo
    private float orbTwoX = 148;
    private float orbTwoY = 125;
    
    // Scroll na esquerda da mesa
    private float scrollX = 88;
    private float scrollY = 25;
    
    // Inkwell ao lado direito superior do scroll
    private float inkwellX = 186;
    private float inkwellY = 82;
    
    // Botões de Ação
    private float greenBtnX = 390;
    private float greenBtnY = 15;
    private float redBtnX = 430;
    private float redBtnY = 15;

    public GameScreen() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        // Set the internal resolution to 640x360 for pixel art scaling
        viewport = new FitViewport(640, 360, camera);

        // Load individual elements
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

        // Configurar a fonte Pixel Art usando FreeType
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("VT323-Regular.ttf"));
        
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 24; // Tamanho ideal para pixel art legível
        parameter.color = Color.WHITE;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        font = generator.generateFont(parameter);
        
        FreeTypeFontParameter techParameter = new FreeTypeFontParameter();
        techParameter.size = 18; // Menor para a legenda
        techParameter.color = Color.YELLOW; // Amarelo para destacar que é metadado do projeto
        techParameter.shadowColor = Color.BLACK;
        techParameter.shadowOffsetX = 1;
        techParameter.shadowOffsetY = 1;
        techFont = generator.generateFont(techParameter);
        
        generator.dispose();

        // Hide the default OS cursor
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
        
        // --- INICIALIZAÇÃO DA LÓGICA DO JOGO ---
        hospitalManager = new com.deskgoblin.model.HospitalManager();
        hospitalManager.addPatient(new com.deskgoblin.model.entities.Patient("P001", "Bob o Fazendeiro", "Tosse com Faíscas", 2));
        hospitalManager.addPatient(new com.deskgoblin.model.entities.Patient("P002", "Garn, o Orc", "Ossos de Gelatina", 1));
        
        // Adicionamos um caso de emergência (Severidade Alta)
        hospitalManager.addEmergencyPatient(new com.deskgoblin.model.entities.Patient("E001", "Rei Elfo", "Pele Escamosa Verde", 99));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1f);
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        // 1. Draw Ground (Background)
        batch.draw(groundTexture, 0, 0, 640, 360);
        
        // 1.5 Draw Patient (behind the table)
        if (currentPatient != null) {
            float patientX = (640 - patientTexture.getWidth()) / 2f;
            batch.draw(patientTexture, patientX, 90);
        }
        
        // 2. Draw Table
        if(tableX == 0) {
            tableX = (640 - tableTexture.getWidth()) / 2f;
        }
        batch.draw(tableTexture, tableX, tableY);
        
        // 3. Draw Interactable Items
        batch.draw(orbOneTexture, orbOneX, orbOneY);
        batch.draw(orbTwoTexture, orbTwoX, orbTwoY);
        batch.draw(scrollTexture, scrollX, scrollY);
        batch.draw(inkwellTexture, inkwellX, inkwellY);
        batch.draw(greenButtonTexture, greenBtnX, greenBtnY);
        batch.draw(redButtonTexture, redBtnX, redBtnY);
        
        // 4. Draw Custom Cursor (Hand) on top of everything
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);
        batch.draw(handTexture, mousePos.x, mousePos.y - handTexture.getHeight());
        
        // 5. Draw UI Log Text
        if (logTimer > 0) {
            logTimer -= delta;
            font.draw(batch, uiLogText, 20, 340);
            techFont.draw(batch, uiTechText, 20, 315);
        }
        
        batch.end();
        
        // --- DETECÇÃO DE CLIQUES E INTEGRAÇÃO LÓGICA ---
        if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
            // Recria as hitboxes toda frame pra garantir que acompanhem as variáveis caso a gente as mova no futuro
            com.badlogic.gdx.math.Rectangle orbOneBounds = new com.badlogic.gdx.math.Rectangle(orbOneX, orbOneY, orbOneTexture.getWidth(), orbOneTexture.getHeight());
            com.badlogic.gdx.math.Rectangle orbTwoBounds = new com.badlogic.gdx.math.Rectangle(orbTwoX, orbTwoY, orbTwoTexture.getWidth(), orbTwoTexture.getHeight());
            com.badlogic.gdx.math.Rectangle scrollBounds = new com.badlogic.gdx.math.Rectangle(scrollX, scrollY, scrollTexture.getWidth(), scrollTexture.getHeight());
            com.badlogic.gdx.math.Rectangle inkwellBounds = new com.badlogic.gdx.math.Rectangle(inkwellX, inkwellY, inkwellTexture.getWidth(), inkwellTexture.getHeight());
            com.badlogic.gdx.math.Rectangle greenBtnBounds = new com.badlogic.gdx.math.Rectangle(greenBtnX, greenBtnY, greenButtonTexture.getWidth(), greenButtonTexture.getHeight());
            com.badlogic.gdx.math.Rectangle redBtnBounds = new com.badlogic.gdx.math.Rectangle(redBtnX, redBtnY, redButtonTexture.getWidth(), redButtonTexture.getHeight());

            if (orbOneBounds.contains(mousePos.x, mousePos.y)) {
                // Orbe Verde: Chama o próximo paciente (MaxHeap/Fila)
                if (currentPatient == null) {
                    currentPatient = hospitalManager.getNextPatient();
                    if (currentPatient != null) {
                        showLog("[Orbe Verde] Novo Paciente na Mesa: " + currentPatient.getName(), 
                                "Estrutura: extractMax() O(log n) do MaxHeap ou dequeue() O(1) da Fila");
                    } else {
                        showLog("[Orbe Verde] Nenhum paciente aguardando lá fora.", "Fila e Heap vazios");
                    }
                } else {
                    showLog("[Orbe Verde] Já há um paciente sendo atendido!", "Validação de fluxo de UI");
                }
            } else if (orbTwoBounds.contains(mousePos.x, mousePos.y)) {
                // Orbe Rosa: Diagnostica sintoma do paciente atual (AVL Tree)
                if (currentPatient != null) {
                    com.deskgoblin.model.entities.Disease d = hospitalManager.diagnose(currentPatient.getSymptom());
                    if (d != null) {
                        showLog("Diagnóstico: " + d.getName() + " -> Dar " + d.getCure(), 
                                "Estrutura: Busca O(log n) na Árvore AVL pelo sintoma '" + currentPatient.getSymptom() + "'");
                    } else {
                        showLog("Doença desconhecida para o sintoma: " + currentPatient.getSymptom(), "Busca na AVL Tree falhou (Null)");
                    }
                } else {
                    showLog("[Orbe Rosa] Chame um paciente primeiro!", "Validação de fluxo de UI");
                }
            } else if (scrollBounds.contains(mousePos.x, mousePos.y)) {
                showLog("[Click] Pergaminho tocado!", "");
            } else if (inkwellBounds.contains(mousePos.x, mousePos.y)) {
                showLog("[Click] Tinteiro tocado!", "");
            } else if (greenBtnBounds.contains(mousePos.x, mousePos.y)) {
                // Botão Verde: Internar paciente (Gravar na Hash Table)
                if (currentPatient != null) {
                    hospitalManager.recordDecision(currentPatient, true);
                    showLog("[Botão Verde] Você INTERNOU " + currentPatient.getName(), 
                            "Estrutura: put(ID) O(1) na HashTable (Usando Chaining com LinkedList)");
                    currentPatient = null;
                } else {
                    showLog("[Botão Verde] Nenhum paciente para internar.", "Validação de fluxo de UI");
                }
            } else if (redBtnBounds.contains(mousePos.x, mousePos.y)) {
                // Botão Vermelho: Expulsar paciente (Gravar na Hash Table)
                if (currentPatient != null) {
                    hospitalManager.recordDecision(currentPatient, false);
                    showLog("[Botão Vermelho] Você EXPULSOU " + currentPatient.getName(), 
                            "Estrutura: put(ID) O(1) na HashTable (Usando Chaining com LinkedList)");
                    currentPatient = null;
                } else {
                    showLog("[Botão Vermelho] Nenhum paciente para expulsar.", "Validação de fluxo de UI");
                }
            }
        }
    }

    private void showLog(String text, String techText) {
        this.uiLogText = text;
        this.uiTechText = techText;
        this.logTimer = 4.0f; // Mostrar por 4 segundos
        Gdx.app.log("UI", text + " | " + techText); 
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
        techFont.dispose();
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }
}