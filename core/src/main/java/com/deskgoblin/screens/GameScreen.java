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
import com.deskgoblin.model.entities.MedicalProcess;
import com.deskgoblin.model.datastructures.SinglyLinkedList;

import java.util.Random;

public class GameScreen extends ScreenAdapter {
    private DeskGoblinGame game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture groundTexture, tableTexture, orbOneTexture, orbTwoTexture, scrollTexture, 
                    inkwellTexture, handTexture, patientTexture, speechBubbleTexture, idCardTexture;
    private Texture bgTableTex, vacantBedTex, occupiedBedTex, patientQueueBgTex, patientQueueTex, redOrbTex;
    private Texture brownBgTex, bigScrollTex;
    private boolean showSpeechBubble = true;
    
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

    private String inputName = "";
    private String inputId = "";
    private int inputSeverity = 0; 
    private String searchResult = ""; 

    private String inputBedId = "";
    private boolean isTypingBed = false;
    private String bedAssignmentResult = "";
    private SinglyLinkedList<Patient> leftOrbSnapshot;

    private String rightInputBedId = "M1";
    private boolean isTypingRightBed = false;
    
    private boolean isTypingName = false;
    private boolean isTypingId = false;
    private boolean isSearchingId = false; 
    private String searchInputId = "";     
    private float cursorTimer = 0f;
    
    private String[] patientNames = {"Ana Silva", "Carlos Souza", "Mariana Oliveira", "João Santos", "Pedro Costa", 
                                     "Larissa Fernandes", "Lucas Almeida", "Beatriz Lima", "Gabriel Rocha", "Fernanda Dias"};
    private int nextPatientId = 1;

    private enum UIState { MAIN, SCROLL, SCROLL_REGISTER, SCROLL_VIEW, LEFT_ORB, RIGHT_ORB, HEAL_MINIGAME, ID_ON_COUNTER }
    private UIState currentState = UIState.MAIN;
    
    private float tableX = 0, tableY = 0;
    private float orbOneX = 400, orbOneY = 40;
    private float orbTwoX = 148, orbTwoY = 125;
    private float scrollX = 88, scrollY = 25;
    private float inkwellX = 186, inkwellY = 82;

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
        handTexture = new Texture("hand.png");
        patientTexture = new Texture("patient.png");
        speechBubbleTexture = new Texture("speechBalloon.png");
        idCardTexture = new Texture("id.png");
        bgTableTex = new Texture("bg_table.png");
        vacantBedTex = new Texture("vacant_bed.png");
        occupiedBedTex = new Texture("occupied_bed.png");
        patientQueueBgTex = new Texture("patient_queue_bg.png");
        patientQueueTex = new Texture("patient_queue.png");
        redOrbTex = new Texture("red_orb.png");
        brownBgTex = new Texture("brown_bg.png");
        bigScrollTex = new Texture("big_scroll.png");
        
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

        // --- TESTES ---
        hospitalManager.registerPatient(new Patient("1", "A", "Teste", 1));
        hospitalManager.registerPatient(new Patient("2", "B", "Teste", 1));
        hospitalManager.registerPatient(new Patient("3", "C", "Teste", 2));
        hospitalManager.registerPatient(new Patient("4", "D", "Teste", 3));
        hospitalManager.registerPatient(new Patient("5", "E", "Teste", 4));
        hospitalManager.registerPatient(new Patient("6", "F", "Teste", 5));
        hospitalManager.registerPatient(new Patient("7", "G", "Teste", 6));
        // --------------
        
        leftOrbSnapshot = hospitalManager.getPatientQueueSnapshot();
        
