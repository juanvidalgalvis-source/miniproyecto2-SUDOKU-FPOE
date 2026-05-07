package com.example.sudokump1.events;

/**
 * Generic game event handler to decouple JavaFX input events from the model.
 */
public interface GameEventHandler {
    void handleCellClick(int row, int column);

    void handleKeyInput(int number);

    void handleHelpRequest();

    void refreshValidationVisual();
}

