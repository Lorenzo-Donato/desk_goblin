package com.deskgoblin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import com.deskgoblin.model.datastructures.SinglyLinkedList;

import java.util.Random;

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
    private Patient currentPatient; 
    private Patient patientOnCounter; 
    private boolean isPatientWaiting; 
    private float spawnTimer; 

    private boolean idCardOnTable = false;
    private String selectedBedId = "M1"; 

    private String currentSpeechBubbleText = "";
    private Random random = new Random();
    private boolean isZoomPopupOpen = false; 

    // Variáveis da tela de cadastro
    private String inputName = "";
    private String inputId = "";
    private int inputSeverity = 0; 
    private String searchResult = ""; 
    
    // --- VARIÁVEIS PARA DIGITAÇÃO DIRETA ---
    private boolean isTypingName = false;
    private boolean isTypingId = false;
    private boolean isSearchingId = false; // Flag para buscar ID
    private String searchInputId = "";     // Input da busca
    private float cursorTimer = 0f;
    // ---------------------------------------
    
    private String[] patientNames = {"Ana Silva", "Carlos Souza", "Mariana Oliveira", "João Santos", "Pedro Costa", 
                                     "Larissa Fernandes", "Lucas Almeida", "Beatriz Lima", "Gabriel Rocha", "Fernanda Dias"};
    private int nextPatientId = 1;

    private enum UIState { MAIN, SCROLL, SCROLL_REGISTER, SCROLL_VIEW, LEFT_ORB, RIGHT_ORB, HEAL_MINIGAME, ID_ON_COUNTER }
    private UIState currentState = UIState.MAIN;
    
    private boolean rightButtonBlinking = false;
    private float blinkTimer = 0f;

    private float tableX = 0, tableY = 0;
    private float orbOneX = 400, orbOneY = 40;
    private float orbTwoX = 148, orbTwoY = 125;
    private float scrollX = 88, scrollY = 25;
    private float inkwellX = 186, inkwellY = 82;
    private float greenBtnX = 390, greenBtnY = 15;
    private float redBtnX = 430, redBtnY = 15;

    public GameScreen(DeskGoblinGame game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 360, camera);
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);

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
        parameter.size = 18;
        parameter.color = Color.WHITE;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        font = generator.generateFont(parameter);
        generator.dispose();

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
        
        hospitalManager = new HospitalManager();
        
        spawnTimer = 0.5f;
        isPatientWaiting = false;
        idCardOnTable = false;
    }

    @Override
    public void render(float delta) {
        // --- LÓGICA DE SPAWN ---
        if (!isPatientWaiting && !idCardOnTable && patientOnCounter == null) {
            spawnTimer -= delta;
            if (spawnTimer <= 0) {
                String name = patientNames[random.nextInt(patientNames.length)];
                String id = "P" + nextPatientId;
                nextPatientId++;
                int severity = (int)(Math.random() * 6) + 1;
                patientOnCounter = new Patient(id, name, "Sintoma " + (int)(Math.random() * 5), severity);
                isPatientWaiting = true;
            }
        }

        if (hospitalManager.updateMedicalProcesses(delta)) {
            rightButtonBlinking = true;
        }

        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1f);
        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(groundTexture, 0, 0, 640, 360);
        
        // Draw Patient
        Patient visiblePatient = null;
        if (isPatientWaiting) visiblePatient = patientOnCounter;
        else if (idCardOnTable || currentState == UIState.SCROLL || currentState == UIState.SCROLL_REGISTER || currentState == UIState.SCROLL_VIEW) {
            visiblePatient = currentPatient; 
        } else if (currentState == UIState.LEFT_ORB) {
            visiblePatient = hospitalManager.peekNextPatient();
        }
        
        if (visiblePatient != null) {
            float patientX = (640 - patientTexture.getWidth()) / 2f;
            float patientY = 90;
            batch.draw(patientTexture, patientX, patientY);

            // --- ! MAIOR (Ajuste de escala) ---
            if (isPatientWaiting && currentState == UIState.MAIN) {
                float origScaleX = font.getData().scaleX;
                float origScaleY = font.getData().scaleY;
                font.getData().setScale(2.0f); // Dobra o tamanho da fonte apenas para o "!"
                font.draw(batch, "!", patientX + patientTexture.getWidth()/2 - 10, patientY + patientTexture.getHeight() + 20);
                font.getData().setScale(origScaleX, origScaleY); // Volta ao tamanho normal
            }
            // ---------------------------
        }
        
        if (tableX == 0) tableX = (640 - tableTexture.getWidth()) / 2f;
        batch.draw(tableTexture, tableX, tableY);
        
        batch.draw(orbOneTexture, orbOneX, orbOneY);
        batch.draw(orbTwoTexture, orbTwoX, orbTwoY);
        batch.draw(scrollTexture, scrollX, scrollY);
        batch.draw(inkwellTexture, inkwellX, inkwellY);
        batch.draw(greenButtonTexture, greenBtnX, greenBtnY);
        
        if (rightButtonBlinking) {
            blinkTimer += delta;
            if (blinkTimer % 0.5f < 0.25f) batch.setColor(Color.RED);
        }
        batch.draw(redButtonTexture, redBtnX, redBtnY);
        batch.setColor(Color.WHITE);

        if (currentState == UIState.SCROLL_REGISTER || currentState == UIState.SCROLL_VIEW) {
            batch.setColor(0.3f, 0.3f, 0.3f, 0.9f);
            batch.draw(getWhitePixelTexture(), 0, 0, 640, 360);
            batch.setColor(Color.WHITE);
        }
        
        drawOverlays(delta);

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);
        batch.draw(handTexture, mousePos.x, mousePos.y - handTexture.getHeight());
        
        batch.end();
        
        handleInput();
        handleTypingInput();
    }

    private void drawOverlays(float delta) {
        // --- CORRIGIDO: Exclui o ID_ON_COUNTER para não mostrar o texto de ESC ---
        if (currentState != UIState.MAIN && currentState != UIState.ID_ON_COUNTER) {
            font.draw(batch, "[ ESC para fechar ]", 10, 350);
        }

        float patientX = (640 - patientTexture.getWidth()) / 2f;
        float patientY = 90;
        float idWidth = 200, idHeight = 50;
        float idX = patientX - 85; 
        float idY = patientY - 65;

        if (idCardOnTable && currentPatient != null) {
            if (currentState != UIState.SCROLL_REGISTER && currentState != UIState.SCROLL_VIEW) {
                String speech = currentSpeechBubbleText;
                float bubbleWidth = 200, bubbleHeight = 40;
                float bubbleX = patientX - 50;
                float bubbleY = patientY + patientTexture.getHeight() + 5;
                
                batch.end(); batch.begin();
                batch.setColor(Color.BLACK);
                batch.draw(getWhitePixelTexture(), bubbleX, bubbleY, bubbleWidth, bubbleHeight);
                batch.setColor(Color.WHITE);
                batch.end(); batch.begin();
                font.draw(batch, speech, bubbleX + 10, bubbleY + 25);
            }

            batch.end(); batch.begin();
            batch.setColor(Color.BLACK);
            batch.draw(getWhitePixelTexture(), idX, idY, idWidth, idHeight);
            batch.setColor(Color.WHITE);
            batch.end(); batch.begin();

            font.draw(batch, "Nome: " + currentPatient.getName(), idX + 10, idY + 40);
            font.draw(batch, "ID: " + currentPatient.getId(), idX + 10, idY + 15);
            batch.setColor(Color.WHITE);
        }

        if (isZoomPopupOpen && currentPatient != null) {
            float rectX = (640 - 300) / 2f;
            float rectY = 180;
            float rectWidth = 300, rectHeight = 80;
            
            batch.end(); batch.begin();
            batch.setColor(0, 0, 0, 0.85f); 
            batch.draw(getWhitePixelTexture(), rectX, rectY, rectWidth, rectHeight);
            
            batch.setColor(Color.WHITE);
            batch.end(); batch.begin();

            font.draw(batch, "Nome: " + currentPatient.getName(), rectX + 20, rectY + 60);
            font.draw(batch, "ID: " + currentPatient.getId(), rectX + 20, rectY + 30);
            batch.setColor(Color.WHITE);
        }

        GlyphLayout layout = new GlyphLayout();

        switch (currentState) {
            case SCROLL:
                font.draw(batch, "--- PERGAMINHO (AVL Tree) ---", 160, 320);
                font.draw(batch, "1. Cadastrar Paciente", 180, 290);
                font.draw(batch, "2. Ver Pacientes", 180, 260);
                break;
            case SCROLL_REGISTER:
                font.draw(batch, "--- CADASTRO ---", 230, 330);

                cursorTimer += delta;
                boolean cursorVisible = (int)(cursorTimer * 2) % 2 == 0;

                batch.setColor(Color.BLACK);
                batch.draw(getWhitePixelTexture(), 180, 280, 280, 30);
                batch.setColor(Color.WHITE); 
                font.draw(batch, "Nome: ", 185, 300);
                font.draw(batch, inputName, 240, 300);
                if (isTypingName && cursorVisible) {
                    layout.setText(font, inputName);
                    font.draw(batch, "_", 240 + layout.width, 300);
                }
                if (!isTypingName && inputName.isEmpty()) {
                    font.draw(batch, "[ Clique aqui ]", 240, 300);
                }

                batch.setColor(Color.BLACK);
                batch.draw(getWhitePixelTexture(), 180, 240, 280, 30);
                batch.setColor(Color.WHITE); 
                font.draw(batch, "ID: ", 185, 260);
                font.draw(batch, inputId, 225, 260);
                if (isTypingId && cursorVisible) {
                    layout.setText(font, inputId);
                    font.draw(batch, "_", 225 + layout.width, 260);
                }
                if (!isTypingId && inputId.isEmpty()) {
                    font.draw(batch, "[ Clique aqui ]", 225, 260);
                }

                font.draw(batch, "Gravidade:", 180, 215);
                for (int i = 0; i < 6; i++) {
                    int btnX = 180 + i * 32;
                    if (inputSeverity == i + 1) batch.setColor(Color.GREEN);
                    else batch.setColor(Color.BLACK);
                    batch.draw(getWhitePixelTexture(), btnX, 145, 28, 28);
                    
                    batch.setColor(Color.WHITE);
                    font.draw(batch, String.valueOf(i + 1), btnX + 8, 165);
                    batch.setColor(Color.WHITE); 
                }

                batch.setColor(Color.BLACK);
                batch.draw(getWhitePixelTexture(), 180, 90, 160, 30);
                batch.setColor(Color.WHITE); 
                font.draw(batch, "[ CONFIRMAR ]", 190, 112);
                batch.setColor(Color.WHITE); 
                break;
            case SCROLL_VIEW:
                font.draw(batch, "--- PACIENTES CADASTRADOS (AVL) ---", 100, 320);
                SinglyLinkedList<Patient> list = hospitalManager.getAllPatients();
                int y = 290;
                while (!list.isEmpty() && y > 50) {
                    Patient p = list.popFront();
                    font.draw(batch, p.getName() + " (ID: " + p.getId() + ", Sev: " + p.getSeverityScore() + ")", 100, y);
                    y -= 20;
                }
                
                font.draw(batch, "[ Buscar por ID ]", 100, 40);
                font.draw(batch, searchResult, 100, 20);

                if (isSearchingId) {
                    cursorTimer += delta;
                    boolean cursorSearchVisible = (int)(cursorTimer * 2) % 2 == 0;

                    batch.setColor(Color.BLACK);
                    batch.draw(getWhitePixelTexture(), 260, 25, 150, 20);
                    batch.setColor(Color.WHITE); 
                    font.draw(batch, searchInputId, 270, 40);
                    if (cursorSearchVisible) {
                        layout.setText(font, searchInputId);
                        font.draw(batch, "_", 270 + layout.width, 40);
                    }
                }
                break;
            case LEFT_ORB:
                font.draw(batch, "--- ORBE ESQUERDO (Min Heap) ---", 160, 320);
                Patient next = hospitalManager.peekNextPatient();
                if (next != null) {
                    font.draw(batch, "Próximo: " + next.getName() + " (Sev: " + next.getSeverityScore() + ")", 130, 290);
                    font.draw(batch, "Pressione 'M' para associar à Maca 1", 130, 260);
                } else {
                    font.draw(batch, "Fila vazia.", 250, 290);
                }
                break;
            case RIGHT_ORB:
                font.draw(batch, "--- ORBE DIREITO (Hash Table) ---", 140, 320);
                Bed b = hospitalManager.getBed(selectedBedId);
                if (b != null) font.draw(batch, b.toString(), 140, 290);
                font.draw(batch, "Use os botões na mesa para gerenciar.", 140, 260);
                break;
        }
    }

    private Texture whitePixel;
    private Texture getWhitePixelTexture() {
        if (whitePixel == null) {
            whitePixel = new Texture(1, 1, Pixmap.Format.RGBA8888);
            whitePixel.draw(new Pixmap(1, 1, Pixmap.Format.RGBA8888) {{
                setColor(1, 1, 1, 1);
                fill();
            }}, 0, 0);
        }
        return whitePixel;
    }

    private String getRandomPhrase(int severity) {
        String[][] phrases = {
            {"Estou gravemente ferido!", "Sinto que vou morrer daqui a pouco!", "Me ajude, por favor!", "O fim está próximo..."},
            {"Preciso de atendimento urgente.", "Estou passando muito mal.", "Algo está errado comigo.", "Não aguento a dor..."},
            {"Não estou me sentindo bem.", "Preciso de ajuda médica.", "Minha condição está piorando.", "Estou com muitas dores."},
            {"Estou com febre alta.", "Acho que vou desmaiar.", "Me sinto fraco.", "Estou com tontura."},
            {"Tenho uma dor de cabeça.", "Estou resfriado.", "Só um mal-estar.", "Estou espirrando muito."},
            {"Estou bem, só uma alergia.", "Não é nada grave.", "Posso esperar um pouco.", "Só estou com coceira."}
        };
        int index = Math.min(Math.max(severity - 1, 0), 5);
        String phrase = phrases[index][random.nextInt(phrases[index].length)];
        return phrase + " (" + severity + ")"; 
    }

    private void handleTypingInput() {
        if (!isTypingName && !isTypingId && !isSearchingId) return;

        java.util.function.Consumer<String> adder = (str) -> {
            if (isTypingName) { if (inputName.length() < 20) inputName += str; }
            else if (isTypingId) { if (inputId.length() < 20) inputId += str; }
            else if (isSearchingId) { if (searchInputId.length() < 20) searchInputId += str; }
        };

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (isSearchingId) {
                if (!searchInputId.isEmpty()) {
                    Patient p = hospitalManager.getPatientRecord(searchInputId);
                    if (p != null) searchResult = "Encontrado: " + p.getName() + " (" + p.getId() + ")";
                    else searchResult = "Nenhum paciente com este ID.";
                }
                isSearchingId = false;
                searchInputId = "";
                return;
            } else {
                isTypingName = false;
                isTypingId = false;
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if (isTypingName && inputName.length() > 0) inputName = inputName.substring(0, inputName.length() - 1);
            if (isTypingId && inputId.length() > 0) inputId = inputId.substring(0, inputId.length() - 1);
            if (isSearchingId && searchInputId.length() > 0) searchInputId = searchInputId.substring(0, searchInputId.length() - 1);
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            adder.accept(" ");
            return;
        }

        for (int i = Input.Keys.A; i <= Input.Keys.Z; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                char c = (char)('a' + (i - Input.Keys.A));
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                    c = (char)('A' + (i - Input.Keys.A));
                }
                adder.accept(String.valueOf(c));
                return;
            }
        }

        for (int i = Input.Keys.NUM_0; i <= Input.Keys.NUM_9; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                char c = (char)('0' + (i - Input.Keys.NUM_0));
                adder.accept(String.valueOf(c));
                return;
            }
        }

        for (int i = Input.Keys.NUMPAD_0; i <= Input.Keys.NUMPAD_9; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                char c = (char)('0' + (i - Input.Keys.NUMPAD_0));
                adder.accept(String.valueOf(c));
                return;
            }
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isTypingName = false;
            isTypingId = false;
            isSearchingId = false;
            if (currentState == UIState.MAIN) {
                game.setScreen(new OptionsScreen(game, this));
            } else {
                currentState = UIState.MAIN;
                searchResult = ""; 
                searchInputId = "";
            }
            return;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float patientX = (640 - patientTexture.getWidth()) / 2f;
            float patientY = 90;
            float idX = patientX - 85; 
            float idY = patientY - 65;
            float idWidth = 200, idHeight = 50;
            Rectangle idBounds = new Rectangle(idX, idY, idWidth, idHeight);
            
            float rectX = (640 - 300) / 2f;
            float rectY = 180;
            float rectWidth = 300, rectHeight = 80;
            Rectangle popupBounds = new Rectangle(rectX, rectY, rectWidth, rectHeight);

            // 1. Lógica do Pop-up
            if (isZoomPopupOpen) {
                if (popupBounds.contains(mousePos.x, mousePos.y)) return; 
                else isZoomPopupOpen = false;
            }

            // 2. Clicar no cartão para abrir pop-up
            if (!isZoomPopupOpen && idCardOnTable && currentPatient != null && idBounds.contains(mousePos.x, mousePos.y)) {
                isZoomPopupOpen = true;
                return;
            }

            // 3. INTERAÇÃO UNIFICADA DA MESA (MAIN ou ID_ON_COUNTER)
            boolean isOnTable = (currentState == UIState.MAIN || currentState == UIState.ID_ON_COUNTER);
            if (isOnTable) {
                Rectangle orbOneBounds = new Rectangle(orbOneX, orbOneY, orbOneTexture.getWidth(), orbOneTexture.getHeight());
                Rectangle orbTwoBounds = new Rectangle(orbTwoX, orbTwoY, orbTwoTexture.getWidth(), orbTwoTexture.getHeight());
                Rectangle scrollBounds = new Rectangle(scrollX, scrollY, scrollTexture.getWidth(), scrollTexture.getHeight());
                Rectangle greenBtnBounds = new Rectangle(greenBtnX, greenBtnY, greenButtonTexture.getWidth(), greenButtonTexture.getHeight());
                Rectangle redBtnBounds = new Rectangle(redBtnX, redBtnY, redButtonTexture.getWidth(), redButtonTexture.getHeight());
                Rectangle patientBounds = new Rectangle(patientX, patientY, patientTexture.getWidth(), patientTexture.getHeight());
                
                // 3.1 Clique no paciente (só se estiver no MAIN e com !)
                if (currentState == UIState.MAIN && isPatientWaiting && patientBounds.contains(mousePos.x, mousePos.y)) {
                    isPatientWaiting = false; 
                    currentPatient = patientOnCounter;
                    idCardOnTable = true;     
                    currentState = UIState.ID_ON_COUNTER;
                    currentSpeechBubbleText = getRandomPhrase(currentPatient.getSeverityScore());
                    return;
                }

                // 3.2 Clique em outros objetos da mesa
                UIState targetState = null;
                if (scrollBounds.contains(mousePos.x, mousePos.y)) targetState = UIState.SCROLL;
                else if (orbOneBounds.contains(mousePos.x, mousePos.y)) targetState = UIState.RIGHT_ORB;
                else if (orbTwoBounds.contains(mousePos.x, mousePos.y)) targetState = UIState.LEFT_ORB;
                else if (greenBtnBounds.contains(mousePos.x, mousePos.y)) targetState = UIState.HEAL_MINIGAME;
                else if (redBtnBounds.contains(mousePos.x, mousePos.y)) {
                    if (hospitalManager.removePatientFromBed("M1")) rightButtonBlinking = false;
                }

                if (targetState != null) {
                    currentState = targetState;
                    return;
                }
            }

            // 4. Fechar o ID_ON_COUNTER se clicar em um espaço vazio
            if (currentState == UIState.ID_ON_COUNTER) {
                if (!new Rectangle(patientX, patientY, patientTexture.getWidth(), patientTexture.getHeight()).contains(mousePos.x, mousePos.y) 
                    && !idBounds.contains(mousePos.x, mousePos.y)) {
                    currentState = UIState.MAIN; 
                }
            }

            // 5. Outros menus (SCROLL, SCROLL_REGISTER, SCROLL_VIEW)
            if (currentState == UIState.SCROLL) {
                if (mousePos.x >= 180 && mousePos.x <= 400) {
                    if (mousePos.y >= 270 && mousePos.y <= 290) {
                        currentState = UIState.SCROLL_REGISTER;
                        inputName = ""; inputId = ""; inputSeverity = 0;
                        isTypingName = false; isTypingId = false;
                    } else if (mousePos.y >= 240 && mousePos.y <= 260) {
                        currentState = UIState.SCROLL_VIEW;
                        searchResult = ""; searchInputId = ""; isSearchingId = false;
                    }
                }
            } else if (currentState == UIState.SCROLL_REGISTER) {
                if (mousePos.x >= 100 && mousePos.x <= 540 && mousePos.y >= 270 && mousePos.y <= 330) {
                    isTypingName = true;
                    isTypingId = false;
                    return;
                } else if (mousePos.x >= 100 && mousePos.x <= 540 && mousePos.y >= 230 && mousePos.y <= 290) {
                    isTypingId = true;
                    isTypingName = false;
                    return;
                }
                for (int i = 0; i < 6; i++) {
                    int btnX = 180 + i * 32;
                    if (mousePos.x >= btnX && mousePos.x <= btnX + 28 && mousePos.y >= 145 && mousePos.y <= 173) {
                        inputSeverity = i + 1;
                        return;
                    }
                }
                if (mousePos.x >= 180 && mousePos.x <= 340 && mousePos.y >= 90 && mousePos.y <= 120) {
                    if (!inputName.isEmpty() && !inputId.isEmpty() && inputSeverity > 0) {
                        Patient p = new Patient(inputId, inputName, "Diagnosticado", inputSeverity);
                        hospitalManager.registerPatient(p);
                        
                        isPatientWaiting = false;
                        idCardOnTable = false;
                        patientOnCounter = null;
                        currentPatient = null;
                        inputName = ""; inputId = ""; inputSeverity = 0;
                        spawnTimer = 5f; 
                        currentState = UIState.SCROLL;
                    }
                    return;
                }
            } else if (currentState == UIState.SCROLL_VIEW) {
                if (isSearchingId) {
                    if (!(mousePos.x >= 260 && mousePos.x <= 410 && mousePos.y >= 25 && mousePos.y <= 45)) {
                        isSearchingId = false;
                        searchInputId = "";
                    }
                } else if (mousePos.x >= 100 && mousePos.x <= 250 && mousePos.y >= 25 && mousePos.y <= 40) {
                    isSearchingId = true;
                    searchInputId = "";
                    searchResult = "";
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
        groundTexture.dispose(); tableTexture.dispose(); orbOneTexture.dispose(); orbTwoTexture.dispose();
        scrollTexture.dispose(); inkwellTexture.dispose(); greenButtonTexture.dispose(); redButtonTexture.dispose();
        handTexture.dispose(); patientTexture.dispose();
        if (whitePixel != null) whitePixel.dispose();
        font.dispose();
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }
}