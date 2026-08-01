package com.deskgoblin;

import com.badlogic.gdx.Game;
import com.deskgoblin.screens.GameScreen;

public class DeskGoblinGame extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
