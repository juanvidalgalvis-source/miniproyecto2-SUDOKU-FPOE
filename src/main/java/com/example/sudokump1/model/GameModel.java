package com.example.sudokump1.model;

/**
 * Facade used by MVC layers.
 * Keeps the GUI/controllers decoupled from the core Sudoku logic.
 */
public class GameModel {
    private final SudokuModel sudokuModel;

    public GameModel() {
        this.sudokuModel = new SudokuModel();
    }

    public SudokuModel getSudokuModel() {
        return sudokuModel;
    }
}

