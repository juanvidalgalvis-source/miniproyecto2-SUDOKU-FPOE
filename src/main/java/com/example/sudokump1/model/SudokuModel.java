package com.example.sudokump1.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Logic model for a 6x6 Sudoku.
 *
 * No GUI and no JavaFX dependencies.
 */
public class SudokuModel {
    // Board is 6x6.
    public static final int BOARD_SIZE = 6;

    // Blocks are 2x3 (2 rows x 3 columns).
    public static final int BLOCK_ROWS = 2;
    public static final int BLOCK_COLUMNS = 3;

    private final List<List<SudokuCell>> currentBoard;
    private final List<List<SudokuCell>> solutionBoard;
    private final Set<String> fixedCells;

    private SudokuCell selectedCell;
    private boolean gameCompleted;

    public SudokuModel() {
        // Creates an empty logical board for the player's current state.
        this.currentBoard = createEmptyBoard();

        // Creates an empty solution board (will be filled later in future tasks).
        this.solutionBoard = createEmptyBoard();

        // Stores coordinates of fixed cells so they cannot be edited by the user.
        this.fixedCells = new HashSet<>();

        // Initially no cell is selected.
        this.selectedCell = null;

        // Initially game is not completed.
        this.gameCompleted = false;
    }

    public List<List<SudokuCell>> getCurrentBoard() {
        return currentBoard;
    }

    public List<List<SudokuCell>> getSolutionBoard() {
        return solutionBoard;
    }

    public SudokuCell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(int row, int column) {
        this.selectedCell = getCellOrThrow(row, column);
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }

