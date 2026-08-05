package com.deskgoblin.model.entities;

public class MedicalProcess {
    private Patient patient;
    private Bed bed;
    private boolean finished;

    private static final int STAGE_COUNT = 6;
    private static final float DEFAULT_STAGE_DURATION = 5.0f;

    // 6 Procedimentos fixos
    private final String[] stageNames = {
        "Procedimento 1", "Procedimento 2", "Procedimento 3", 
        "Procedimento 4", "Procedimento 5", "Procedimento 6"
    };
    private final float[] stageTimers = new float[STAGE_COUNT];
    private int currentStageIndex;

    public MedicalProcess(Patient patient, Bed bed) {
        this.patient = patient;
        this.bed = bed;
        this.currentStageIndex = 0;
        this.finished = false;
        // Inicializa todos os procedimentos com 5 segundos
        for (int i = 0; i < STAGE_COUNT; i++) {
            stageTimers[i] = DEFAULT_STAGE_DURATION;
        }
    }

    public void update(float delta) {
        if (finished) return;

        // Se o estágio atual ainda tem tempo, diminui
        if (stageTimers[currentStageIndex] > 0) {
            stageTimers[currentStageIndex] -= delta;
        }

        // Se o tempo do estágio atual acabou e ainda não é o último
        if (stageTimers[currentStageIndex] <= 0 && currentStageIndex < STAGE_COUNT - 1) {
            currentStageIndex++; // Avança para o próximo estágio
        } 
        // Se o tempo do último estágio (6) acabou
        else if (stageTimers[currentStageIndex] <= 0 && currentStageIndex == STAGE_COUNT - 1) {
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public Patient getPatient() { return patient; }
    public Bed getBed() { return bed; }

    public int getCurrentStageIndex() { return Math.min(currentStageIndex, STAGE_COUNT - 1); }
    
    public String getStageName(int index) { return stageNames[index]; }
    
    public float getStageTimer(int index) { return stageTimers[index]; }

    public int getStageCount() { return STAGE_COUNT; }
    
    public float getTotalTimeRemaining() {
        float total = 0;
        for (int i = currentStageIndex; i < STAGE_COUNT; i++) {
            total += stageTimers[i];
        }
        return total;
    }
}