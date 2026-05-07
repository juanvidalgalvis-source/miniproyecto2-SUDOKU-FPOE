package com.example.sudokump1.controller.keyboard;

import com.example.sudokump1.events.GameEventHandler;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * Adapter between JavaFX keyboard events and the game event handler.
 */
public class KeyboardAdapter implements EventHandler<KeyEvent> {

    private final GameEventHandler gameEventHandler;

    public KeyboardAdapter(GameEventHandler gameEventHandler) {
        this.gameEventHandler = gameEventHandler;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event == null || event.getCode() == null) {
            return;
        }

        // Help placeholder: H
        switch (event.getCode()) {
            case H -> {
                gameEventHandler.handleHelpRequest();
                return;
            }
            default -> {
                // continue
            }
        }

        // digit keys 1..6
        String text = event.getText();
        if (text != null && !text.isBlank()) {
            char ch = text.charAt(0);
            if (ch >= '1' && ch <= '6') {
                int number = ch - '0';
                gameEventHandler.handleKeyInput(number);
                return;
            }
        }

        // Backspace/Delete clears
        switch (event.getCode()) {
            case BACK_SPACE, DELETE -> gameEventHandler.handleKeyInput(0);
            default -> {
                // ignore
            }
        }
    }
}

