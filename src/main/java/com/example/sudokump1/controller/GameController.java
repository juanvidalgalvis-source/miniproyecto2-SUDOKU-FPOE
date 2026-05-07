package com.example.sudokump1.controller;

import com.example.sudokump1.events.GameEventHandler;
import com.example.sudokump1.model.GameModel;
import com.example.sudokump1.model.SudokuModel;

import javafx.fxml.FXML;
import com.example.sudokump1.controller.keyboard.KeyboardAdapter;


import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
// (KeyCode import removed - keyboard is handled by adapter)

import javafx.scene.layout.GridPane;

/**
 * MVC Controller responsible for coordinating GUI interactions with the logical model.
 * Avoids logic inside FXML/View.
 */
public class GameController implements GameEventHandler {

    @FXML
    private GridPane boardGrid;

    @FXML
    private Label statusLabel;

    private final GameModel gameModel;

    public GameController() {
        this.gameModel = new GameModel();
    }

    @FXML
    public void initialize() {
        // Placeholder init. The actual board rendering will be added in task 0.7.
        SudokuModel sudokuModel = gameModel.getSudokuModel();
        if (statusLabel != null) {
            statusLabel.setText("Sudoku 6x6 listo");
        }

        // Wire keyboard adapter (placeholder behavior already implemented in adapter).
        if (boardGrid != null) {
            boardGrid.setFocusTraversable(true);
            boardGrid.addEventFilter(KeyEvent.KEY_PRESSED, new KeyboardAdapter(this));
        }

        // Build the initial visual board (6x6) based on the model.
        if (boardGrid != null) {
            renderBoardCells();
        }
    }

    // ----------------------
    // GameEventHandler API
    // ----------------------

    @Override
    public void handleCellClick(int row, int column) {
        gameModel.getSudokuModel().setSelectedCell(row, column);
        refreshValidationVisual();
    }

    @Override
    public void handleKeyInput(int number) {
        SudokuModel sudokuModel = gameModel.getSudokuModel();
        if (sudokuModel.getSelectedCell() == null) {
            return;
        }

        int row = sudokuModel.getSelectedCell().getRow();
        int column = sudokuModel.getSelectedCell().getColumn();
        sudokuModel.insertNumber(row, column, number);
        refreshValidationVisual();

        if (sudokuModel.isGameCompleted() && statusLabel != null) {
            statusLabel.setText("¡Juego completado!");
        }
    }

    @Override
    public void handleHelpRequest() {
        // Placeholder for future: show help/suggestions.
        if (statusLabel != null) {
            statusLabel.setText("Ayuda (placeholder)");
        }
    }

    @Override
    public void refreshValidationVisual() {
        // Placeholder for future view updates.
        // The view will read SudokuCell.invalid/value/fixed to style cells.
        if (statusLabel != null) {
            statusLabel.setText("Seleccion: " + formatSelectedCell());
        }
    }

    // (keyboard wiring is handled by KeyboardAdapter)

    /**
     * Task 0.7: builds a minimal 6x6 GridPane from the logical board.
     * At this step we only create visual placeholders (no full synchronization beyond style classes).
     */
    private void renderBoardCells() {
        boardGrid.getChildren().clear();
        boardGrid.getColumnConstraints().clear();
        boardGrid.getRowConstraints().clear();

        SudokuModel sudokuModel = gameModel.getSudokuModel();

        int size = SudokuModel.BOARD_SIZE;
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                final int finalRow = row;
                final int finalColumn = column;

                Label cellLabel = new Label();
                cellLabel.getStyleClass().add("cell");
                cellLabel.setPrefSize(60, 60);

                // Bind mouse -> event handler (adapter)
                cellLabel.setOnMouseClicked(e -> {
                    handleCellClick(finalRow, finalColumn);
                });

                boardGrid.add(cellLabel, column, row);
            }
        }

        // Initial selection styling
        refreshValidationVisual();
    }

    private String formatSelectedCell() {
        SudokuModel sudokuModel = gameModel.getSudokuModel();
        if (sudokuModel.getSelectedCell() == null) {
            return "(none)";
        }
        return sudokuModel.getSelectedCell().getRow() + "," + sudokuModel.getSelectedCell().getColumn();
    }
}

