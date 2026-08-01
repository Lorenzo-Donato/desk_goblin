package com.deskgoblin.util;

import com.badlogic.gdx.Gdx;

public class GameLogger {
    public static void info(String tag, String message) {
        Gdx.app.log(tag, message);
    }
    
    public static void error(String tag, String message) {
        Gdx.app.error(tag, message);
    }
    
    public static void error(String tag, String message, Throwable exception) {
        Gdx.app.error(tag, message, exception);
    }
    
    public static void debug(String tag, String message) {
        Gdx.app.debug(tag, message);
    }
}