    /**
     * Sets up a dynamic initial board using the provided values.
     *
     * @param initialValues value matrix as rows/columns with 0 meaning empty.
     *                       This method is only used to initialize fixed cells.
     *                       Lengths must be BOARD_SIZE.
     */
    public void setInitialBoard(List<List<Integer>> initialValues) {
        Objects.requireNonNull(initialValues, "initialValues");
        if (initialValues.size() != BOARD_SIZE) {
            throw new IllegalArgumentException("initialValues must have BOARD_SIZE rows");
        }

        for (int row = 0; row < BOARD_SIZE; row++) {
            List<Integer> rowValues = initialValues.get(row);
            if (rowValues.size() != BOARD_SIZE) {
                throw new IllegalArgumentException("initialValues rows must have BOARD_SIZE columns");
            }
        }

        fixedCells.clear();
        gameCompleted = false;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                int value = initialValues.get(row).get(column);
                boolean isFixed = value != 0;

                SudokuCell cell = currentBoard.get(row).get(column);
                cell.setFixed(isFixed);
                cell.setInvalid(false);
                cell.setValue(isFixed ? value : 0);

                if (isFixed) {
                    fixedCells.add(coordinatesKey(row, column));
                }
            }
        }

        // solutionBoard remains independent. The caller/controller can populate it later.
        // For now, completion is based on currentBoard consistency + being filled.
        updateInvalidFlags();
        this.gameCompleted = isBoardComplete();
    }

    public boolean insertNumber(int row, int column, int number) {
        SudokuCell cell = getCellOrThrow(row, column);
        if (cell.isFixed()) {
            return false;
        }
        if (!isNumberInRange(number)) {
            return false;
        }

        if (number == 0) {
            clearCell(row, column);
            return true;
        }

        boolean valid = isValidMove(row, column, number);
        cell.setValue(number);
        cell.setInvalid(!valid);

        if (valid) {
            // Clear invalid flags for other cells that might be affected (simple approach).
            updateInvalidFlags();
        }

        this.gameCompleted = isBoardComplete();
        return valid;
    }

    public void clearCell(int row, int column) {
        SudokuCell cell = getCellOrThrow(row, column);
        if (cell.isFixed()) {
            return;
        }
        cell.setValue(0);
        cell.setInvalid(false);
        updateInvalidFlags();
        this.gameCompleted = isBoardComplete();
    }

    public boolean isValidMove(int row, int column, int number) {
        if (!isNumberInRange(number)) {
            return false;
        }
        if (fixedCells.contains(coordinatesKey(row, column))) {
            // fixed cells are not editable; consider their value validation elsewhere
            return true;
        }

        // Check row
        for (int col = 0; col < BOARD_SIZE; col++) {
            if (col == column) {
                continue;
            }
            int existing = currentBoard.get(row).get(col).getValue();
            if (existing == number) {
                return false;
            }
        }

        // Check column
        for (int r = 0; r < BOARD_SIZE; r++) {
            if (r == row) {
                continue;
            }
            int existing = currentBoard.get(r).get(column).getValue();
            if (existing == number) {
                return false;
            }
        }

        // Check block 2x3
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startColumn = (column / BLOCK_COLUMNS) * BLOCK_COLUMNS;
        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startColumn; c < startColumn + BLOCK_COLUMNS; c++) {
                if (r == row && c == column) {
                    continue;
                }
                int existing = currentBoard.get(r).get(c).getValue();
                if (existing == number) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isBoardComplete() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                SudokuCell cell = currentBoard.get(row).get(column);
                if (cell.getValue() == 0) {
                    return false;
                }
            }
        }

        // If full and no cell is marked invalid, we consider it complete.
        // (The controller/view can display invalid flags in real time.)
        return !hasAnyInvalidCell();
    }

    private boolean hasAnyInvalidCell() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                if (currentBoard.get(row).get(column).isInvalid()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Recomputes invalid flags using sets for efficiency.
     */
    private void updateInvalidFlags() {
        // Reset invalid
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                currentBoard.get(row).get(column).setInvalid(false);
            }
        }

        // Row constraints
        for (int row = 0; row < BOARD_SIZE; row++) {
            markDuplicatesInLine(getRowValues(row), row, 0, true);
        }

        // Column constraints
        for (int column = 0; column < BOARD_SIZE; column++) {
            markDuplicatesInLine(getColumnValues(column), 0, column, false);
        }

        // Block constraints
        for (int blockRow = 0; blockRow < BOARD_SIZE / BLOCK_ROWS; blockRow++) {
            for (int blockColumn = 0; blockColumn < BOARD_SIZE / BLOCK_COLUMNS; blockColumn++) {
                markDuplicatesInBlock(blockRow, blockColumn);
            }
        }
    }

    private List<Integer> getRowValues(int row) {
        List<Integer> values = new ArrayList<>();
        for (int column = 0; column < BOARD_SIZE; column++) {
            values.add(currentBoard.get(row).get(column).getValue());
        }
        return values;
    }

    private List<Integer> getColumnValues(int column) {
        List<Integer> values = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            values.add(currentBoard.get(row).get(column).getValue());
        }
        return values;
    }

    private void markDuplicatesInLine(List<Integer> values, int fixedIndex, int varyingIndex, boolean isRow) {
        HashSet<Integer> seen = new HashSet<>();
        HashSet<Integer> duplicates = new HashSet<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            int value = values.get(i);
            if (value == 0) {
                continue;
            }
            if (!seen.add(value)) {
                duplicates.add(value);
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            int value = values.get(i);
            if (value == 0) {
                continue;
            }
            if (!duplicates.contains(value)) {
                continue;
            }

            int row = isRow ? fixedIndex : i;
            int column = isRow ? i : fixedIndex;
            currentBoard.get(row).get(column).setInvalid(true);
        }
    }

    private void markDuplicatesInBlock(int blockRow, int blockColumn) {
        int startRow = blockRow * BLOCK_ROWS;
        int startColumn = blockColumn * BLOCK_COLUMNS;

        HashSet<Integer> seen = new HashSet<>();
        HashSet<Integer> duplicates = new HashSet<>();

        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startColumn; c < startColumn + BLOCK_COLUMNS; c++) {
                int value = currentBoard.get(r).get(c).getValue();
                if (value == 0) {
                    continue;
                }
                if (!seen.add(value)) {
                    duplicates.add(value);
                }
            }
        }

        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startColumn; c < startColumn + BLOCK_COLUMNS; c++) {
                int value = currentBoard.get(r).get(c).getValue();
                if (value != 0 && duplicates.contains(value)) {
                    currentBoard.get(r).get(c).setInvalid(true);
                }
            }
        }
    }

    private boolean isNumberInRange(int number) {
        return number >= 1 && number <= BOARD_SIZE;
    }

    private SudokuCell getCellOrThrow(int row, int column) {
        if (row < 0 || row >= BOARD_SIZE || column < 0 || column >= BOARD_SIZE) {
            throw new IndexOutOfBoundsException("row/column out of range");
        }
        return currentBoard.get(row).get(column);
    }

    private List<List<SudokuCell>> createEmptyBoard() {
        List<List<SudokuCell>> board = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            List<SudokuCell> rowCells = new ArrayList<>();
            for (int column = 0; column < BOARD_SIZE; column++) {
                rowCells.add(new SudokuCell(row, column));
            }
            board.add(rowCells);
        }
        return board;
    }

    private String coordinatesKey(int row, int column) {
        return row + ":" + column;
    }
}

