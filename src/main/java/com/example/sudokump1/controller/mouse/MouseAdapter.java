package com.example.sudokump1.controller.mouse;

import com.example.sudokump1.events.GameEventHandler;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Adapter between JavaFX mouse events and the game event handler.
 *
 * Note: row/column are expected to be encoded by the caller (via setters) in this simplified adapter.
 */
public class MouseAdapter implements EventHandler<MouseEvent> {

    private final GameEventHandler gameEventHandler;
    private final int row;
    private final int column;

    public MouseAdapter(GameEventHandler gameEventHandler, int row, int column) {
        this.gameEventHandler = gameEventHandler;
        this.row = row;
        this.column = column;
    }

    @Override
    public void handle(MouseEvent event) {
        gameEventHandler.handleCellClick(row, column);
    }
}