        spawnTimer = 0.5f;
        isPatientWaiting = false;
        idCardOnTable = false;
    }

    @Override
    public void render(float delta) {
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

        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1f);
        viewport.apply();
        camera.update();
        
        hospitalManager.updateMedicalProcesses(delta);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(groundTexture, 0, 0, 640, 360);
        
        Patient visiblePatient = null;
        if (isPatientWaiting) visiblePatient = patientOnCounter;
        else if (idCardOnTable || currentState == UIState.SCROLL || currentState == UIState.SCROLL_REGISTER || currentState == UIState.SCROLL_VIEW || currentState == UIState.RIGHT_ORB) {
            visiblePatient = currentPatient; 
        } else if (currentState == UIState.LEFT_ORB) {
            visiblePatient = hospitalManager.peekNextPatient();
        }
        
        if (visiblePatient != null) {
            float patientX = (640 - patientTexture.getWidth()) / 2f;
            float patientY = 90;
            batch.draw(patientTexture, patientX, patientY);

            if (isPatientWaiting && currentState == UIState.MAIN) {
                float origScaleX = font.getData().scaleX;
                float origScaleY = font.getData().scaleY;
                font.getData().setScale(2.0f);
                font.draw(batch, "!", patientX + patientTexture.getWidth()/2 - 10, patientY + patientTexture.getHeight() + 20);
                font.getData().setScale(origScaleX, origScaleY);
            }
        }
        
        if (tableX == 0) tableX = (640 - tableTexture.getWidth()) / 2f;
        batch.draw(tableTexture, tableX, tableY);
        
        batch.draw(orbOneTexture, orbOneX, orbOneY);
        batch.draw(orbTwoTexture, orbTwoX, orbTwoY);
        batch.draw(scrollTexture, scrollX, scrollY);
        batch.draw(inkwellTexture, inkwellX, inkwellY);

        // --- FUNDO SCROLL ---
        if (currentState == UIState.SCROLL || currentState == UIState.SCROLL_REGISTER || currentState == UIState.SCROLL_VIEW) {
            batch.draw(brownBgTex, 0, 0, 640, 360);
            float scrollBgX = (640 - 465) / 2f;
            batch.draw(bigScrollTex, scrollBgX, 0, 465, 360);
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
        if (currentState != UIState.MAIN && currentState != UIState.ID_ON_COUNTER) {
            font.draw(batch, "[ ESC para fechar ]", 10, 350);
        }

        float patientX = (640 - patientTexture.getWidth()) / 2f;
        float patientY = 90;
        
        GlyphLayout layout = new GlyphLayout();
        float origScaleX, origScaleY;

        // 1. BALÃO DE FALA e CARTÃO DE ID
        if (idCardOnTable && currentPatient != null && (currentState == UIState.MAIN || currentState == UIState.ID_ON_COUNTER)) {
            if (showSpeechBubble) {
                String speech = currentSpeechBubbleText;
                
                float bubbleWidth = 280, bubbleHeight = 45;
                float bubbleX = patientX + 40; 
                float bubbleY = patientY + patientTexture.getHeight() + 5;
                
                batch.end(); batch.begin();
                batch.setColor(Color.WHITE);
                batch.draw(speechBubbleTexture, bubbleX, bubbleY, bubbleWidth, bubbleHeight);
                batch.end(); batch.begin();

                // Centraliza o texto
                layout.setText(font, speech);
                font.draw(batch, speech, bubbleX + (bubbleWidth - layout.width) / 2f, bubbleY + 30);
            }

            // 2. CARTÃO DE ID (Voltando ao tamanho original de 200x50 e layout igual ao seu esboço)
            float idWidth = 200, idHeight = 50;
            float idX = patientX - 85; 
            float idY = patientY - 65;

            batch.end(); batch.begin();
            batch.setColor(Color.WHITE);
            batch.draw(idCardTexture, idX, idY, idWidth, idHeight);
            batch.end(); batch.begin();

            // Fonte ligeiramente menor para caber direitinho
            origScaleX = font.getData().scaleX;
            origScaleY = font.getData().scaleY;
            font.getData().setScale(0.85f);

            // ID no topo direito (ao lado do rosto)
            font.draw(batch, "ID: " + currentPatient.getId(), idX + 70, idY + 38);

            // NOME embaixo, mais à esquerda (abaixo da foto do personagem)
            font.draw(batch, "Nome: " + currentPatient.getName(), idX + 10, idY + 18);

            font.getData().setScale(origScaleX, origScaleY);
        }

        // 3. ZOOM POP-UP (Voltando ao tamanho original de 300x80, layout corrigido)
        if (isZoomPopupOpen && currentPatient != null && (currentState == UIState.MAIN || currentState == UIState.ID_ON_COUNTER)) {
            float rectX = (640 - 300) / 2f;
            float rectY = 180;
            float rectWidth = 300, rectHeight = 80;
            
            batch.end(); batch.begin();
            batch.setColor(Color.WHITE);
            batch.draw(idCardTexture, rectX, rectY, rectWidth, rectHeight);
            batch.end(); batch.begin();

            origScaleX = font.getData().scaleX;
            origScaleY = font.getData().scaleY;
            font.getData().setScale(0.9f); // Zoom levemente maior

            // ID no topo direito do pop-up
            font.draw(batch, "ID: " + currentPatient.getId(), rectX + 105, rectY + 62);
            // NOME embaixo à esquerda do pop-up
            font.draw(batch, "Nome: " + currentPatient.getName(), rectX + 10, rectY + 35);

            font.getData().setScale(origScaleX, origScaleY);
        }

        // ------------------------------------------------
        // Telas de Menu (Pergaminho, Orbes, etc.)
        // ------------------------------------------------

        switch (currentState) {
            case SCROLL:
                font.draw(batch, "--- PERGAMINHO (AVL Tree) ---", 200, 320);
                font.draw(batch, "1. Cadastrar Paciente", 220, 290);
                font.draw(batch, "2. Ver Pacientes", 220, 260);
                break;
            case SCROLL_REGISTER:
                font.draw(batch, "--- CADASTRO ---", 270, 330);

                cursorTimer += delta;
                boolean cursorVisible = (int)(cursorTimer * 2) % 2 == 0;

                batch.setColor(Color.BLACK);
                batch.draw(getWhitePixelTexture(), 220, 280, 280, 30);
                batch.setColor(Color.WHITE); 
                font.draw(batch, "Nome: ", 225, 300);
                font.draw(batch, inputName, 280, 300);
                if (isTypingName && cursorVisible) {
                    layout.setText(font, inputName);
                    font.draw(batch, "_", 280 + layout.width, 300);
                }
                if (!isTypingName && inputName.isEmpty()) {
                    font.draw(batch, "[ Clique aqui ]", 280, 300);
                }

                batch.setColor(Color.BLACK);
                batch.draw(getWhitePixelTexture(), 220, 240, 280, 30);
                batch.setColor(Color.WHITE); 
                font.draw(batch, "ID: ", 225, 260);
                font.draw(batch, inputId, 265, 260);
                if (isTypingId && cursorVisible) {
                    layout.setText(font, inputId);
                    font.draw(batch, "_", 265 + layout.width, 260);
                }
                if (!isTypingId && inputId.isEmpty()) {
                    font.draw(batch, "[ Clique aqui ]", 265, 260);
                }

                font.draw(batch, "Gravidade:", 220, 215);
                for (int i = 0; i < 6; i++) {
                    int btnX = 220 + i * 32;
                    if (inputSeverity == i + 1) batch.setColor(Color.GREEN);
                    else batch.setColor(Color.BLACK);
                    batch.draw(getWhitePixelTexture(), btnX, 145, 28, 28);
                    batch.setColor(Color.WHITE);
                    font.draw(batch, String.valueOf(i + 1), btnX + 8, 165);
                    batch.setColor(Color.WHITE); 
                }

                batch.setColor(Color.BLACK);
                batch.draw(getWhitePixelTexture(), 220, 90, 160, 30);
                batch.setColor(Color.WHITE); 
                font.draw(batch, "[ CONFIRMAR ]", 230, 112);
                batch.setColor(Color.WHITE); 
                break;
            case SCROLL_VIEW:
                font.draw(batch, "--- PACIENTES CADASTRADOS (AVL) ---", 170, 320);
                SinglyLinkedList<Patient> list = hospitalManager.getAllPatients();
                int y = 290;
                while (!list.isEmpty() && y > 50) {
                    Patient p = list.popFront();
                    font.draw(batch, p.getName() + " (ID: " + p.getId() + ", Sev: " + p.getSeverityScore() + ")", 170, y);
                    y -= 20;
                }
                font.draw(batch, "[ Buscar por ID ]", 170, 40);
                font.draw(batch, searchResult, 170, 20);
                if (isSearchingId) {
                    cursorTimer += delta;
                    boolean cursorSearchVisible = (int)(cursorTimer * 2) % 2 == 0;
                    batch.setColor(Color.BLACK);
                    batch.draw(getWhitePixelTexture(), 330, 25, 150, 20);
                    batch.setColor(Color.WHITE); 
                    font.draw(batch, searchInputId, 340, 40);
                    if (cursorSearchVisible) {
                        layout.setText(font, searchInputId);
                        font.draw(batch, "_", 340 + layout.width, 40);
                    }
                }
                break;
            
            case LEFT_ORB:
                batch.draw(patientQueueBgTex, 0, 0, 640, 360);
                float orbX = (640 - 360) / 2f;
                float orbY = (360 - 274) / 2f;
                batch.draw(redOrbTex, orbX, orbY, 360, 274);
                
                int queueSize = 0;
                SinglyLinkedList.Node<Patient> node = null;
                if (leftOrbSnapshot != null) {
                    node = leftOrbSnapshot.getHead();
                    SinglyLinkedList.Node<Patient> temp = node;
                    while(temp != null) { queueSize++; temp = temp.next; }
                }
                
                int displayCount = Math.min(queueSize, 5);
                float spacing = 10f;
                float totalWidth = displayCount * 33f + Math.max(0, displayCount - 1) * spacing;
                float startX = orbX + (360f - totalWidth) / 2f;
                float pY = orbY + (274f - 79f) / 2f;
                
                for(int i = 0; i < displayCount; i++) {
                    batch.draw(patientQueueTex, startX + i * (33f + spacing), pY, 33, 79);
                    if (node != null) {
                        font.draw(batch, "S:" + node.data.getSeverityScore(), startX + i * (33f + spacing) + 4, pY - 5);
                        node = node.next;
                    }
                }
                if (queueSize > 5) {
                    font.draw(batch, "...", startX + displayCount * (33f + spacing) + 10, pY + 40);
                }
                
                Patient nextPat = hospitalManager.peekNextPatient();
                font.draw(batch, "Proximo: " + (nextPat != null ? nextPat.getName() : "Nenhum"), 100, 70);
                font.draw(batch, "Digite a Maca (M1-M12):", 100, 45);
                
                batch.setColor(Color.BLACK);
                batch.draw(getWhitePixelTexture(), 270, 25, 100, 25);
                batch.setColor(Color.WHITE); 
                font.draw(batch, inputBedId, 280, 43);
                
                boolean cursorBedVisible = (int)(cursorTimer * 2) % 2 == 0;
                if (isTypingBed && cursorBedVisible) {
                    layout.setText(font, inputBedId);
                    font.draw(batch, "_", 280 + layout.width, 43);
                }
                
                font.draw(batch, bedAssignmentResult, 400, 45);
                break;

            case RIGHT_ORB:
                float tableX = (640 - 592) / 2f;
                float tableY = (360 - 320) / 2f;
                batch.draw(bgTableTex, tableX, tableY, 592, 320);
                
                float bedX = tableX + 17;
                float bedY = tableY + (320 - 287) / 2f;

                Bed rightBed = hospitalManager.getBed(rightInputBedId.toUpperCase());
                boolean isOccupied = rightBed != null && rightBed.isOccupied();
                
                if (isOccupied) {
                    batch.draw(occupiedBedTex, bedX, bedY, 287, 287);
                } else {
                    batch.draw(vacantBedTex, bedX, bedY, 287, 287);
                }
                
                float rightPaneX = tableX + 320;
                
                font.draw(batch, "Maca:", rightPaneX, 315);
                
                batch.setColor(Color.BLACK);
                batch.draw(getWhitePixelTexture(), rightPaneX + 45, 297, 60, 22);
                batch.setColor(Color.WHITE); 
                font.draw(batch, rightInputBedId, rightPaneX + 50, 314);
                
                boolean cursorRightBedVisible = (int)(cursorTimer * 2) % 2 == 0;
                if (isTypingRightBed && cursorRightBedVisible) {
                    layout.setText(font, rightInputBedId);
                    font.draw(batch, "_", rightPaneX + 50 + layout.width, 314);
                }

                if (rightBed != null) {
                    if (isOccupied) {
                        Patient p = rightBed.getPatient();
                        font.draw(batch, "Paciente: " + p.getName(), rightPaneX, 275);
                        font.draw(batch, "Gravidade: " + p.getSeverityScore(), rightPaneX, 255);

                        MedicalProcess process = hospitalManager.getActiveProcessForBed(rightInputBedId.toUpperCase());
                        
                        int lineY = 225;
                        for (int i = 0; i < 6; i++) {
                            String name = process != null ? process.getStageName(i) : "Procedimento " + (i+1);
                            float timeLeft = process != null ? process.getStageTimer(i) : 0;
                            int currentIdx = process != null ? process.getCurrentStageIndex() : 0;
                            
                            String timeStr = String.format("%.1f", Math.max(0, timeLeft));
                            
                            if (process != null && i < currentIdx) {
                                batch.setColor(Color.GREEN);
                            } else if (process != null && i == currentIdx) {
                                batch.setColor(Color.WHITE);
                            } else {
                                batch.setColor(Color.LIGHT_GRAY);
                            }
                            font.draw(batch, (i+1) + "/6 - " + name + " (" + timeStr + "s)", rightPaneX, lineY);
                            batch.setColor(Color.WHITE);
                            lineY -= 20;
                        }
                        font.draw(batch, "Tempo restante: " + String.format("%.1f", process != null ? process.getTotalTimeRemaining() : 0) + "s", rightPaneX, lineY - 10);

                    } else {
                        font.draw(batch, "Status: LIVRE", rightPaneX, 275);
                        font.draw(batch, "(Sem paciente)", rightPaneX, 255);
                    }
                } else {
                    font.draw(batch, "Maca invalida!", rightPaneX, 275);
                }
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
        if (!isTypingName && !isTypingId && !isSearchingId && !isTypingBed && !isTypingRightBed) return;

        java.util.function.Consumer<String> adder = (str) -> {
            if (isTypingName) { if (inputName.length() < 20) inputName += str; }
            else if (isTypingId) { if (inputId.length() < 20) inputId += str; }
            else if (isSearchingId) { if (searchInputId.length() < 20) searchInputId += str; }
            else if (isTypingBed) { if (inputBedId.length() < 10) inputBedId += str; }
            else if (isTypingRightBed) { if (rightInputBedId.length() < 10) rightInputBedId += str; }
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
            } 
            else if (isTypingBed) {
                if (!inputBedId.isEmpty()) {
                    String bedIdToUse = inputBedId.toUpperCase();
                    Bed bed = hospitalManager.getBed(bedIdToUse);

                    if (bed == null) {
                        bedAssignmentResult = "Maca invalida!";
                    } else if (bed.isOccupied()) {
                        bedAssignmentResult = "a maca esta ocupada";
                    } else {
                        Patient p = hospitalManager.popNextPatient();
                        if (p == null) {
                            bedAssignmentResult = "Fila vazia!";
                        } else {
                            boolean success = hospitalManager.assignPatientToBed(p, bedIdToUse, 5.0f);
                            if (success) {
                                bedAssignmentResult = "Sucesso! Alocado na " + bedIdToUse;
                                leftOrbSnapshot = hospitalManager.getPatientQueueSnapshot();
                            } else {
                                hospitalManager.addPatientToQueue(p);
                                bedAssignmentResult = "Erro interno!";
                            }
                        }
                    }
                }
                isTypingBed = false;
                inputBedId = "";
                return;
            }
            else if (isTypingRightBed) {
                if (!rightInputBedId.isEmpty()) {
                    rightInputBedId = rightInputBedId.toUpperCase();
                }
                isTypingRightBed = false;
                return;
            }
            else {
                isTypingName = false;
                isTypingId = false;
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if (isTypingName && inputName.length() > 0) inputName = inputName.substring(0, inputName.length() - 1);
            else if (isTypingId && inputId.length() > 0) inputId = inputId.substring(0, inputId.length() - 1);
            else if (isSearchingId && searchInputId.length() > 0) searchInputId = searchInputId.substring(0, searchInputId.length() - 1);
            else if (isTypingBed && inputBedId.length() > 0) inputBedId = inputBedId.substring(0, inputBedId.length() - 1);
            else if (isTypingRightBed && rightInputBedId.length() > 0) rightInputBedId = rightInputBedId.substring(0, rightInputBedId.length() - 1);
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
            isTypingName = false; isTypingId = false; isSearchingId = false; isTypingBed = false; isTypingRightBed = false;
            if (currentState == UIState.MAIN) {
                game.setScreen(new OptionsScreen(game, this));
            } else {
                currentState = UIState.MAIN;
                searchResult = ""; searchInputId = ""; bedAssignmentResult = "";
            }
            return;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float patientX = (640 - patientTexture.getWidth()) / 2f;
            float patientY = 90;
            
            // --- NOVAS COORDENADAS DOS RETÂNGULOS DE CLIQUE ---
            float idWidth = 200, idHeight = 50;
            float idX = patientX - 85; 
            float idY = patientY - 65;
            Rectangle idBounds = new Rectangle(idX, idY, idWidth, idHeight);
            
            float rectX = (640 - 300) / 2f;
            float rectY = 180;
            float rectWidth = 300, rectHeight = 80;
            Rectangle popupBounds = new Rectangle(rectX, rectY, rectWidth, rectHeight);

            if (isZoomPopupOpen) {
                if (popupBounds.contains(mousePos.x, mousePos.y)) return; 
                else {
                    isZoomPopupOpen = false;
                    return;
                }
            }

            if (!isZoomPopupOpen && idCardOnTable && currentPatient != null && idBounds.contains(mousePos.x, mousePos.y)) {
                isZoomPopupOpen = true;
                return;
            }

            boolean isOnTable = (currentState == UIState.MAIN || currentState == UIState.ID_ON_COUNTER);
            if (isOnTable) {
                Rectangle orbOneBounds = new Rectangle(orbOneX, orbOneY, orbOneTexture.getWidth(), orbOneTexture.getHeight());
                Rectangle orbTwoBounds = new Rectangle(orbTwoX, orbTwoY, orbTwoTexture.getWidth(), orbTwoTexture.getHeight());
                Rectangle scrollBounds = new Rectangle(scrollX, scrollY, scrollTexture.getWidth(), scrollTexture.getHeight());
                Rectangle patientBounds = new Rectangle(patientX, patientY, patientTexture.getWidth(), patientTexture.getHeight());
                
                if (currentState == UIState.MAIN && isPatientWaiting && patientBounds.contains(mousePos.x, mousePos.y)) {
                    isPatientWaiting = false; 
                    currentPatient = patientOnCounter;
                    idCardOnTable = true;     
                    currentState = UIState.ID_ON_COUNTER;
                    currentSpeechBubbleText = getRandomPhrase(currentPatient.getSeverityScore());
                    showSpeechBubble = true;
                    return;
                }

                UIState targetState = null;
                if (scrollBounds.contains(mousePos.x, mousePos.y)) targetState = UIState.SCROLL;
                else if (orbOneBounds.contains(mousePos.x, mousePos.y)) {
                    targetState = UIState.RIGHT_ORB;
                    rightInputBedId = "M1";
                }
                else if (orbTwoBounds.contains(mousePos.x, mousePos.y)) {
                    targetState = UIState.LEFT_ORB;
                    leftOrbSnapshot = hospitalManager.getPatientQueueSnapshot();
                }

                if (targetState != null) {
                    currentState = targetState;
                    return;
                }
            }

            if (currentState == UIState.ID_ON_COUNTER) {
                float bubbleWidth = 280, bubbleHeight = 45;
                float bubbleX = patientX + 40; 
                float bubbleY = patientY + patientTexture.getHeight() + 5;
                Rectangle bubbleBounds = new Rectangle(bubbleX, bubbleY, bubbleWidth, bubbleHeight);
                Rectangle patientBounds = new Rectangle(patientX, patientY, patientTexture.getWidth(), patientTexture.getHeight());
                
                if (showSpeechBubble && bubbleBounds.contains(mousePos.x, mousePos.y)) {
                    showSpeechBubble = false;
                    return;
                }
                if (!showSpeechBubble && patientBounds.contains(mousePos.x, mousePos.y)) {
                    showSpeechBubble = true;
                    currentSpeechBubbleText = getRandomPhrase(currentPatient.getSeverityScore());
                    return;
                }

                if (!patientBounds.contains(mousePos.x, mousePos.y) 
                    && !idBounds.contains(mousePos.x, mousePos.y)
                    && (!showSpeechBubble || !bubbleBounds.contains(mousePos.x, mousePos.y))) {
                    currentState = UIState.MAIN; 
                    idCardOnTable = false;
                    isPatientWaiting = true;
                }
            }

            if (currentState == UIState.LEFT_ORB) {
                if (mousePos.x >= 270 && mousePos.x <= 370 && mousePos.y >= 25 && mousePos.y <= 50) {
                    isTypingBed = true;
                    isTypingName = false; isTypingId = false; isSearchingId = false; isTypingRightBed = false;
                    cursorTimer = 0f;
                    return;
                }
            }
            
            if (currentState == UIState.RIGHT_ORB) {
                if (mousePos.x >= 389 && mousePos.x <= 449 && mousePos.y >= 297 && mousePos.y <= 319) {
                    isTypingRightBed = true;
                    isTypingName = false; isTypingId = false; isSearchingId = false; isTypingBed = false;
                    cursorTimer = 0f;
                    return;
                }
            }

            if (currentState == UIState.SCROLL) {
                if (mousePos.x >= 220 && mousePos.x <= 440) {
                    if (mousePos.y >= 270 && mousePos.y <= 290) {
                        currentState = UIState.SCROLL_REGISTER;
                        inputName = ""; inputId = ""; inputSeverity = 0;
                        isTypingName = false; isTypingId = false;
                        cursorTimer = 0f;
                    } else if (mousePos.y >= 240 && mousePos.y <= 260) {
                        currentState = UIState.SCROLL_VIEW;
                        searchResult = ""; searchInputId = ""; isSearchingId = false;
                    }
                }
            } else if (currentState == UIState.SCROLL_REGISTER) {
                if (mousePos.x >= 140 && mousePos.x <= 580 && mousePos.y >= 270 && mousePos.y <= 330) {
                    isTypingName = true; isTypingId = false; cursorTimer = 0f; return;
                } else if (mousePos.x >= 140 && mousePos.x <= 580 && mousePos.y >= 230 && mousePos.y <= 290) {
                    isTypingId = true; isTypingName = false; cursorTimer = 0f; return;
                }
                for (int i = 0; i < 6; i++) {
                    int btnX = 220 + i * 32;
                    if (mousePos.x >= btnX && mousePos.x <= btnX + 28 && mousePos.y >= 145 && mousePos.y <= 173) {
                        inputSeverity = i + 1; return;
                    }
                }
                if (mousePos.x >= 220 && mousePos.x <= 380 && mousePos.y >= 90 && mousePos.y <= 120) {
                    if (!inputName.isEmpty() && !inputId.isEmpty() && inputSeverity > 0) {
                        hospitalManager.registerPatient(new Patient(inputId, inputName, "Diagnosticado", inputSeverity));
                        leftOrbSnapshot = hospitalManager.getPatientQueueSnapshot();
                        
                        isPatientWaiting = false; idCardOnTable = false; patientOnCounter = null; currentPatient = null;
                        inputName = ""; inputId = ""; inputSeverity = 0;
                        spawnTimer = 5f; currentState = UIState.SCROLL;
                    } return;
                }
            } else if (currentState == UIState.SCROLL_VIEW) {
                if (isSearchingId) {
                    if (!(mousePos.x >= 330 && mousePos.x <= 480 && mousePos.y >= 25 && mousePos.y <= 45)) {
                        isSearchingId = false; searchInputId = "";
                    }
                } else if (mousePos.x >= 170 && mousePos.x <= 320 && mousePos.y >= 25 && mousePos.y <= 40) {
                    isSearchingId = true; searchInputId = ""; searchResult = "";
                    cursorTimer = 0f;
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
        scrollTexture.dispose(); inkwellTexture.dispose();
        handTexture.dispose(); patientTexture.dispose();
        speechBubbleTexture.dispose();
        idCardTexture.dispose();
        bgTableTex.dispose();
        vacantBedTex.dispose();
        occupiedBedTex.dispose();
        patientQueueBgTex.dispose();
        patientQueueTex.dispose();
        redOrbTex.dispose();
        brownBgTex.dispose();
        bigScrollTex.dispose();
        if (whitePixel != null) whitePixel.dispose();
        font.dispose();
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }
}