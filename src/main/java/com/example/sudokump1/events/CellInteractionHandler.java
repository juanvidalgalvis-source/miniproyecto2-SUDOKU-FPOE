package com.example.sudokump1.events;

/**
 * Specialized interface focused on cell interactions.
 */
public interface CellInteractionHandler {
    void onCellClick(int row, int column);
}

